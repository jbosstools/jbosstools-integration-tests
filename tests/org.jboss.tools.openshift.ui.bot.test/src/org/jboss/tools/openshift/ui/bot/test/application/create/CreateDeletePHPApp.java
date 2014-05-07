package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * Test capabilities of create/delete PHP application on a OpenShift server.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateDeletePHPApp {

	private final String PHP_APP_NAME = "phpapp" + new Date().getTime();
	
	@Test
	public void canCreatePHPApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PHP, PHP_APP_NAME, false, true, true);
	}
	
	@After
	public void canDeletePHPApp() {
		new DeleteApplication(PHP_APP_NAME).perform();
	}
}
