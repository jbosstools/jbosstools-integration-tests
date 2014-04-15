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

public class CreateDeleteScalableRubyApp {

	private final String RUBY_APP_NAME = TestProperties
			.get("openshift.rubyapp.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils
				.cleanupGitFolder(TestProperties.get("openshift.rubyapp.name"));
	}

	@Test
	public void canCreateScalableRubyApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.RUBY_1_9, RUBY_APP_NAME, true, true, true);
	}

	@After
	public void canDeleteScalableRubyApp() {
		new DeleteApplication(RUBY_APP_NAME, OpenShiftLabel.AppType.RUBY_1_9_TREE).perform();
	}

}
