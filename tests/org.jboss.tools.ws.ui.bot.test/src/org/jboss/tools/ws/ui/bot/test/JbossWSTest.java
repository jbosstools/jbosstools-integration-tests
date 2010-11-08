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
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.Class;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.JavaEEEnterpriseApplicationProject;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWebService;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWebServiceClient;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServlet;
import org.jboss.tools.ui.bot.ext.parts.SWTBotHyperlinkExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotScaleExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewFileWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.DynamicWebProjectWizard;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class JbossWSTest extends SWTTestExt {

	public static final String EAR_PROJECT_NAME="EAR";
	public static final String PKG_NAME="jbossws";
		
	public static final String CLASS_A="ClassA";
	public static final String CLASS_B="ClassB";
	public static final String CLASS_C="ClassC";
	public static final String BOTTOMUP_WS_PROJ_NAME="BottomUpJbossWS";	
	public static final String BOTTOMUP_WS_WSDL_URL="http://localhost:8080/"+BOTTOMUP_WS_PROJ_NAME+"/"+CLASS_A+"?wsdl";	
	public static final String BOTTOMUP_WS_CLIENT_PROJ_NAME = "BottomUpJbossWSClient";
	public static final String BOTTOMUP_WS_CLIENT_SERVLET_NAME = "BottomUpJbossWStest";
	public static final String BOTTOMUP_WS_CLIENT_SERVLET_URL = "http://localhost:8080/"+BOTTOMUP_WS_CLIENT_PROJ_NAME+"/"+BOTTOMUP_WS_CLIENT_SERVLET_NAME;
	public static final String TWO_SERVICES_CLIENT_SERVLET_NAME="TwoServicesJbossWStest";
	public static final String TWO_SERVICES_CLIENT_SERVLET_URL = "http://localhost:8080/"+BOTTOMUP_WS_CLIENT_PROJ_NAME+"/"+TWO_SERVICES_CLIENT_SERVLET_NAME;
	public static final String TOPDOWN_WS_PROJ_NAME="TopDownJbossWS";	
	public static final String TOPDOWN_WS_WSDL_URL="http://localhost:8080/"+TOPDOWN_WS_PROJ_NAME+"/"+CLASS_B+"?wsdl";	
	public static final String TOPDOWN_WS_CLIENT_PROJ_NAME = "TopDownJbossWSClient";
	public static final String TOPDOWN_WS_CLIENT_SERVLET_NAME = "TopDownJbossWStest";
	public static final String TOPDOWN_WS_CLIENT_SERVLET_URL = "http://localhost:8080/"+TOPDOWN_WS_CLIENT_PROJ_NAME+"/"+TOPDOWN_WS_CLIENT_SERVLET_NAME;
	public static final String JBOSSWS_CRED_LOGIN="admin";
	public static final String JBOSSWS_CRED_PASS="admin";
	public static final int CLIENT_SCALE_DEVELOP=5;
	public static final int CLIENT_SCALE_ASSEMBLE=4;
	public static final int CLIENT_SCALE_DEPLOY=3;
	public static final int CLIENT_SCALE_INSTALL=2;
	public static final int CLIENT_SCALE_START=1;
	public static final int CLIENT_SCALE_TEST=0;
	public static final int SERVICE_SCALE_DEVELOP=5;
	public static final int SERVICE_SCALE_ASSEMBLE=4;
	public static final int SERVICE_SCALE_DEPLOY=3;
	public static final int SERVICE_SCALE_INSTALL=2;
	public static final int SERVICE_SCALE_START=1;
	public static final int SERVICE_SCALE_TEST=0;
	protected static Map<Integer,String> wizardConfigTexts = new HashMap<Integer, String>();

	private static final String SOAP_REQUEST_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>" +
	"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
	" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
	" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
	"<soap:Body>{0}</soap:Body>" +
	"</soap:Envelope>";;
	
	private static final Logger L = Logger.getLogger(JbossWSTest.class.getName());
	public JbossWSTest() {

	}
	
	static {
		wizardConfigTexts.put(0, "Test");
		wizardConfigTexts.put(1, "Start");
		wizardConfigTexts.put(2, "Install");
		wizardConfigTexts.put(3, "Deploy");
		wizardConfigTexts.put(4, "Assemble");
		wizardConfigTexts.put(5, "Develop");
	}
		
	protected void createClient(String projectName, String servletName,String wsdlDef, int type) {
		createProject(projectName);
		SWTBot wiz = open.newObject(WebServicesWebServiceClient.LABEL);
		wiz.comboBoxWithLabel(
				WebServicesWebServiceClient.TEXT_SERVICE_DEFINITION).setText(
						wsdlDef);
		SWTBotScaleExt slider = bot.scale();	
		slider.setSelection(type);
		selectJbossWSRuntime();
		bot.sleep(TIME_1S); // wait for wizard to validate wsdl url and
		// enable Finish button		
		open.finish(wiz);
		projectExplorer.selectProject(projectName);
		// create servlet which will invoke service
		createInvokingServlet(servletName);
	}

	/**
	 * checks if 'Web Service Runtime' is set to 'JbossWS' and possibly sets it correctly
	 * @param wiz wizard page of new Web Service
	 */
	protected void selectJbossWSRuntime() {
		SWTBotHyperlinkExt link = bot.hyperlink(1); 
		String linkText = link.getText();
		if (!linkText.contains("JBossWS")) {
			link.click();
			SWTBot dBot = bot.activeShell().bot();
			dBot.tree().select("JBossWS");
			open.finish(dBot,IDELabel.Button.OK);
		}
	}
	
	protected void bottomUpService(String projName, String serviceClass, int serverType) {		
		SWTBot wiz = open.newObject(Class.LABEL);
		wiz.textWithLabel(Class.TEXT_PACKAGE).setText(PKG_NAME);
		wiz.textWithLabel(Class.TEXT_NAME).setText(serviceClass);
		open.finish(wiz);
		eclipse.setClassContentFromResource(bot.editorByTitle(serviceClass
				+ ".java"), true,
				org.jboss.tools.ws.ui.bot.test.Activator.PLUGIN_ID,
				PKG_NAME, serviceClass + ".java.ws");
		wiz = open.newObject(ActionItem.NewObject.WebServicesWebService.LABEL);
		wiz.textWithLabel(WebServicesWebService.TEXT_SERVICE_IMPLEMENTATION)
				.setText(PKG_NAME + "." + serviceClass);
		SWTBotScaleExt slider = bot.scale();	
		slider.setSelection(serverType);
		selectJbossWSRuntime();
		open.finish(wiz);
		projectExplorer.runOnServer(projName);
	}

	protected void createInvokingServlet(String servletName) {
		SWTBot wiz = open.newObject(WebServlet.LABEL);
		wiz.textWithLabel(WebServlet.TEXT_JAVA_PACKAGE).setText(
				PKG_NAME);
		wiz.textWithLabel(WebServlet.TEXT_CLASS_NAME).setText(
				servletName);
		open.finish(wiz);
		eclipse.setClassContentFromResource(bot
				.editorByTitle(servletName
						+ ".java"), true,
				org.jboss.tools.ws.ui.bot.test.Activator.PLUGIN_ID,
				PKG_NAME, servletName
						+ ".java.servlet");
	}

	protected void createProject(String name) {
		new NewFileWizardAction().run().selectTemplate("Web", "Dynamic Web Project").next();
		new DynamicWebProjectWizard().setProjectName(name).finish();
		util.waitForNonIgnoredJobs();
		assertTrue(projectExplorer.existsResource(name));
		projectExplorer.selectProject(name);
	}
	
	protected void createEARProject(String name) {
		SWTBot wiz = open.newObject(JavaEEEnterpriseApplicationProject.LABEL);
		wiz.textWithLabel(JavaEEEnterpriseApplicationProject.TEXT_PROJECT_NAME).setText(name);
		// set EAR version
		SWTBotCombo combo = wiz.comboBox(1);
		combo.setSelection(combo.itemCount()-1);
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.checkBox("Generate application.xml deployment descriptor").click();
		open.finish(wiz);
		assertTrue(projectExplorer.existsResource(name));
		projectExplorer.selectProject(name);
	}

	protected void assertServiceDeployed(String wsdlURL) {
		HttpURLConnection connection = null;
		try {
			URL u = new URL(wsdlURL);
			connection = (HttpURLConnection) u.openConnection();
			assertEquals("Service was not sucessfully deployed, WSDL '" + wsdlURL + "' was not found",
					HttpURLConnection.HTTP_OK, connection.getResponseCode());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	protected void assertServiceNotDeployed(String wsdlURL) {
		HttpURLConnection connection = null;
		try {
			URL u = new URL(wsdlURL);
			connection = (HttpURLConnection) u.openConnection();
			assertEquals("Project was not sucessfully undeployed, WSDL '" + wsdlURL + "' is still available",
					HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	protected void assertServiceResponseToClient(String startServlet, String response) {
		InputStream is = null;
		try {
			URL u = new URL(startServlet);
			is = u.openStream();
			String rsp = readStream(is);
			assertContains(response, rsp);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					//ignore
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getSoapRequest(String body) {
		return MessageFormat.format(SOAP_REQUEST_TEMPLATE, body);
	}
	
	protected String readStream(InputStream is) {
		Reader r = null;
		Writer w = null;
		try {
			char[] buffer = new char[1024];
			r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			w = new StringWriter();
			int n;
			while ((n = r.read(buffer)) != -1) {
				w.write(buffer, 0, n);
			}
		} catch (IOException e) {
			L.log(Level.WARNING, e.getMessage(), e);
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					//ignore
					L.log(Level.WARNING, e.getMessage(), e);
				}
			}
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					//ignore
					L.log(Level.WARNING, e.getMessage(), e);
				}
			}
		}
		return w != null ? w.toString() : "";
	}
}
