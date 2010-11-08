package org.jboss.tools.ws.ui.bot.test;

import org.jboss.tools.ui.bot.ext.config.Annotations.*;
import org.junit.Test;


@SWTBotTestRequires(server=@Server(),perspective="Java EE")
public class BottomUpWebService extends JbossWSTest {

	@Test
	public void bottomUpJbossWS1() {
		int testlevel=4;
		for (int j=0;j<testlevel;j++) {				
			bottomUpJbossWebService(0, j);
		}
	}
	
	@Test
	public void bottomUpJbossWS2() {
		int testlevel=4;
		for (int j=0;j<testlevel;j++) {				
			bottomUpJbossWebService(1, j);
		}
	}
	
	@Test
	public void bottomUpJbossWS3() {
		int testlevel=4;
		for (int j=0;j<testlevel;j++) {				
			bottomUpJbossWebService(2, j);
		}
	}
	
	@Test
	public void bottomUpJbossWS4() {
		int testlevel=4;
		for (int j=0;j<testlevel;j++) {				
			bottomUpJbossWebService(3, j);
		}
	}
	
	protected void bottomUpJbossWebService(int serverType, int clientType) {
		log.info(" * Running test ServiceType: '"+wizardConfigTexts.get(serverType)+"', ClientType: '"+wizardConfigTexts.get(clientType)+"'");
		createEARProject(EAR_PROJECT_NAME);
		createProject(BOTTOMUP_WS_PROJ_NAME);
		bottomUpService(BOTTOMUP_WS_PROJ_NAME, CLASS_A,serverType);
		assertServiceDeployed(BOTTOMUP_WS_WSDL_URL);
		// generate client stubs
		createClient(BOTTOMUP_WS_CLIENT_PROJ_NAME, BOTTOMUP_WS_CLIENT_SERVLET_NAME, BOTTOMUP_WS_WSDL_URL, clientType);		
		servers.removeAllProjectsFromServer(configuredState.getServer().name);
		if (serverType >= SERVICE_SCALE_ASSEMBLE) {
			// service was not assembled into EAR
			projectExplorer.runOnServer(BOTTOMUP_WS_PROJ_NAME);
		} else {
			projectExplorer.runOnServer(EAR_PROJECT_NAME);
		}				
		if (clientType >= CLIENT_SCALE_ASSEMBLE) {
			// client was not assembled into EAR
			projectExplorer.runOnServer(BOTTOMUP_WS_CLIENT_PROJ_NAME);
			
		} else {
			projectExplorer.runOnServer(EAR_PROJECT_NAME);
		}
		// but we need to run any of wars on server (to get browser)
		projectExplorer.runOnServer(BOTTOMUP_WS_PROJ_NAME);
		assertServiceResponseToClient(BOTTOMUP_WS_CLIENT_SERVLET_URL,
				"1234567890");
		servers.removeAllProjectsFromServer(configuredState.getServer().name);
		assertServiceNotDeployed(BOTTOMUP_WS_WSDL_URL);
		servers.removeAllProjectsFromServer();
		projectExplorer.deleteAllProjects();
	}
}
