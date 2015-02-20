package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test correctness of application details.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID408ApplicationPropertiesTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Before
	public void createApplication() {
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
	}
	
	@Test
	public void testApplicationDetails() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeItem application = explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		
		applicationDetails(explorer, application, applicationName, "Do-It-Yourself 0.1 (diy-0.1)",
				OpenShiftLabel.ContextMenu.APPLICATION_DETAILS);
	}
	
	/**
	 * Test application details.
	 * Application type is type of an application received via REST
	 * calling (mostly lowercase name with dash and number).
	 */
	public static void applicationDetails(View viewOfItem, TreeItem itemToHandle,
			String applicationName, String applicationType, String... contextMenuPath) {
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_DETAILS),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_DETAILS);

		// Process properties
		Map<String, String> properties = new HashMap<String, String>();
		Tree propertyTree = new DefaultTree();
		for (TreeItem item: propertyTree.getAllItems()) {
			try {
				properties.put(item.getCell(0), item.getCell(1));
			} catch (NullPointerException ex) {
				if (item.getCell(0).equals("Cartridges")) {
					// PASS
				} else {
					fail("Property " + item.getCell(0) + " has no associated value.");
				}
			}
		}
		
		assertTrue("Application name property is not correct. Was "
				+ properties.get("Name") + " but should be " + applicationName,
				properties.get("Name").equals(applicationName));
		
		assertTrue("Application type property is not correct. Was "
				+ properties.get("Type") + " but should be " + applicationType,
				properties.get("Type").equals(applicationType));
		
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_DETAILS),
				TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();;
	}
}
