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
public class CreateDeletePerlApp extends OpenShiftBotTest {
	private final String PERL_APP_NAME = TestProperties
			.get("openshift.perl.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties.get("openshift.perl.name"));
	}

	@Test
	public void canCreatePerlApp() {
		createOpenShiftApplication(PERL_APP_NAME, OpenShiftLabel.AppType.PERL);
	}

	@After
	public void canDeletePerlApp() {
		deleteOpenShiftApplication(PERL_APP_NAME,
				OpenShiftLabel.AppType.PERL);
	}
}
