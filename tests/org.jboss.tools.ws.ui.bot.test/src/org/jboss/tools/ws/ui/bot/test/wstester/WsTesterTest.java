/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.wstester;

import static org.junit.Assert.assertEquals;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.uiforms.impl.expandablecomposite.DefaultExpandableComposite;
import org.jboss.tools.ws.reddeer.swt.condition.WsTesterNotEmptyResponseText;
import org.jboss.tools.ws.reddeer.ui.dialogs.InputDialog;
import org.jboss.tools.ws.reddeer.ui.tester.views.SelectWSDLDialog;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView.RequestType;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for Web Service Tester
 *
 * @author jlukas
 */
@RunWith(RedDeerSuite.class)
public class WsTesterTest {

	private static final String SERVICE_URL = "http://www.webservicex.net/BibleWebservice.asmx";
	private static final Logger LOGGER = Logger.getLogger(WsTesterTest.class.getName());

	private WsTesterView wstv;

	@Before
	public void openWsTester() {
		wstv = wstv == null ? new WsTesterView() : wstv;
		if (!wstv.isOpened()) {
			wstv.open();
		}
	}

	@AfterClass
	public static void deleteProjects() {
		ProjectHelper.deleteAllProjects();
	}

	@After
	public void clearResponseBody() {
		wstv.activate();
		new DefaultText(new DefaultExpandableComposite(JBossWSUIMessages.JAXRSWSTestView2_ResponseBody_Section))
				.setText("");
	}

	/**
	 * Test behavior of UI
	 */
	@Test
	public void testUI() {
		wstv.setRequestType(RequestType.PUT);
		Assert.assertEquals(RequestType.PUT, wstv.getRequestType());
		wstv.setRequestType(RequestType.JAX_WS);
		Assert.assertEquals(RequestType.JAX_WS, wstv.getRequestType());
		wstv.setRequestType(RequestType.DELETE);
		Assert.assertEquals(RequestType.DELETE, wstv.getRequestType());

		wstv.setRequestHeadersSectionExpansion(true);
		wstv.addHeaderRequestArg("a", "1");
		wstv.addHeaderRequestArg("b", "2");
		wstv.addHeaderRequestArg("c", "3");
		Assert.assertEquals(3, wstv.getHeaderRequestArgs().keySet().size());
		Assert.assertTrue(wstv.getHeaderRequestArgs().containsKey("b"));

		wstv.addHeaderRequestArg("d", "4");
		Assert.assertEquals(4, wstv.getHeaderRequestArgs().keySet().size());
		Assert.assertTrue(wstv.getHeaderRequestArgs().containsKey("d"));
		Assert.assertEquals(4, wstv.getHeaderRequestArgs().keySet().size());

		wstv.removeHeaderRequestArg("a", "1");
		wstv.removeHeaderRequestArg("c", "3");
		Assert.assertEquals(2, wstv.getHeaderRequestArgs().keySet().size());

		wstv.clearHeaderRequestArgs();
		Assert.assertEquals(0, wstv.getHeaderRequestArgs().keySet().size());

		wstv.setRequestType(RequestType.JAX_WS);
		selectPort(wstv, "BibleWebserviceSoap");
		Assert.assertTrue(wstv.getRequestBody().contains("http://schemas.xmlsoap.org/soap/envelope/"));

		selectPort(wstv, "BibleWebserviceSoap12");
		Assert.assertTrue("Got: " + wstv.getRequestBody(),
				wstv.getRequestBody().contains("http://www.w3.org/2003/05/soap-envelope"));
	}

	/**
	 * Test refreshing body requests in the UI
	 */
	@Test
	public void testNamespaces() {
		String uri = new File(prepareWsdl(), "original.wsdl").toURI().toString();
		SelectWSDLDialog dlg = wstv.invokeGetFromWSDL();
		dlg.setURI(uri);
		AbstractWait.sleep(TimePeriod.SHORT);
		List<String> items = dlg.getServices();

		LOGGER.log(Level.FINE, "Services: {0}", items);
		Assert.assertEquals(2, items.size());
		Assert.assertTrue(items.contains("EchoService"));
		dlg.selectService("EchoService");
		items = dlg.getPorts();

		LOGGER.log(Level.FINE, "Ports: {0}", items);
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.contains("EchoPort"));
		items = dlg.getOperations();

