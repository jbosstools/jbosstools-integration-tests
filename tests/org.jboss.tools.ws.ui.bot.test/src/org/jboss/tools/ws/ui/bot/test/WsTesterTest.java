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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTabItem;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.view.ViewBase;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;
import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * Tests for Web Service Tester
 *
 * @author jlukas
 */
@SuppressWarnings("restriction")
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

    private static class WsTesterView extends ViewBase {

        private static final Bundle WSUI_BUNDLE = Platform.getBundle("org.jboss.tools.ws.ui");

        enum Ws_Type {

            JAX_WS, JAX_RS;

            String getLabel() {
                switch (this) {
                    case JAX_WS:
                        return "JAX-WS";
                    case JAX_RS:
                        return "JAX-RS";
                }
                throw new AssertionError("Invalid Web Service Type");
            }
        }

        enum Http_Method {

            GET, POST, PUT, DELETE;
        }

        enum Request_Arg_Type {

            HEADER(JBossWSUIMessages.JAXRSWSTestView_Request_Header_Label),
            PARAMETER(JBossWSUIMessages.JAXRSWSTestView_Request_Parameters_Label);
            private String type;

            private Request_Arg_Type(String s) {
                type = s;
            }

            private String getType() {
                return type;
            }
        }

        public WsTesterView() {
            super();
            viewObject = new IView() {

                public String getName() {
                    // Web Service Tester
                    return getStringFromBundle("%test.view.name");
                }

                public List<String> getGroupPath() {
                    // JBoss Tools Web Services
                    return Collections.singletonList(getStringFromBundle("%test.view.category.name"));
                }
            };
        }

        @Override
        public SWTBotView show() {
            SWTBotView b = super.show();
            // maximize tester view
            menu(IDEWorkbenchMessages.Workbench_window).menu(WorkbenchMessages.MaximizePartAction_text).click();
            return b;
        }

        public void setWebServiceType(Ws_Type type) {
            comboBox(0).setSelection(type.getLabel());
        }

//      public void setSampleContent() {
//          button(JBossWSUIMessages.JAXRSWSTestView_Set_Sample_Data_Label).click();
//      }
        public void setHttpMethod(Http_Method m) {
            comboBox(1).setSelection(m.toString());
        }

        public Http_Method getHttpMethod() {
            return Enum.valueOf(Http_Method.class, comboBox(1).getText());
        }

        public boolean isHttpMethodSelectionEnabled() {
            return comboBox(1).isEnabled();
        }

        public void setServiceURL(String url) {
            comboBox(2).typeText(url);
        }

        public void setActionURL(String s) {
            text(0).typeText(s);
        }

        public void setRequestBody(String s) {
            SWTBotTabItem ti = tabItem(JBossWSUIMessages.JAXRSWSTestView_Request_Body_Label);
            ti.activate();
            text(1).setText(s);
        }

        public void addRequestArg(Request_Arg_Type type, String name, String value) {
            SWTBotTabItem ti = bot.tabItem(type.getType());
            ti.activate();
            text(1).typeText(name + "=" + value);
            bot.button("Add").click();
            text(1).setText("");
        }

        public Map<String, String> getRequestArgs(Request_Arg_Type type) {
            SWTBotTabItem ti = bot.tabItem(type.getType());
            ti.activate();
            String[] args = list(0).getItems();
            Map<String, String> result = new HashMap<String, String>();
            for (String s : args) {
                int i = s.indexOf('=');
                result.put(s.substring(0, i), s.substring(i + 1));
            }
            return result;
        }

        public void editRequestArg(Request_Arg_Type type, String oldName,
                String oldValue, String newName, String newValue) {
            SWTBotTabItem ti = bot.tabItem(type.getType());
            ti.activate();
            bot.list(0).select(oldName + "=" + oldValue);
            bot.button("Edit").click();
            SWTBot sh = bot.activeShell().bot();
            sh.text(0).typeText(newName + "=" + newValue);
            sh.button("OK").click();
        }

        public void upRequestArg(Request_Arg_Type type, String name, String value) {
            SWTBotTabItem ti = bot.tabItem(type.getType());
            ti.activate();
            list(0).select(name + "=" + value);
            bot.button("Up").click();
        }

        public void downRequestArg(Request_Arg_Type type, String name, String value) {
            SWTBotTabItem ti = bot.tabItem(type.getType());
            ti.activate();
            list(0).select(name + "=" + value);
            bot.button("Down").click();
        }

        public void removeRequestArg(Request_Arg_Type type, String name, String value) {
            SWTBotTabItem ti = bot.tabItem(type.getType());
            ti.activate();
            list(0).select(name + "=" + value);
            bot.button("Remove").click();
        }

        public void clearRequestArgs(Request_Arg_Type type) {
            SWTBotTabItem ti = bot.tabItem(type.getType());
            ti.activate();
            SWTBotButton b = bot.button("Clear All");
            if (b.isEnabled()) {
                b.click();
            }
        }

        public String getResponseBody() {
            SWTBotTabItem ti = tabItem(JBossWSUIMessages.JAXRSWSTestView_Results_Body_Label);
            ti.activate();
            return bot.text(2).getText();
        }

        public String[] getResponseHeaders() {
            SWTBotTabItem ti = tabItem(JBossWSUIMessages.JAXRSWSTestView_Results_Header_Label);
            ti.activate();
            return list(1).getItems();
        }

        public void invoke() {
            String dlgTitle = JBossWSUIMessages.JAXRSWSTestView_Invoke_Label;
            button(dlgTitle).click();
            waitWhile(Conditions.shellIsActive(dlgTitle), 120000);
            sleep(500);
        }

        public void openMonitor() {
            button(JBossWSUIMessages.JAXRSWSTestView_Open_Monitor_Button).click();
        }

        public void configureMonitor() {
            button(JBossWSUIMessages.JAXRSWSTestView_Configure_Monitor_Button).click();
        }

        private String getStringFromBundle(String key) {
            return Platform.getResourceString(WSUI_BUNDLE, key);
        }
    }
}
