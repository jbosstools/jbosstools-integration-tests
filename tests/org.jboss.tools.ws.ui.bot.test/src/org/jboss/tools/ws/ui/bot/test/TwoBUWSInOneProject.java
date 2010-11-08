package org.jboss.tools.ws.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWebServiceClient;
import org.junit.AfterClass;
import org.junit.Test;

@SWTBotTestRequires(server=@Server(),perspective="Java EE")
public class TwoBUWSInOneProject extends JbossWSTest {
	
	@Test
	public void twoServices() {
		createEARProject(EAR_PROJECT_NAME);
		createProject(BOTTOMUP_WS_PROJ_NAME);
		// service A
		bottomUpService(BOTTOMUP_WS_PROJ_NAME, CLASS_A,SERVICE_SCALE_START);
		// service B
		bottomUpService(BOTTOMUP_WS_PROJ_NAME, CLASS_B,SERVICE_SCALE_START);
		
		assertServiceDeployed(BOTTOMUP_WS_WSDL_URL);
		assertServiceDeployed(BOTTOMUP_WS_WSDL_URL.replace(CLASS_A, CLASS_B));
		
		// generate client stubs
		createClient(BOTTOMUP_WS_CLIENT_PROJ_NAME, TWO_SERVICES_CLIENT_SERVLET_NAME, BOTTOMUP_WS_WSDL_URL, CLIENT_SCALE_START);		
		util.waitForAll(TIME_5S);
		// service B client
		SWTBot wiz = open.newObject(WebServicesWebServiceClient.LABEL);
		wiz.comboBoxWithLabel(
				WebServicesWebServiceClient.TEXT_SERVICE_DEFINITION).setText(
						BOTTOMUP_WS_WSDL_URL.replace(CLASS_A, CLASS_B));
		bot.sleep(TIME_1S);// wait for wizard to validate wsdl url and enable 'Finish' button
		selectJbossWSRuntime();
		open.finish(wiz);
		util.waitForAll(TIME_5S);
		// create servlet which will inovke service
		createInvokingServlet(TWO_SERVICES_CLIENT_SERVLET_NAME);
	
		// we need to undeploy auto-created EAR and deploy service and client
		// wars again
		servers.removeAllProjectsFromServer(configuredState.getServer().name);		
		projectExplorer.runOnServer(BOTTOMUP_WS_PROJ_NAME);
		projectExplorer.runOnServer(BOTTOMUP_WS_CLIENT_PROJ_NAME);
		assertServiceResponseToClient(TWO_SERVICES_CLIENT_SERVLET_URL,
				"1234567890");
		assertServiceResponseToClient(TWO_SERVICES_CLIENT_SERVLET_URL,
		"11111");
		servers.removeAllProjectsFromServer(configuredState.getServer().name);
		assertServiceNotDeployed(BOTTOMUP_WS_WSDL_URL);
		
	}
}
