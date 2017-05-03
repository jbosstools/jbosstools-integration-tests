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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

public class ID605ShowWarningOnMultipleCartridgeRemoveTest extends IDXXXCreateTestingApplication {

	OpenShiftExplorerView explorer = new OpenShiftExplorerView();

	@Test
	public void deselectEmbeddedCartridge() {
		embedCartridge(OpenShiftLabel.EmbeddableCartridge.CRON, true);		
				
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
			getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).select();
		try {
			new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).setChecked(false);
		} catch (WaitTimeoutExpiredException ex) {
			// pass
		}
		
		try {
			new WaitUntil(new ShellIsAvailable("Remove cartridge cron-1.4"));
			
			new DefaultShell("Remove cartridge cron-1.4");
			new YesButton().click();
			
			new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
			assertFalse("Cartridge should be unchecked after its removing, but it is still presented.",
					new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).isChecked()); 
			
			
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be warning dialog about possible data loss, but dialog is missing.");
		}
		
		new CancelButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	
	@Test
	public void deselectMultipleEmbeddedCartridges() {
		embedCartridge(OpenShiftLabel.EmbeddableCartridge.CRON, true);
		embedCartridge(OpenShiftLabel.EmbeddableCartridge.POSTGRE_SQL, true);
		
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
			getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new PushButton("Deselect All").click();
		
		try {
			new WaitUntil(new ShellIsAvailable("Deselect All Cartridges"));
			
			new DefaultShell("Deselect All Cartridges");
			new YesButton().click();
			
			new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
			
			assertFalse("Cartridge has been removed, it should not be listed as checked anymore",
					new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).isChecked());
			assertFalse("Cartridge has been removed, it should not be listed as checked anymore",
					new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.POSTGRE_SQL).isChecked());
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be warning dialog about possible data loss, but dialog is missing.");
		}
		
		new CancelButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	// embed if embed is true, remove otherwise
	private void embedCartridge(String cartridge, boolean embed) {
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
			getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new DefaultTable().getItem(cartridge).select();
		try {
			new DefaultTable().getItem(cartridge).setChecked(embed);
		} catch (WaitTimeoutExpiredException ex) {
			// pass, there is an issue with table events, but it works
		}
		
		new WaitUntil(new ControlIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		try {
			new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE),
					TimePeriod.LONG);
		
			new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE);
			new OkButton().click();
		} catch (WaitTimeoutExpiredException ex) {
			// PASS - there is no embed cartridge shell showing info about cartridge
		}
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
	}
}
