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
public class CreateDeleteEWSApp {
	
	private final String EWS_APP_NAME = "ewsapp" + new Date().getTime();

	@Test
	public void canCreateEWSApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JBOSS_EWS, EWS_APP_NAME, false, true, true);
	}

	@After
	public void canDeleteEWSApp() {
		new DeleteApplication(EWS_APP_NAME).perform();
	}
}
