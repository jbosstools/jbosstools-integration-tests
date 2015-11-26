package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShift2Application;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
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
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
	}
	
	@Test
	public void testApplicationDetails() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName);
		
		applicationDetails(explorer, application.getTreeItem(), applicationName, "Do-It-Yourself 0.1 (diy-0.1)",
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
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				applicationName, applicationName).perform();;
	}
}
