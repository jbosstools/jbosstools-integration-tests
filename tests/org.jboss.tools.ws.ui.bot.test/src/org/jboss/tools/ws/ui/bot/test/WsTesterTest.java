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
package org.jboss.tools.ws.ui.bot.test;

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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ws.ui.bot.test.widgets.SelectWSDLDialog;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView.Request_Arg_Type;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView.Request_Type;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for Web Service Tester
 *
 * @author jlukas
 */
public class WsTesterTest extends SWTTestExt {

	private static final Logger L = Logger.getLogger(WsTesterTest.class.getName());
	private static final String SERVICE_URL = "http://www.webservicex.net/BibleWebservice.asmx";

	/**
	 * Test behavior of UI
	 */
	@Test
	public void testUI() {
		WsTesterView wstv = new WsTesterView();
		SWTBotView viewBot = wstv.show();
		Assert.assertTrue("Tester View is not active", viewBot.isActive());
		wstv.setRequestType(Request_Type.PUT);
		Assert.assertEquals(Request_Type.PUT, wstv.getRequestType());
		wstv.setRequestType(Request_Type.JAX_WS);
		Assert.assertEquals(Request_Type.JAX_WS, wstv.getRequestType());

		wstv.setRequestType(Request_Type.DELETE);
		Assert.assertEquals(Request_Type.DELETE, wstv.getRequestType());
		wstv.expandSection(Request_Arg_Type.HEADER.toString());
		wstv.addRequestArg(Request_Arg_Type.HEADER, "a", "1");
		wstv.addRequestArg(Request_Arg_Type.HEADER, "b", "2");
		wstv.addRequestArg(Request_Arg_Type.HEADER, "c", "3");
		Assert.assertEquals(3, wstv.getRequestArgs(Request_Arg_Type.HEADER).keySet().size());
		Assert.assertTrue(wstv.getRequestArgs(Request_Arg_Type.HEADER).containsKey("b"));
		wstv.addRequestArg(Request_Arg_Type.HEADER, "d", "4");
		Assert.assertEquals(4, wstv.getRequestArgs(Request_Arg_Type.HEADER).keySet().size());
		Assert.assertTrue(wstv.getRequestArgs(Request_Arg_Type.HEADER).containsKey("d"));
		Assert.assertEquals(4, wstv.getRequestArgs(Request_Arg_Type.HEADER).keySet().size());
		wstv.removeRequestArg(Request_Arg_Type.HEADER, "a", "1");
		wstv.removeRequestArg(Request_Arg_Type.HEADER, "c", "3");
		Assert.assertEquals(2, wstv.getRequestArgs(Request_Arg_Type.HEADER).keySet().size());
		wstv.clearRequestArgs(Request_Arg_Type.HEADER);
		Assert.assertEquals(0, wstv.getRequestArgs(Request_Arg_Type.HEADER).keySet().size());

		wstv.setRequestType(Request_Type.JAX_WS);
		selectPort(wstv, "BibleWebserviceSoap");
		Assert.assertTrue(wstv.getRequestBody().contains("http://schemas.xmlsoap.org/soap/envelope/"));
		selectPort(wstv, "BibleWebserviceSoap12");
		Assert.assertTrue("Got: " + wstv.getRequestBody(), wstv.getRequestBody().contains("http://www.w3.org/2003/05/soap-envelope"));
		viewBot.close();
	}

