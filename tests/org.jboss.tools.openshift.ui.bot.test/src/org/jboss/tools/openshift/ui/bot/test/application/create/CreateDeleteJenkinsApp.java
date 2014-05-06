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
public class CreateDeleteJenkinsApp {

	private final String JENKINS_APP_NAME = "jenkinsapp"+ new Date().getTime();
	
	@Test
	public void canCreateJenkinsApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JENKINS, JENKINS_APP_NAME, false, true, true);
	}
	
	@After
	public void canDeleteJenkinsApp() {
		new DeleteApplication(JENKINS_APP_NAME).perform();
	}
}
