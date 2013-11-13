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
public class CreateDeleteEWSApp extends OpenShiftBotTest {
	private final String EWS_APP_NAME = TestProperties
			.get("openshift.ewsapp.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties.get("openshift.ewsapp.name"));
	}

	@Test
	public void canCreateEWSApp() {
		createOpenShiftApplication(EWS_APP_NAME, OpenShiftUI.AppType.JBOSS_EWS);
	}

	@After
	public void canDeleteEWSApp() {
		deleteOpenShiftApplication(EWS_APP_NAME,
				OpenShiftUI.AppType.JBOSS_EWS);
	}
}
