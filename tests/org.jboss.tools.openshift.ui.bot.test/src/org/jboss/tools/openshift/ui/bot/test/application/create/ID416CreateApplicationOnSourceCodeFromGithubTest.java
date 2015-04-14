package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.openshift.ui.condition.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.wizard.application.NewApplicationWizard;
import org.jboss.tools.openshift.ui.wizard.application.OpenNewApplicationWizard;
import org.junit.After;
import org.junit.Test;

public class ID416CreateApplicationOnSourceCodeFromGithubTest {

	private String sourceCodeURL = "https://github.com/openshift/django-example";
	private String applicationName = "django" + System.currentTimeMillis();
	
	private boolean applicationCreated = false;
	
	@Test
	public void testCreateApplicationOnSourceCodeFromGithub() {
		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME, Datastore.DOMAIN);
	
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.PYTHON, 
				Datastore.DOMAIN, applicationName, false, true, false, 
				false, sourceCodeURL, null, true, null, null, null, (String[]) null);
		
		wizard.postCreateSteps(false);
		
		applicationCreated = true;
	
		// Wait a bit more bcs of django
		AbstractWait.sleep(TimePeriod.getCustom(7));
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(Datastore.USERNAME, 
					Datastore.DOMAIN, applicationName, "Yeah Django!"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application from github source code has not been created successfully.");
		}
	}
	
	@After
	public void deleteApplication() {
		if (applicationCreated) {
			new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
		}
	}
}
