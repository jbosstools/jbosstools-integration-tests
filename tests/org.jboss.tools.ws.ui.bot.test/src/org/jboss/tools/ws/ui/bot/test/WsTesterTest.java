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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ws.ui.bot.test.widgets.SelectWSDLDialog;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView;
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
        Assert.assertFalse(wstv.isHttpMethodSelectionEnabled());
        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_RS);
        Assert.assertTrue(wstv.isHttpMethodSelectionEnabled());
        wstv.setHttpMethod(WsTesterView.Http_Method.PUT);
        Assert.assertEquals(WsTesterView.Http_Method.PUT, wstv.getHttpMethod());
        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_WS);
        Assert.assertFalse(wstv.isHttpMethodSelectionEnabled());
        Assert.assertEquals(WsTesterView.Http_Method.POST, wstv.getHttpMethod());

        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_RS);
        Assert.assertTrue(wstv.isHttpMethodSelectionEnabled());
        wstv.setHttpMethod(WsTesterView.Http_Method.DELETE);
        Assert.assertEquals(WsTesterView.Http_Method.DELETE, wstv.getHttpMethod());
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.HEADER, "a", "1");
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.HEADER, "b", "2");
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.HEADER, "c", "3");
        Assert.assertEquals(3, wstv.getRequestArgs(WsTesterView.Request_Arg_Type.HEADER).keySet().size());
        Assert.assertTrue(wstv.getRequestArgs(WsTesterView.Request_Arg_Type.HEADER).containsKey("b"));
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.HEADER, "d", "4");
        Assert.assertEquals(4, wstv.getRequestArgs(WsTesterView.Request_Arg_Type.HEADER).keySet().size());
        Assert.assertTrue(wstv.getRequestArgs(WsTesterView.Request_Arg_Type.HEADER).containsKey("d"));
        wstv.upRequestArg(WsTesterView.Request_Arg_Type.HEADER, "c", "3");
        wstv.downRequestArg(WsTesterView.Request_Arg_Type.HEADER, "b", "2");
        Assert.assertEquals(4, wstv.getRequestArgs(WsTesterView.Request_Arg_Type.HEADER).keySet().size());
        wstv.removeRequestArg(WsTesterView.Request_Arg_Type.HEADER, "a", "1");
        wstv.removeRequestArg(WsTesterView.Request_Arg_Type.HEADER, "c", "3");
        Assert.assertEquals(2, wstv.getRequestArgs(WsTesterView.Request_Arg_Type.HEADER).keySet().size());
        wstv.clearRequestArgs(WsTesterView.Request_Arg_Type.HEADER);
        Assert.assertEquals(0, wstv.getRequestArgs(WsTesterView.Request_Arg_Type.HEADER).keySet().size());
        
        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_WS);
        SelectWSDLDialog dlg = wstv.getFromWSDL();
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
        items = dlg.getOperations();
        L.log(Level.FINE, "Operations: {0}", items);
        Assert.assertEquals(4, items.size());
        Assert.assertTrue(items.contains("GetBookTitles"));
        Assert.assertTrue(items.contains("GetBibleWordsByChapterAndVerse"));
        dlg.selectOperation("GetBibleWordsbyKeyWord");
        dlg.ok();
        Assert.assertEquals("http://www.webservicex.net/BibleWebservice.asmx", wstv.getServiceURL());
        Assert.assertEquals("http://www.webserviceX.NET/GetBibleWordsbyKeyWord", wstv.getActionURL());
        viewBot.close();
    }

    /**
     * Test SOAP service invocation
     */
    @Test
    public void testSOAPService() {
        WsTesterView wstv = new WsTesterView();
        wstv.show();
        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_WS);
        Assert.assertFalse(wstv.isHttpMethodSelectionEnabled());
        Assert.assertEquals(WsTesterView.Http_Method.POST, wstv.getHttpMethod());
        String actionUrl = "http://www.webserviceX.NET/GetBibleWordsByChapterAndVerse";
        wstv.setServiceURL(SERVICE_URL);
        wstv.setActionURL(actionUrl);
        InputStream is = WsTesterTest.class.getResourceAsStream("/resources/jbossws/message_soap_out.xml");
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
        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_RS);
        wstv.setHttpMethod(WsTesterView.Http_Method.GET);
        wstv.setServiceURL(SERVICE_URL + "/GetBibleWordsByChapterAndVerse");
        wstv.clearRequestArgs(WsTesterView.Request_Arg_Type.PARAMETER);
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.PARAMETER, "BookTitle", "Luke");
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.PARAMETER, "chapter", "2");
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.PARAMETER, "Verse", "2");
        wstv.editRequestArg(WsTesterView.Request_Arg_Type.PARAMETER, "chapter", "2", "chapter", "1");
        wstv.invoke();
        String rsp = wstv.getResponseBody();
        String[] rspHeaders = wstv.getResponseHeaders();
        L.log(Level.FINE, "REST response: {0}", rsp);
        L.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
        Assert.assertTrue(rsp.trim().length() > 0);
        checkResponse(rsp, "&lt;Chapter&gt;1&lt;/Chapter&gt;");
        checkResponse(rsp, "ministers of the word");
    }

    /**
     * Test REST service invocation (POST request)
     */
    @Test
    public void testRESTPOSTService() {
        WsTesterView wstv = new WsTesterView();
        wstv.show();
        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_RS);
        wstv.setHttpMethod(WsTesterView.Http_Method.POST);
        wstv.setServiceURL(SERVICE_URL + "/GetBibleWordsByChapterAndVerse");
        String requestBody = "BookTitle=John&chapter=3&Verse=1\r";
        wstv.setRequestBody(requestBody);
        wstv.clearRequestArgs(WsTesterView.Request_Arg_Type.PARAMETER);
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.HEADER, "Content-Type", "application/x-www-form-urlencoded");
        wstv.addRequestArg(WsTesterView.Request_Arg_Type.HEADER, "Content-Length", String.valueOf(requestBody.length()));
        wstv.invoke();
        String rsp = wstv.getResponseBody();
        String[] rspHeaders = wstv.getResponseHeaders();
        L.log(Level.FINE, "REST response: {0}", rsp);
        L.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
        Assert.assertTrue(rsp.trim().length() > 0);
        checkResponse(rsp, "&lt;Chapter&gt;3&lt;/Chapter&gt;");
        checkResponse(rsp, "There was a man of the Pharisees, named Nicodemus, a ruler of the Jews");
    }

