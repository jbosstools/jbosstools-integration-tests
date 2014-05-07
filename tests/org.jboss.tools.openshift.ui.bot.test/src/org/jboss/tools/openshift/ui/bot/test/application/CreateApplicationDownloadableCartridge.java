package org.jboss.tools.openshift.ui.bot.test.application;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of creating new application on a downloadable cartridge.
 * By default is used Haskell application
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateApplicationDownloadableCartridge {

	/* Alternative links (other applications)
	// http://www.accursoft.com/cartridges/mflow.yml
	// http://www.accursoft.com/cartridges/network.yml
	*/
	
	private String URL = "http://www.accursoft.com/cartridges/snap.yml";
	private String APP_NAME = "dlcart" + System.currentTimeMillis();
	
	@Test
	public void createApplicationOnDownloadableCartridge() {
		new NewApplicationTemplates(false).createApplicationOnDownloadableCartridge(
				URL, APP_NAME, false, false, (String[]) null);;
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(APP_NAME).perform();
	}

}