		LOGGER.log(Level.FINE, "Operations: {0}", items);
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.contains("echo"));
		dlg.ok();

		LOGGER.log(Level.INFO, "Request: {0}", wstv.getRequestBody());
		Assert.assertTrue(wstv.getRequestBody().contains("<echo xmlns=\"http://test.jboss.org/ns\""));
		dlg = wstv.invokeGetFromWSDL();
		dlg.setURI(uri);
		AbstractWait.sleep(TimePeriod.SHORT);
		items = dlg.getServices();

		LOGGER.log(Level.FINE, "Services: {0}", items);
		Assert.assertEquals(2, items.size());
		Assert.assertTrue(items.contains("gsearch_rss"));
		dlg.selectService("gsearch_rss");
		items = dlg.getPorts();

		LOGGER.log(Level.FINE, "Ports: {0}", items);
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.contains("gsearch_rssSoap"));
		items = dlg.getOperations();

		LOGGER.log(Level.FINE, "Operations: {0}", items);
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.contains("GetSearchResults"));
		dlg.ok();

		LOGGER.log(Level.INFO, "Request: {0}", wstv.getRequestBody());
		Assert.assertTrue(
				wstv.getRequestBody().contains("<GetSearchResults xmlns=\"http://www.ecubicle.net/webservices\""));
	}

	/**
	 * Test SOAP service invocation
	 * 
	 * Fails due to JBDS-3907
	 * 
	 * @see https://issues.jboss.org/browse/JBDS-3907
	 */
	@Test
	public void testSOAPService() {
		wstv.setRequestType(RequestType.JAX_WS);
		Assert.assertEquals(RequestType.JAX_WS, wstv.getRequestType());
		wstv.setServiceURL(SERVICE_URL + "?WSDL");

		InputStream is = WsTesterTest.class.getResourceAsStream("/resources/jbossws/message_soap_out.xml");
		wstv.setRequestBody(readResource(is));
		wstv.invoke();

		new WaitUntil(new WsTesterNotEmptyResponseText(), TimePeriod.getCustom(20));
		String rsp = wstv.getResponseBody();
		LOGGER.log(Level.FINE, "SOAP response: {0}", rsp);
		Assert.assertTrue(rsp.trim().length() > 0);
		checkResponse(rsp, "&lt;BookTitle&gt;Mark&lt;/BookTitle&gt;");
	}

	/**
	 * Test SOAP 1.2 service invocation
	 * Fails due to JBDS-3907
	 * 
	 * @see https://issues.jboss.org/browse/JBDS-3907
	 */
	@Test
	public void testSOAP12Service() {
		wstv.setRequestType(RequestType.JAX_WS);
		assertEquals(RequestType.JAX_WS, wstv.getRequestType());		

		SelectWSDLDialog selectWSDLDialog = wstv.invokeGetFromWSDL();
		try {
			selectWSDLDialog.openURL();
			final String wsdlURLDialogTitle = "WSDL URL";
			InputDialog wsdlURLDialog = new InputDialog(wsdlURLDialogTitle);
			wsdlURLDialog.setInputText(SERVICE_URL + "?WSDL");
			wsdlURLDialog.ok();
			new DefaultShell(selectWSDLDialog.TITLE);
			assertEquals(SERVICE_URL + "?WSDL", selectWSDLDialog.getURI());
			selectWSDLDialog.selectPort("BibleWebserviceSoap12");
			selectWSDLDialog.ok();
		} finally {
			if (new ShellWithTextIsAvailable(selectWSDLDialog.TITLE).test()) {
				selectWSDLDialog.close();
			}
		}
		Assert.assertEquals(SERVICE_URL + "?WSDL", wstv.getServiceURL());
		InputStream is = WsTesterTest.class.getResourceAsStream("/resources/jbossws/message_soap12_out.xml");
		wstv.setRequestBody(readResource(is));
		wstv.invoke();

		String rsp = wstv.getResponseBody();
		LOGGER.log(Level.FINE, "SOAP response: {0}", rsp);
		Assert.assertTrue(rsp.trim().length() > 0);
		checkResponse(rsp, "&lt;BookTitle&gt;Mark&lt;/BookTitle&gt;");
	}

	/**
	 * Test REST service invocation (GET request)
	 */
	@Test
	public void testRESTGETService() {
		wstv.setRequestType(RequestType.GET);
		wstv.setServiceURL(SERVICE_URL + "/GetBibleWordsByChapterAndVerse");
		wstv.setRequestParametersSectionExpansion(true);
		wstv.addParameterRequestArg("BookTitle", "Luke");
		wstv.addParameterRequestArg("chapter", "2");
		wstv.addParameterRequestArg("Verse", "2");
		wstv.editParameterRequestArg("chapter", "2", "chapter", "1");

		try {
			wstv.invoke();
			new WaitUntil(new WsTesterNotEmptyResponseText(), TimePeriod.getCustom(5), false);
			String rsp = wstv.getResponseBody();
			String[] rspHeaders = wstv.getResponseHeaders();
			LOGGER.log(Level.FINE, "REST response: {0}", rsp);
			LOGGER.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
			Assert.assertTrue(rsp.trim().length() > 0);
			checkResponse(rsp, "&lt;Chapter&gt;1&lt;/Chapter&gt;");
			checkResponse(rsp, "ministers of the word");
		} finally {
			wstv.clearParameterRequestArgs();
		}
	}

	/**
	 * Test REST service invocation (POST request)
	 */
	@Test
	public void testRESTPOSTService() {
		wstv.setRequestType(WsTesterView.RequestType.POST);
		wstv.setServiceURL(SERVICE_URL + "/GetBibleWordsByChapterAndVerse");
		String requestBody = "BookTitle=John&chapter=3&Verse=1\r";
		wstv.setRequestBody(requestBody);
		wstv.setResponseHeadersSectionExpansion(true);
		wstv.addHeaderRequestArg("Content-Type", "application/x-www-form-urlencoded");
		wstv.addHeaderRequestArg("Content-Length", String.valueOf(requestBody.length()));

		try {
			wstv.invoke();
			new WaitUntil(new WsTesterNotEmptyResponseText(), TimePeriod.getCustom(5), false);
			String rsp = wstv.getResponseBody();
			String[] rspHeaders = wstv.getResponseHeaders();
			LOGGER.log(Level.FINE, "REST response: {0}", rsp);
			LOGGER.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
			Assert.assertTrue("Empty response body", rsp.trim().length() > 0);
			checkResponse(rsp, "&lt;Chapter&gt;3&lt;/Chapter&gt;");
			checkResponse(rsp, "There was a man of the Pharisees, named Nicodemus, a ruler of the Jews");
		} finally {
			wstv.clearHeaderRequestArgs();
		}
	}

	@Test
	public void testErrorResponse() {
		wstv.setRequestType(RequestType.GET);
		wstv.setServiceURL("https://watchful.li/api/v1/sites");
		wstv.invoke();

		new WaitUntil(new ShellWithTextIsAvailable(""));
		new OkButton().click();
		new WaitUntil(new WsTesterNotEmptyResponseText(), TimePeriod.getCustom(5), false);
		Assert.assertEquals(0, wstv.getParameterRequestArgs().size());

		String rsp = wstv.getResponseBody();
		String[] rspHeaders = wstv.getResponseHeaders();
		LOGGER.log(Level.FINE, "REST response: {0}", rsp);
		LOGGER.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
		Assert.assertTrue(rsp.trim().length() > 0);
		checkResponse(rsp, "Invalid API key");
	}

	private String readResource(InputStream is) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s);
				sb.append('\n');
			}
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.log(Level.FINEST, e.getMessage(), e);
				}
			}
		}
		return sb.toString();
	}

	private void checkResponse(String rsp, String expContent) {
		if (!rsp.contains(expContent)) {
			if (rsp.contains("503")) { // 503 Service Unavailable
				throw new AssertionError("Service Unavailable: " + SERVICE_URL);
			} else {
				throw new AssertionError("Response doesn't contain \"" + expContent + "\"" + "\nResponse was:" + rsp);
			}
		}
	}

	private File prepareWsdl() {
		String[] files = { "imported.wsdl", "original.wsdl", "schema.xsd" };
		File targetFolder = new File(System.getProperty("java.io.tmpdir"), "WsTesterTest");
		targetFolder.mkdirs();
		for (String file : files) {
			InputStream is = WsTesterTest.class.getResourceAsStream("/resources/wsdl/" + file);
			File target = new File(targetFolder, file);
			if (target.exists()) {
				target.delete();
			}
			try {
				OutputStream os = new BufferedOutputStream(new FileOutputStream(target));
				copy(is, os);
			} catch (FileNotFoundException fnfe) {
				throw new RuntimeException(fnfe);
			}
		}
		return targetFolder;
	}

	private void copy(InputStream in, OutputStream out) {
		byte[] buf = new byte[1024];
		int len;
		try {
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe2) {
					LOGGER.log(Level.WARNING, ioe2.getMessage(), ioe2);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe2) {
					LOGGER.log(Level.WARNING, ioe2.getMessage(), ioe2);
				}
			}
		}
	}

	private void selectPort(WsTesterView wstv, String portName) {
		SelectWSDLDialog dlg = wstv.invokeGetFromWSDL();
		
		try {
			dlg.openURL();
			InputDialog wsdUrlDialog = new InputDialog("WSDL URL");
			wsdUrlDialog.typeInputText(SERVICE_URL + "?WSDL");
			wsdUrlDialog.ok();
			new DefaultShell(dlg.TITLE);

			Assert.assertEquals(SERVICE_URL + "?WSDL", dlg.getURI());
			List<String> items = dlg.getServices();
			LOGGER.log(Level.INFO, "Services: {0}", items);
			Assert.assertEquals(1, items.size());
			Assert.assertTrue(items.contains("BibleWebservice"));
			items = dlg.getPorts();

			LOGGER.log(Level.INFO, "Ports: {0}", items);
			Assert.assertEquals(2, items.size());
			Assert.assertTrue(items.contains("BibleWebserviceSoap"));
			Assert.assertTrue(items.contains("BibleWebserviceSoap12"));
			dlg.selectPort(portName);
			items = dlg.getOperations();

			LOGGER.log(Level.INFO, "Operations: {0}", items);
			Assert.assertEquals(4, items.size());
			Assert.assertTrue(items.contains("GetBookTitles"));
			Assert.assertTrue(items.contains("GetBibleWordsByChapterAndVerse"));
			dlg.selectOperation("GetBibleWordsbyKeyWord");
			dlg.ok();
			Assert.assertEquals("http://www.webservicex.net/BibleWebservice.asmx?WSDL", wstv.getServiceURL());
		} finally {
			if (new ShellWithTextIsAvailable(dlg.TITLE).test()) {
				dlg.close();
			}
		}
	}
}
