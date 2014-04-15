package org.jboss.tools.openshift.ui.bot.test.application;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

public class CreateApplicationThroughShellMenu {

	public static final String APP_NAME = "diy" + System.currentTimeMillis();
	
	@Test
	public void createApplicationUsingOpenShiftWizard() {
		new NewApplicationTemplates(true).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, APP_NAME, false, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(APP_NAME, OpenShiftLabel.AppType.DIY_TREE).perform();
	}
}

