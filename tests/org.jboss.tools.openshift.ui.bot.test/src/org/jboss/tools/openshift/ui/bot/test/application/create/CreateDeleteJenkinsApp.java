package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.openshift.ui.bot.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateDeleteJenkinsApp {

	private final String JENKINS_APP_NAME = TestProperties
			.get("openshift.jenkins.name") + new Date().getTime();
	
	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties
				.get("openshift.jenkins.name"));
	}
	
	@Test
	public void canCreateJenkinsApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JENKINS, JENKINS_APP_NAME, false, true, true);
	}
	
	@After
	public void canDeleteJenkinsApp() {
		new DeleteApplication(JENKINS_APP_NAME, OpenShiftLabel.AppType.JENKINS_TREE).perform();
	}
}
