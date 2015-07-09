package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteApplication;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewApplicationWizard;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenNewApplicationWizard;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of creating a new application via central.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID404CreateNewApplicationViaCentralTest {

	private String applicationName;
	
	@Test
	public void testCreateNewApplicationViaCentral() {
		applicationName = "diy" + System.currentTimeMillis();
		OpenNewApplicationWizard.openWizardFromCentral(Datastore.USERNAME);
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.DIY, 
				Datastore.DOMAIN, applicationName, false, true,
				false, false, null, null, true, null, null, null, (String[]) null);

		wizard.postCreateSteps(true);
		
		wizard.verifyApplication(Datastore.USERNAME, Datastore.DOMAIN,
				applicationName, applicationName);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
}
