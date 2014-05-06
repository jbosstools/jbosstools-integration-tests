package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateDeleteScalableEWSApp {

	private final String EWS_APP_NAME = "sewsapp" + new Date().getTime();

	@Test
	public void canCreateScalableEWSApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JBOSS_EWS, EWS_APP_NAME, true, true, true);
	}

	@After
	public void canDeleteScalableEWSApp() {
		new DeleteApplication(EWS_APP_NAME).perform();
	}
}
