package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * Test capabilities of create/delete scalable JBoss EAP application on a OpenShift server.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateDeleteScalableJBossApp {

	private final String JBOSS_APP_NAME = "seapapp" + new Date().getTime();

	@Test
	public void canCreateScalableJBossApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JBOSS_EAP, JBOSS_APP_NAME, true, true, true);
	}

	@After
	public void canDeleteScalableJBossApp() {
		new DeleteApplication(JBOSS_APP_NAME).perform();
	}
}
