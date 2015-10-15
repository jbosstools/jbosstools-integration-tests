package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
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
		
		NewOpenShift2ApplicationWizard wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME,
				DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromCentral();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.DIY, 
				applicationName, false, true, false, false, null, null, true, null, null, null, (String[]) null);

		wizard.postCreateSteps(true);
		
		wizard.verifyApplication(applicationName, applicationName);
		wizard.verifyServerAdapter(applicationName, applicationName);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName, 
				applicationName).perform();
	}
}
