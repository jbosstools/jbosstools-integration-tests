/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.application.cartridge;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift2Application;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
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
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName);
		
		embedCartridge(explorer, application.getTreeItem(), applicationName, OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE);
	}
	
	public static void embedCartridge(View viewOfItem, TreeItem itemToHandle, String applicationName,
			String... contextMenuPath) {
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		try {
			new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell with embeddable cartridges has not been opened.");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		assertTrue("There should be no checked embeddable cartridges on a blank application",
				getCheckedItemsCount() == 0);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).select();
		//TableUtils.checkTableItem(new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON), true);
		//	new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).setChecked(true);
		try {
			new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).setChecked(true);
		} catch (WaitTimeoutExpiredException ex) {
			// pass, bad notifications of event
		}
		
		new WaitUntil(new ControlIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE).setFocus();
		new OkButton().click();
	
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(
				DatastoreOS2.DOMAIN).getApplication(applicationName);
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
		
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
	
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		assertTrue("There should be right one embeddable cartridges on the application",
				getCheckedItemsCount() == 1);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).select();
		try {
			new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).setChecked(false);
		} catch (WaitTimeoutExpiredException ex) {
			// pass
		}
		
		Shell removeCartridge = new DefaultShell("Remove cartridge cron-1.4");
		new YesButton().click();
		
		new WaitWhile(new ShellIsAvailable(removeCartridge), TimePeriod.LONG);
		new WaitUntil(new ControlIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		explorer.open();
		application.select();
		application.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			application.getTreeItem().getItem("Cron 1.4 cron-1.4");
			fail("There is tree item for embedded cartridge under application in OpenShift explorer view. "
					+ "There is item with name \"" + application.getTreeItem().getItems().get(0).getText() + "\"");
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
