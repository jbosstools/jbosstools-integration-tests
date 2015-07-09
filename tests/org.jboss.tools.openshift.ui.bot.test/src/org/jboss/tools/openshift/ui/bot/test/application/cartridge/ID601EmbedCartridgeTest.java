package org.jboss.tools.openshift.ui.bot.test.application.cartridge;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.junit.Test;

/**
 * Test capabilities of embedding embeddable cartridges.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID601EmbedCartridgeTest extends IDXXXCreateTestingApplication {

	@Test
	public void testAddRemoveEmbeddableCartridge() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeItem application = explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
	
		embedCartridge(explorer, application, applicationName, OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE);
	}
	
	public static void embedCartridge(View viewOfItem, TreeItem itemToHandle, String applicationName,
			String... contextMenuPath) {
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell with embeddable cartridges has not been opened.");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		assertTrue("There should be no checked embeddable cartridges on a blank application",
				getCheckedItemsCount() == 0);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).setChecked(true);
		
		new WaitUntil(new ButtonWithTextIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE).setFocus();
		new OkButton().click();
	
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeItem application = explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN,
				applicationName);
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
		
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
	
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		assertTrue("There should be right one embeddable cartridges on the application",
				getCheckedItemsCount() == 1);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).setChecked(false);
		
		new DefaultShell("Remove cartridge cron-1.4");
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("Remove cartridge cron-1.4"));
		new WaitUntil(new ButtonWithTextIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		explorer.open();
		application.select();
		application.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			application.getItem("Cron 1.4 cron-1.4");
			fail("There is tree item for embedded cartridge under application in OpenShift explorer view. "
					+ "There is item with name \"" + application.getItems().get(0).getText() + "\"");
		} catch (CoreLayerException ex) {
			// pass
		}
	}
	
	
	private static int getCheckedItemsCount() {
		int checkedCount = 0;
		for (TableItem item: new DefaultTable().getItems()) {
			if (item.isChecked()) {
				checkedCount++;
			}
		}
		return checkedCount;
	}
}
