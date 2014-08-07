package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
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
		Templates templates = new Templates(Datastore.USERNAME, Datastore.DOMAIN, true);
		templates.createQuickstart(OpenShiftLabel.Cartridge.DJANGO, applicationName,
				false, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
	
}
