package org.jboss.tools.openshift.ui.bot.test.app;

import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

public class CreateApplicationFromGithubEnterprise {

	@Test
	public void createAppFromGithubTemplate() {
		CreateApplicationFromGithub.createAppFromGithub(OpenShiftLabel.Cartridge.MYSQL);
	}
	
	@After
	public void deleteApplication() {
		CreateApplicationFromGithub.deleteApp();
	}
	
}