	/**
	 * Test refreshing body requests in the UI
	 */
	@Test
	public void testNamespaces() {
		String uri = new File(prepareWsdl(), "original.wsdl").toURI().toString();
		WsTesterView wstv = new WsTesterView();
		SWTBotView viewBot = wstv.show();
		Assert.assertTrue("Tester View is not active", viewBot.isActive());
		SelectWSDLDialog dlg = wstv.getFromWSDL();
		dlg.setURI(uri);
		bot.sleep(1000);
		List<String> items = dlg.getServices();
		L.log(Level.FINE, "Services: {0}", items);
		Assert.assertEquals(2, items.size());
		Assert.assertTrue(items.contains("EchoService"));
		items = dlg.getPorts();
		L.log(Level.FINE, "Ports: {0}", items);
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.contains("EchoPort"));
		items = dlg.getOperations();
		L.log(Level.FINE, "Operations: {0}", items);
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.contains("echo"));
		dlg.ok();
		L.log(Level.INFO, "Request: {0}", wstv.getRequestBody());
		Assert.assertTrue(wstv.getRequestBody().contains(
				"<echo xmlns = \"http://test.jboss.org/ns\">"));

		dlg = wstv.getFromWSDL();
		dlg.setURI(uri);
		bot.sleep(1000);
		items = dlg.getServices();
		L.log(Level.FINE, "Services: {0}", items);
		Assert.assertEquals(2, items.size());
		Assert.assertTrue(items.contains("gsearch_rss"));
		dlg.selectService("gsearch_rss");
		items = dlg.getPorts();
		L.log(Level.FINE, "Ports: {0}", items);
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.contains("gsearch_rssSoap"));
		items = dlg.getOperations();
		L.log(Level.FINE, "Operations: {0}", items);
		Assert.assertEquals(1, items.size());
		Assert.assertTrue(items.contains("GetSearchResults"));
		dlg.ok();
		L.log(Level.INFO, "Request: {0}", wstv.getRequestBody());
		Assert.assertTrue(wstv.getRequestBody().contains(
						"<GetSearchResults xmlns = \"http://www.ecubicle.net/webservices\">"));
	}

	/**
	 * Test SOAP service invocation
	 */
	@Test
	public void testSOAPService() {
		WsTesterView wstv = new WsTesterView();
		wstv.show();
		wstv.setRequestType(Request_Type.JAX_WS);
		Assert.assertEquals(Request_Type.JAX_WS, wstv.getRequestType());
		wstv.setServiceURL(SERVICE_URL);
		InputStream is = WsTesterTest.class.getResourceAsStream("/resources/jbossws/message_soap_out.xml");
		wstv.setRequestBody(readResource(is));
		wstv.invoke();
		String rsp = wstv.getResponseBody();
		L.log(Level.FINE, "SOAP response: {0}", rsp);
		Assert.assertTrue(rsp.trim().length() > 0);
		checkResponse(rsp, "&lt;BookTitle&gt;Mark&lt;/BookTitle&gt;");
	}

	/**
	 * Test SOAP 1.2 service invocation
	 */
	@Test
	public void testSOAP12Service() {
		WsTesterView wstv = new WsTesterView();
		wstv.show();
		wstv.setRequestType(Request_Type.JAX_WS);
		Assert.assertEquals(Request_Type.JAX_WS, wstv.getRequestType());
		SelectWSDLDialog dlg = wstv.getFromWSDL();
		try {
			dlg.openURL();
			SWTBotShell sh = bot.activeShell();
			sh.bot().text().typeText(SERVICE_URL + "?WSDL");
			sh.bot().button("OK").click();
			bot.sleep(1000);
			Assert.assertEquals(SERVICE_URL + "?WSDL", dlg.getURI());
			dlg.selectPort("BibleWebserviceSoap12");
			dlg.ok();
		} finally {
			if (dlg.isOpen()) {
				dlg.close();
			}
		}
		Assert.assertEquals(SERVICE_URL, wstv.getServiceURL());
		InputStream is = WsTesterTest.class.getResourceAsStream("/resources/jbossws/message_soap12_out.xml");
		wstv.setRequestBody(readResource(is));
		wstv.invoke();
		String rsp = wstv.getResponseBody();
		L.log(Level.FINE, "SOAP response: {0}", rsp);
		Assert.assertTrue(rsp.trim().length() > 0);
		checkResponse(rsp, "&lt;BookTitle&gt;Mark&lt;/BookTitle&gt;");
	}

	/**
	 * Test REST service invocation (GET request)
	 */
	@Test
	public void testRESTGETService() {
		WsTesterView wstv = new WsTesterView();
		wstv.show();
		wstv.setRequestType(Request_Type.GET);
		wstv.setServiceURL(SERVICE_URL + "/GetBibleWordsByChapterAndVerse");
		wstv.expandSection(Request_Arg_Type.PARAMETER.toString());
		wstv.addRequestArg(Request_Arg_Type.PARAMETER, "BookTitle", "Luke");
		wstv.addRequestArg(Request_Arg_Type.PARAMETER, "chapter", "2");
		wstv.addRequestArg(Request_Arg_Type.PARAMETER, "Verse",	"2");
		wstv.editRequestArg(Request_Arg_Type.PARAMETER, "chapter", "2", "chapter", "1");
		try {
			wstv.invoke();
			String rsp = wstv.getResponseBody();
			String[] rspHeaders = wstv.getResponseHeaders();
			L.log(Level.FINE, "REST response: {0}", rsp);
			L.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
			Assert.assertTrue(rsp.trim().length() > 0);
			checkResponse(rsp, "&lt;Chapter&gt;1&lt;/Chapter&gt;");
			checkResponse(rsp, "ministers of the word");
		} finally {
			wstv.clearRequestArgs(Request_Arg_Type.PARAMETER);
		}
	}

	/**
	 * Test REST service invocation (POST request)
	 */
	@Test
	public void testRESTPOSTService() {
		WsTesterView wstv = new WsTesterView();
		wstv.show();
		wstv.setRequestType(WsTesterView.Request_Type.POST);
		wstv.setServiceURL(SERVICE_URL + "/GetBibleWordsByChapterAndVerse");
		String requestBody = "BookTitle=John&chapter=3&Verse=1\r";
		wstv.setRequestBody(requestBody);
		wstv.expandSection(JBossWSUIMessages.JAXRSWSTestView2_ResponseHeaders_Section);
		wstv.addRequestArg(Request_Arg_Type.HEADER,	"Content-Type", "application/x-www-form-urlencoded");
		wstv.addRequestArg(Request_Arg_Type.HEADER,	"Content-Length", String.valueOf(requestBody.length()));
		try {
			wstv.invoke();
			String rsp = wstv.getResponseBody();
			String[] rspHeaders = wstv.getResponseHeaders();
			L.log(Level.FINE, "REST response: {0}", rsp);
			L.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
			Assert.assertTrue(rsp.trim().length() > 0);
			checkResponse(rsp, "&lt;Chapter&gt;3&lt;/Chapter&gt;");
			checkResponse(rsp, "There was a man of the Pharisees, named Nicodemus, a ruler of the Jews");
		} finally {
			wstv.clearRequestArgs(Request_Arg_Type.HEADER);
		}
	}

	@Test
	public void testErrorResponse() {
		WsTesterView wstv = new WsTesterView();
		wstv.show();
		wstv.setRequestType(Request_Type.GET);
		wstv.setServiceURL("http://www.zvents.com/rest/event_update");
		wstv.invoke();
		Assert.assertEquals(0, wstv.getRequestArgs(Request_Arg_Type.PARAMETER).size());
		String rsp = wstv.getResponseBody();
		String[] rspHeaders = wstv.getResponseHeaders();
		L.log(Level.FINE, "REST response: {0}", rsp);
		L.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
		Assert.assertTrue(rsp.trim().length() > 0);
		checkResponse(rsp, "Invalid API Key.");
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
			L.log(Level.WARNING, e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					L.log(Level.FINEST, e.getMessage(), e);
				}
			}
		}
		return sb.toString();
	}

	private void checkResponse(String rsp, String expContent) {
		try {
			Assert.assertTrue(rsp, rsp.contains(expContent));
		} catch (AssertionError t) {
			if (rsp.contains("503")) {
				L.log(Level.WARNING, "Service Unavailable: {0}", SERVICE_URL);
			} else {
				throw t;
			}
		}
	}

	private File prepareWsdl() {
		String[] files = { "imported.wsdl", "original.wsdl", "schema.xsd" };
		File targetFolder = new File(System.getProperty("java.io.tmpdir"),
				"WsTesterTest");
		targetFolder.mkdirs();
		for (String file : files) {
			InputStream is = WsTesterTest.class.getResourceAsStream("/wsdl/"
					+ file);
			File target = new File(targetFolder, file);
			if (target.exists()) {
				target.delete();
			}
			try {
				OutputStream os = new BufferedOutputStream(
						new FileOutputStream(target));
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
					L.log(Level.WARNING, ioe2.getMessage(), ioe2);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe2) {
					L.log(Level.WARNING, ioe2.getMessage(), ioe2);
				}
			}
		}
	}

	private void selectPort(WsTesterView wstv, String portName) {
		SelectWSDLDialog dlg = wstv.getFromWSDL();
		try {
			dlg.openURL();
			SWTBotShell sh = bot.activeShell();
			sh.bot().text().typeText(SERVICE_URL + "?WSDL");
			sh.bot().button("OK").click();
			bot.sleep(1000);
			Assert.assertEquals(SERVICE_URL + "?WSDL", dlg.getURI());
			List<String> items = dlg.getServices();
			L.log(Level.FINE, "Services: {0}", items);
			Assert.assertEquals(1, items.size());
			Assert.assertTrue(items.contains("BibleWebservice"));
			items = dlg.getPorts();
			L.log(Level.FINE, "Ports: {0}", items);
			Assert.assertEquals(2, items.size());
			Assert.assertTrue(items.contains("BibleWebserviceSoap"));
			Assert.assertTrue(items.contains("BibleWebserviceSoap12"));
			dlg.selectPort(portName);
			items = dlg.getOperations();
			L.log(Level.FINE, "Operations: {0}", items);
			Assert.assertEquals(4, items.size());
			Assert.assertTrue(items.contains("GetBookTitles"));
			Assert.assertTrue(items.contains("GetBibleWordsByChapterAndVerse"));
			dlg.selectOperation("GetBibleWordsbyKeyWord");
			dlg.ok();
			Assert.assertEquals("http://www.webservicex.net/BibleWebservice.asmx", wstv.getServiceURL());
		} finally {
			if (dlg.isOpen()) {
				dlg.close();
			}
		}	}
}
