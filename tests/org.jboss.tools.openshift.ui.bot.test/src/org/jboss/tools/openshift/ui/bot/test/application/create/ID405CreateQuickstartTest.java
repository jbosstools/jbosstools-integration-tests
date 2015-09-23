package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.Templates;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
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
		Templates templates = new Templates(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, false);
		templates.createQuickstart(OpenShiftLabel.Cartridge.DJANGO, applicationName,
				false, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, applicationName,
				applicationName).perform();
	}
	
}
