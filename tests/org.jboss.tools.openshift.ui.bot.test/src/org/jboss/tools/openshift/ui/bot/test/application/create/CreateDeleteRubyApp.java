package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * Test capabilities of create/delete Ruby application on a OpenShift server.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateDeleteRubyApp {

	private final String RUBY_APP_NAME = "rubyapp" + new Date().getTime();

	@Test
	public void canCreateRubyApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.RUBY_1_9, RUBY_APP_NAME, false, true, true);
	}

	@After
	public void canDeleteRubyApp() {
		new DeleteApplication(RUBY_APP_NAME).perform();
	}

}
