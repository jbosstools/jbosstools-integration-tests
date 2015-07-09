package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewApplicationWizard;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenNewApplicationWizard;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteApplication;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of creating a new application with embedded cartridge.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID413CreateApplicationWithEmbeddableCartridgeTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Test
	public void testCreateApplicationWithEmbeddableCartridge() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME, Datastore.DOMAIN);
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.DIY,
				Datastore.DOMAIN, applicationName, false, true, false, false, null, null, true,
				null, null, null, OpenShiftLabel.EmbeddableCartridge.CRON);
		wizard.postCreateSteps(true);
		
		TreeItem application = explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		application.select();
		application.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			application.getItem("Cron 1.4 cron-1.4");
			// PASS
		} catch (SWTLayerException ex) {
			fail("There is no tree item for embedded cartridge under application in OpenShift explorer view. "
					+ "There is item with name \"" + application.getItems().get(0).getText() + "\"");
		}
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
}
