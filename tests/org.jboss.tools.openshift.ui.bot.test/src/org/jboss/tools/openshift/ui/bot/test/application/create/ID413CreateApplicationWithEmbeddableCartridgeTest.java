package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShift2Application;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
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
		
		OpenShift2ApplicationWizard wizard = new OpenShift2ApplicationWizard(Datastore.USERNAME, 
				Datastore.SERVER, Datastore.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.DIY,
				applicationName, false, true, false, false, null, null, true,
				null, null, null, OpenShiftLabel.EmbeddableCartridge.CRON);
		wizard.postCreateSteps(true);
		
		OpenShift2Application application = explorer.getOpenShift2Connection(Datastore.USERNAME, Datastore.SERVER).getDomain(
				Datastore.DOMAIN).getApplication(applicationName);
		application.select();
		application.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			application.getTreeItem().getItem("Cron 1.4 cron-1.4");
			// PASS
		} catch (SWTLayerException ex) {
			fail("There is no tree item for embedded cartridge under application in OpenShift explorer view. "
					+ "There is item with name \"" + application.getTreeItem().getItems().get(0).getText() + "\"");
		}
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, applicationName,
				applicationName).perform();
	}
}
