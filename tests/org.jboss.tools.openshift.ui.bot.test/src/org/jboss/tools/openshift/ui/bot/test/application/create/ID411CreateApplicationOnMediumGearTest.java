package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.Templates;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of creating an application on medium gear.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID411CreateApplicationOnMediumGearTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Test
	public void testCreateApplicationOnMediumGear() {
		new Templates(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, false, false, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, applicationName, 
				applicationName).perform();
	}
	
}
