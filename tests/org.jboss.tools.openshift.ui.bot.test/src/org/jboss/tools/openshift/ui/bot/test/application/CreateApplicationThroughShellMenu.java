package org.jboss.tools.openshift.ui.bot.test.application;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * Test try to create a new application through a menu bar on a workbench shell.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateApplicationThroughShellMenu {

	private String APP_NAME = "diy" + System.currentTimeMillis();
	
	@Test
	public void createApplicationUsingOpenShiftWizard() {
		new NewApplicationTemplates(true).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, APP_NAME, false, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(APP_NAME).perform();
	}
}

