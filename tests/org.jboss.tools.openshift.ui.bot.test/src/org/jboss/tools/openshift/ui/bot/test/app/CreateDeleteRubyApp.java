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
public class CreateDeleteRubyApp extends OpenShiftBotTest {

	private final String RUBY_APP_NAME = TestProperties
			.get("openshift.rubyapp.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils
				.cleanupGitFolder(TestProperties.get("openshift.rubyapp.name"));
	}

	@Test
	public void canCreateScaledRubyApp() {
		createOpenShiftApplication(RUBY_APP_NAME,
				OpenShiftUI.AppType.RUBY_1_9);
	}

	@After
	public void canDeleteRubyApp() {
		deleteOpenShiftApplication(RUBY_APP_NAME,
				OpenShiftUI.AppType.RUBY_1_9);
	}

}
