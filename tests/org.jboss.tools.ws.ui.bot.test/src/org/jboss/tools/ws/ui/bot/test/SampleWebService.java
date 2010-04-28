package org.jboss.tools.ws.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.JBossToolsCreateaSampleWebService;
import org.junit.Test;


@SWTBotTestRequires(server=@Server(),perspective="Java EE")
public class SampleWebService extends JbossWSTest {


	@Test
	public void sampleWebService() {
		console.show().toolbarToggleButton("Show Console When Standard Out Changes").deselect();
		console.show().toolbarToggleButton("Show Console When Standard Error Changes").deselect();
		int testlevel = 3;
		for (int i=0;i<testlevel;i++) {			
			sampleWebService(i);			
		}
	}
	
	private void sampleWebService(int clientType) {
		log.info(" * Running test with ClientType: '"+wizardConfigTexts.get(clientType)+"'");
		createEARProject(EAR_PROJECT_NAME);
		createProject(SAMPLE_WS_PROJ_NAME);
		SWTBot wiz = open.newObject(JBossToolsCreateaSampleWebService.LABEL);
		wiz.comboBox().setText(SAMPLE_WS_PROJ_NAME);
		open.finish(wiz);
		open.viewOpen(ActionItem.View.ServerServers.LABEL);
		projectExplorer.runOnServer(SAMPLE_WS_PROJ_NAME);
		// browse WSDL
		assertServiceDeployed(SAMPLE_WS_WSDL_URL);		
		// create and run WS Sample Client
		createClient(SAMPLE_WSCLIENT_PROJ_NAME, SAMPLE_WSCLIENT_SERVLET_NAME, SAMPLE_WS_WSDL_URL, clientType);
		// re run project
		if (clientType<=CLIENT_SCALE_DEPLOY) {
			servers.removeProjectFromServers(EAR_PROJECT_NAME);
		}
		projectExplorer.runOnServer(SAMPLE_WSCLIENT_PROJ_NAME);
		assertServiceResponseToClient(SAMPLE_WSCLIENT_SERVLET_URL, "abcdefg");
		servers.removeAllProjectsFromServer(configuredState.getServer().name);
		assertServiceNotDeployed(SAMPLE_WS_WSDL_URL);
		servers.removeAllProjectsFromServer();
		projectExplorer.deleteAllProjects();
	}
}
