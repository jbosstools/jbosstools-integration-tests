package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * Test capabilities of create/delete scalable Perl application on a OpenShift server.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateDeleteScalablePerlApp {
	
	private final String PERL_APP_NAME = "sperlapp" + new Date().getTime();

	@Test
	public void canCreateScalablePerlApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PERL, PERL_APP_NAME, true, true, true);
	}

	@After
	public void canDeleteScalablePerlApp() {
		new DeleteApplication(PERL_APP_NAME).perform();
	}
}
