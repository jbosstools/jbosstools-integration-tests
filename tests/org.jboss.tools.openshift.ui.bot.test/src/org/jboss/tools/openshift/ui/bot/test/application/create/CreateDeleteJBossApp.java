package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

public class CreateDeleteJBossApp {

	private final String JBOSS_APP_NAME = "eapapp" + new Date().getTime();

	@Test
	public void canCreateJBossApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JBOSS_EAP, JBOSS_APP_NAME, false, true, true);
	}

	@After
	public void canDeleteJBossApp() {
		new DeleteApplication(JBOSS_APP_NAME).perform();
	}
}
