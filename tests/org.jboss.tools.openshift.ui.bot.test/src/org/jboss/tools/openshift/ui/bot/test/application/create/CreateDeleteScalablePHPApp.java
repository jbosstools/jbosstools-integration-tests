package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.openshift.ui.bot.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateDeleteScalablePHPApp {

	private final String PHP_APP_NAME = TestProperties
			.get("openshift.phpapp.name") + new Date().getTime();
	
	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties
				.get("openshift.phpapp.name"));
	}
	
	@Test
	public void canCreateScalablePHPApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PHP, PHP_APP_NAME, true, true, true);
	}
	
	@After
	public void canDeleteScalablePHPApp() {
		new DeleteApplication(PHP_APP_NAME, OpenShiftLabel.AppType.PHP_TREE).perform();
	}
}
