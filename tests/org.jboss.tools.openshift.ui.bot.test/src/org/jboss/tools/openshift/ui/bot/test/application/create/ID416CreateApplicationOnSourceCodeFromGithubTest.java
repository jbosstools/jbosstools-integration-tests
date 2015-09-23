package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.junit.After;
import org.junit.Test;

public class ID416CreateApplicationOnSourceCodeFromGithubTest {

	private String sourceCodeURL = "https://github.com/openshift/django-example";
	private String applicationName = "django" + System.currentTimeMillis();
	
	private boolean applicationCreated = false;
	
	@Test
	public void testCreateApplicationOnSourceCodeFromGithub() {
		OpenShift2ApplicationWizard wizard = new OpenShift2ApplicationWizard(Datastore.USERNAME, 
				Datastore.SERVER, Datastore.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.PYTHON, 
				applicationName, false, true, false, false, sourceCodeURL, null, 
				true, null, null, null, (String[]) null);
		
		wizard.postCreateSteps(false);
		
		applicationCreated = true;
	
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(Datastore.USERNAME, Datastore.SERVER, 
					Datastore.DOMAIN, applicationName, "Yeah Django!"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application from github source code has not been created successfully.");
		}
	}
	
	@After
	public void deleteApplication() {
		if (applicationCreated) {
			new DeleteUtils(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, 
					applicationName, applicationName).perform();
		}
	}
}