//  @Test
//  @Ignore
//  public void testSOAPSample() {
//      WsTesterView wstv = new WsTesterView();
//      wstv.show();
//      wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_WS);
//      wstv.setSampleContent();
//      wstv.invoke();
//      String rsp = wstv.getResponseBody();
//      L.log(Level.FINE, "SOAP Sample response: {0}", rsp);
//      checkResponse(rsp, "To be, or not to be: that is the question");
//  }
//
//  @Test
//  @Ignore
//  public void testRESTSample() {
//      WsTesterView wstv = new WsTesterView();
//      SWTBotView viewBot = wstv.show();
//      wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_RS);
//      wstv.setSampleContent();
//      wstv.invoke();
//      String rsp = wstv.getResponseBody();
//      String[] rspHeaders = wstv.getResponseHeaders();
//      L.log(Level.FINE, "REST Sample response: {0}", rsp);
//      L.log(Level.FINE, "Response headers: {0}", Arrays.asList(rspHeaders));
//      checkResponse(rsp, "Colorado Springs");
//      viewBot.close();
//  }

    @Test
    public void testOpenMonitor() {
        WsTesterView wstv = new WsTesterView();
        SWTBotView tv = wstv.show();
        wstv.openMonitor();
        SWTBotView av = bot.activeView();
        Assert.assertEquals("TCP/IP Monitor", av.getTitle());
        av.close();
        tv.close();
    }

    @Test
    public void testConfigureMonitor() {
        WsTesterView wstv = new WsTesterView();
        SWTBotView tv = wstv.show();
        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_WS);
        Assert.assertFalse(wstv.isHttpMethodSelectionEnabled());
        Assert.assertEquals(WsTesterView.Http_Method.POST, wstv.getHttpMethod());
        String actionUrl = "http://www.webserviceX.NET/GetBibleWordsByChapterAndVerse";
        wstv.setServiceURL(SERVICE_URL);
        wstv.setActionURL(actionUrl);
        InputStream is = WsTesterTest.class.getResourceAsStream("/resources/jbossws/message_soap_out.xml");
        wstv.setRequestBody(readResource(is));
        wstv.configureMonitor();
        SWTBotShell sh = bot.activeShell();
        Assert.assertEquals("New Monitor", sh.getText());
        sh.bot().spinner("80", 0).setSelection(8077);
        sh.bot().button("OK").click();
        tv.show();
        wstv.invoke();
        tv.show();
        String rsp = wstv.getResponseBody();
        L.log(Level.FINE, "SOAP response: {0}", rsp);
        Assert.assertTrue(rsp.trim().length() > 0);
        checkResponse(rsp, "&lt;BookTitle&gt;Mark&lt;/BookTitle&gt;");
    }

    @Test
    public void testErrorResponse() {
        WsTesterView wstv = new WsTesterView();
        wstv.show();
        wstv.setWebServiceType(WsTesterView.Ws_Type.JAX_RS);
        wstv.setHttpMethod(WsTesterView.Http_Method.GET);
        wstv.setServiceURL("http://www.zvents.com/rest/event_update");
        wstv.invoke();
        Assert.assertEquals(0, wstv.getRequestArgs(WsTesterView.Request_Arg_Type.PARAMETER).size());
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
}
