package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.openshift.ui.bot.util.TestUtils;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearWorkspace = true)
public class CreateDeletePHPApp extends OpenShiftBotTest {

	private final String PHP_APP_NAME = TestProperties
			.get("openshift.phpapp.name") + new Date().getTime();
	
	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties
				.get("openshift.phpapp.name"));
	}
	
	@Test
	public void canCreatePHPApp() {
		createOpenShiftApplication(PHP_APP_NAME, OpenShiftUI.AppType.PHP);
	}
	
	@After
	public void canDeletePHPApp() {
		deleteOpenShiftApplication(PHP_APP_NAME, OpenShiftUI.AppType.PHP);
	}
}
