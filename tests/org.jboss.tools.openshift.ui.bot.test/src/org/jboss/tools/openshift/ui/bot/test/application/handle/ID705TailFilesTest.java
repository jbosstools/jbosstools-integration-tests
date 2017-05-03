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
package org.jboss.tools.openshift.ui.bot.test.application.handle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.tools.openshift.reddeer.condition.ConsoleHasSomeText;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift2Application;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

/**
 * Testing capabilities of tailing files on an application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID705TailFilesTest extends IDXXXCreateTestingApplication {
	
	private static String requiredOptions = "-f -n 100 */logs/*";
	private static String desiredOptions = "-f -n 50 */logs/*";
	
	@Test
	public void testTailFiles() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName);

		tailFilesTest(explorer, application.getTreeItem(), true, OpenShiftLabel.ContextMenu.TAIL_FILES);
	}
	
	/**
	 * Test tailing files.
	 * 
	 * @param itemToHandle tree item to handle
	 */
	public static void tailFilesTest(View viewOfItem, TreeItem itemToHandle, boolean verifyInConsole,
			String... contextMenuPath) {
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.TAIL_FILES), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.TAIL_FILES);
		
		LabeledText tailOptionsText = new LabeledText(OpenShiftLabel.TextLabels.TAIL_OPTIONS);
		String tailOptions = tailOptionsText.getText();
		
		assertTrue("Tail options is not set properly. Was " + tailOptions + " but should be " + requiredOptions,
				tailOptions.equals(requiredOptions));
		
		tailOptionsText.setText("error");
		
		new PushButton(OpenShiftLabel.Button.RESET).click();
		
		assertTrue("Tail options has not been reseted properly.",
				tailOptions.equals(requiredOptions));
		
		assertTrue("Cartridge should be selected by default for tailing files, but it is not.",
				new DefaultTable().getItem(0).isChecked());
		
		new PushButton(OpenShiftLabel.Button.DESELECT_ALL).click();
		
		assertFalse("Cartridge should not be selected by now for tailing files, but it is.",
				new DefaultTable().getItem(0).isChecked());
		
		new PushButton(OpenShiftLabel.Button.SELECT_ALL).click();
		
		assertTrue("Cartridge should be selected by now for tailing files, but it is not.",
				new DefaultTable().getItem(0).isChecked());
		
		tailOptionsText.setText(desiredOptions);
		new FinishButton().click();
		
		ConsoleView console = new ConsoleView();
		console.open();
		
		try {
			new WaitUntil(new ConsoleHasSomeText(), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Console should not be empty.");
		}
		
		if (verifyInConsole) {
			verifyTailing(viewOfItem, itemToHandle);
		}
		
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.TAIL_FILES), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.TAIL_FILES);
		
		assertTrue("Tailing options has not been remembered.",
				new LabeledText(OpenShiftLabel.TextLabels.TAIL_OPTIONS).getText().equals(desiredOptions));
		
		new CancelButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.TAIL_FILES), TimePeriod.LONG);
	}
	
	private static void verifyTailing(View viewOfItem, TreeItem itemToHandle) {
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_BROWSER).select();
		
		new ConsoleView().open();
		try {
			new WaitUntil(new org.jboss.reddeer.eclipse.condition.ConsoleHasText("GET / HTTP"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Console should contain info in tailing files about showing an application in browser.");
		}	
	}
}
