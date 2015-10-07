package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftApplicationExists;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of switching a domain in New application wizard for a new
 * application. Opens New application wizard on first domain but selects second domain.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID412UseAnotherDomainTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Test
	public void testSwitchDomain() {
		OpenShift2ApplicationWizard wizard = new OpenShift2ApplicationWizard(Datastore.USERNAME, Datastore.SERVER,
				Datastore.SECOND_DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.DIY,
				applicationName, false, true, false, false, null, null, true, null, null, null, (String[]) null);
		
		wizard.postCreateSteps(true);
		
		new WaitUntil(new OpenShiftApplicationExists(Datastore.USERNAME, Datastore.SERVER, Datastore.SECOND_DOMAIN,
				applicationName), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(Datastore.USERNAME, Datastore.SERVER, Datastore.SECOND_DOMAIN, applicationName, 
				applicationName).perform();
	}
	
}
