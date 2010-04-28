package org.jboss.tools.ws.ui.bot.test;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.Class;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.JavaEEEnterpriseApplicationProject;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebDynamicWebProject;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWebService;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWebServiceClient;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServlet;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotHyperlinkExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotScaleExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class JbossWSTest extends SWTTestExt {

	public static final String EAR_PROJECT_NAME="EAR";
	public static final String PKG_NAME="jbossws";
		
	public static final String SAMPLE_WS_PROJ_NAME="SampleWS";
	public static final String SAMPLE_WS_SERVICE_NAME="HelloWorld";
	public static final String SAMPLE_WS_WSDL_URL="http://localhost:8080/"+SAMPLE_WS_PROJ_NAME+"/"+SAMPLE_WS_SERVICE_NAME+"?wsdl";
	public static final String SAMPLE_WSCLIENT_PROJ_NAME = "SampleWSClient";
	public static final String SAMPLE_WSCLIENT_SERVLET_NAME = "SampleWStest";
	public static final String SAMPLE_WSCLIENT_SERVLET_URL = "http://localhost:8080/"+SAMPLE_WSCLIENT_PROJ_NAME+"/"+SAMPLE_WSCLIENT_SERVLET_NAME;
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
		// create servlet which will inovke service
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
		SWTBot wiz = open.newObject(WebDynamicWebProject.LABEL);
		wiz.textWithLabel(WebDynamicWebProject.TEXT_PROJECT_NAME).setText(name);
		open.finish(wiz);
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
		SWTBotBrowserExt b = bot.browser();
		b.goURL("http://localhost:8080/jbossws/services");
		util.waitForBrowserLoadsPage(b,JBOSSWS_CRED_LOGIN,JBOSSWS_CRED_PASS);
		boolean wsdlOK = b.followLink(wsdlURL);
		assertTrue(
				"Service was not sucessfully deployed, WSDL '"+wsdlURL+"' is not listed in JbossWS endpoint registry",
				wsdlOK);
		util.waitForBrowserLoadsPage(b);
		b.back();
		util.waitForBrowserLoadsPage(b);
	}
	
	protected void assertServiceNotDeployed(String wsdlURL) {
		SWTBotBrowserExt b = bot.browser();
		b.goURL("http://localhost:8080/jbossws/services");
		util.waitForBrowserLoadsPage(b,JBOSSWS_CRED_LOGIN,JBOSSWS_CRED_PASS);
		boolean wsdlOK = !b.getText().contains(wsdlURL);
		assertTrue(
				"Project was not sucessfully undeployed, WSDL '"+wsdlURL+"' is listed in JbossWS endpoint registry",
				wsdlOK);
	}

	protected void assertServiceResponseToClient(String startServlet, String response) {
		SWTBotBrowserExt b = bot.browser();
		b.goURL(startServlet);
		util.waitForBrowserLoadsPage(b);
		String servletReturned = b.getText();
		boolean servletRetOK = servletReturned.contains(response);
		assertTrue(
				"Unexpected Client servlet response, error calling web service, response is : "
						+ servletReturned, servletRetOK);

	}
}
