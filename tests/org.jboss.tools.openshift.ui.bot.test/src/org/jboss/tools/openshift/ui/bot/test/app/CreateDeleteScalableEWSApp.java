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
public class CreateDeleteScalableEWSApp extends OpenShiftBotTest {
	private final String EWS_APP_NAME = TestProperties
			.get("openshift.ewsapp.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties.get("openshift.ewsapp.name"));
	}

	@Test
	public void canCreateScalableEWSApp() {
		createScaledOpenShiftApplication(EWS_APP_NAME, OpenShiftLabel.AppType.JBOSS_EWS);
	}

	@After
	public void canDeleteScalableEWSApp() {
		deleteOpenShiftApplication(EWS_APP_NAME,
				OpenShiftLabel.AppType.JBOSS_EWS);
	}
}
