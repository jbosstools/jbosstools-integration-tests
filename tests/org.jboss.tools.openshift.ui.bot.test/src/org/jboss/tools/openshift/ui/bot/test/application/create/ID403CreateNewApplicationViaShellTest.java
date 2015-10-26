package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of creating a new application via shell menu.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID403CreateNewApplicationViaShellTest {

	private String applicationName;
	
	@Test
	public void testCreateNewApplicationViaShell() {
		applicationName = "diy" + System.currentTimeMillis();
		ApplicationCreator newApplicationTemplate = new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, true);
		
		// Assertions are done inside of create method
		newApplicationTemplate.createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
				applicationName).perform();
	}
}
