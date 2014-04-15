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

public class CreateDeletePerlApp {
	private final String PERL_APP_NAME = TestProperties
			.get("openshift.perl.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties.get("openshift.perl.name"));
	}

	@Test
	public void canCreatePerlApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PERL, PERL_APP_NAME, false, true, true);
	}

	@After
	public void canDeletePerlApp() {
		new DeleteApplication(PERL_APP_NAME, OpenShiftLabel.AppType.PERL_TREE).perform();
	}
}
