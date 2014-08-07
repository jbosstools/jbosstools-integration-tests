package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
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
		Templates newApplicationTemplate = new Templates(Datastore.USERNAME, 
				Datastore.DOMAIN, true);
		
		// Assertions are done inside of create method
		newApplicationTemplate.createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
}
