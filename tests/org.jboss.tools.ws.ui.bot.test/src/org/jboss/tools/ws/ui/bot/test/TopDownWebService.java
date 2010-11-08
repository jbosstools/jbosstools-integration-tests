package org.jboss.tools.ws.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.config.Annotations.*;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWSDL;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWebService;
import org.jboss.tools.ui.bot.ext.parts.SWTBotScaleExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@SWTBotTestRequires(server=@Server(),perspective="Java EE")
public class TopDownWebService extends JbossWSTest {

	@Test
	public void topDownJbossWS1() {
		int testLevel = 3;
		for (int j=0;j<testLevel;j++) {
			topDownJbossWebService(0, j);
		}
	}

	@Test
	public void topDownJbossWS2() {
		int testLevel = 3;
		for (int j=0;j<testLevel;j++) {
			topDownJbossWebService(1, j);
		}
	}

	@Test
	public void topDownJbossWS3() {
		int testLevel = 3;
		for (int j=0;j<testLevel;j++) {
			topDownJbossWebService(2, j);
		}
	}

	private void topDownJbossWebService(int serverType, int clientType) {
		log.info(" * Running test ServiceType: '"+wizardConfigTexts.get(serverType)+"', ClientType: '"+wizardConfigTexts.get(clientType)+"'");
		createEARProject(EAR_PROJECT_NAME);
		createProject(TOPDOWN_WS_PROJ_NAME);
		SWTBot wiz = open.newObject(WebServicesWSDL.LABEL);
		wiz.textWithLabel(WebServicesWSDL.TEXT_FILE_NAME).setText(
				CLASS_B + ".wsdl");
		wiz.textWithLabel(
				WebServicesWSDL.TEXT_ENTER_OR_SELECT_THE_PARENT_FOLDER)
				.setText(TOPDOWN_WS_PROJ_NAME + "/src");
		wiz.button(IDELabel.Button.NEXT).click();
		open.finish(wiz);
		eclipse.setClassContentFromResource(bot.editorByTitle(CLASS_B
				+ ".wsdl"), true,
				org.jboss.tools.ws.ui.bot.test.Activator.PLUGIN_ID,
				PKG_NAME, CLASS_B + ".wsdl");
		wiz = open.newObject(WebServicesWebService.LABEL);
		wiz.comboBoxWithLabel(WebServicesWebService.TEXT_WEB_SERVICE_TYPE)
				.setSelection(1);
		wiz.comboBoxWithLabel("Service definition:").setText(
				"/" + TOPDOWN_WS_PROJ_NAME + "/src/"
						+ CLASS_B + ".wsdl");
		SWTBotScaleExt slider = bot.scale();	
		slider.setSelection(serverType);
		selectJbossWSRuntime();
		open.finish(wiz);
		servers.removeProjectFromServers(EAR_PROJECT_NAME);
		projectExplorer.runOnServer(TOPDOWN_WS_PROJ_NAME);
		assertServiceDeployed(TOPDOWN_WS_WSDL_URL);
		// create and run client

		createClient(TOPDOWN_WS_CLIENT_PROJ_NAME, TOPDOWN_WS_CLIENT_SERVLET_NAME, TOPDOWN_WS_WSDL_URL, clientType);

		servers.removeProjectFromServers(EAR_PROJECT_NAME);
		projectExplorer.runOnServer(TOPDOWN_WS_PROJ_NAME);
		projectExplorer.runOnServer(TOPDOWN_WS_CLIENT_PROJ_NAME);

		assertServiceResponseToClient(TOPDOWN_WS_CLIENT_SERVLET_URL, "0");
		servers.removeAllProjectsFromServer(configuredState.getServer().name);
		assertServiceNotDeployed(TOPDOWN_WS_WSDL_URL);
		servers.removeAllProjectsFromServer();
		projectExplorer.deleteAllProjects();
	}
}
