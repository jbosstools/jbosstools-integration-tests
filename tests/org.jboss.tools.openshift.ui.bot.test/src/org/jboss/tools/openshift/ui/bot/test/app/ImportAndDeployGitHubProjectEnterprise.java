package org.jboss.tools.openshift.ui.bot.test.app;

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@CleanWorkspace
public class ImportAndDeployGitHubProjectEnterprise {

	@BeforeClass
	public static void importApplication() {
		ImportAndDeployGitHubProject.importApplication();
	}
	
	@Test
	public void testDeployGitApp() {
		ImportAndDeployGitHubProject.deployGitApp(OpenShiftLabel.AppType.JBOSS_EAP);
	}

	@AfterClass
	public static void deleteApplication() {
		OpenShiftBotTest.deleteOpenShiftApplication("jbosseapapp", OpenShiftLabel.AppType.JBOSS_EAP);
	}
	
}
