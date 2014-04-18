package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

public class CreateDeleteScalablePHPApp {

	private final String PHP_APP_NAME = "sphpapp" + new Date().getTime();
	
	@Test
	public void canCreateScalablePHPApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PHP, PHP_APP_NAME, true, true, true);
	}
	
	@After
	public void canDeleteScalablePHPApp() {
		new DeleteApplication(PHP_APP_NAME).perform();
	}
}
