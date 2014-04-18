package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

public class CreateDeleteScalableRubyApp {

	private final String RUBY_APP_NAME = "srubyapp" + new Date().getTime();

	@Test
	public void canCreateScalableRubyApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.RUBY_1_9, RUBY_APP_NAME, true, true, true);
	}

	@After
	public void canDeleteScalableRubyApp() {
		new DeleteApplication(RUBY_APP_NAME).perform();
	}

}
