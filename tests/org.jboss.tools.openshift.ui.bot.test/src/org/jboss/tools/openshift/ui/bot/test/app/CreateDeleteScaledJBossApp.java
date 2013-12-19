package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Date;

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.openshift.ui.bot.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@CleanWorkspace
public class CreateDeleteScaledJBossApp extends OpenShiftBotTest {

	private final String JBOSS_APP_NAME = TestProperties
			.get("openshift.jbossapp.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties
				.get("openshift.jbossapp.name"));
	}

	@Test
	public void canCreateScalableJBossApp() {
		createScaledOpenShiftApplication(JBOSS_APP_NAME,
				OpenShiftLabel.AppType.JBOSS_EAP);
	}

	@After
	public void canDeleteScalableJBossApp() {
		deleteOpenShiftApplication(JBOSS_APP_NAME,
				OpenShiftLabel.AppType.JBOSS_EAP);
	}
}
