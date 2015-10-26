package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of creating a quickstart.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID405CreateQuickstartTest {

	private String applicationName = "django" + System.currentTimeMillis();
	
	@Test
	public void testCreateQuickstart() {
		ApplicationCreator templates = new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false);
		templates.createQuickstart(OpenShiftLabel.Cartridge.DJANGO, applicationName,
				false, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
				applicationName).perform();
	}
	
}
