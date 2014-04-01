package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.openshift.ui.bot.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateDeleteJBossApp extends OpenShiftBotTest {

	private final String JBOSS_APP_NAME = TestProperties
			.get("openshift.jbossapp.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties
				.get("openshift.jbossapp.name"));
	}

	@Test
	public void canCreateJBossApp() {
		createOpenShiftApplication(JBOSS_APP_NAME,
				OpenShiftLabel.AppType.JBOSS_EAP);
	}

	@After
	public void canDeleteJBossApp() {
		deleteOpenShiftApplication(JBOSS_APP_NAME,
				OpenShiftLabel.AppType.JBOSS_EAP_TREE);
	}
}
