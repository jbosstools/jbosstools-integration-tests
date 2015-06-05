package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of creating an application on downloadable cartridge.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID406CreateApplicationOnDownloadableCartridgeTest {

	public static String downloadableURL = 
			"https://cartreflect-claytondev.rhcloud.com/github/smarterclayton/openshift-go-cart";
	private String applicationName = "gocart" + System.currentTimeMillis();
	
	@Test
	public void testCreateApplicatinOnDownloadableCartridge() {
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createApplicationOnDownloadableCartridge(
				downloadableURL, applicationName, false, false, true, null, (String[]) null);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
	
}
