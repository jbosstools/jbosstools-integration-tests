package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * Test capabilities of create/delete Perl application on a OpenShift server.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateDeletePerlApp {
	private final String PERL_APP_NAME = "perlapp" + new Date().getTime();

	@Test
	public void canCreatePerlApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PERL, PERL_APP_NAME, false, true, true);
	}

	@After
	public void canDeletePerlApp() {
		new DeleteApplication(PERL_APP_NAME).perform();
	}
}
