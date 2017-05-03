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
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.condition.TableContainsItem;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift2Application;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

/**
 * Test capabilities of creating, editing and removing environment variables
 * on an application and showing them.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID707HandleEnvironmentVariablesTest extends IDXXXCreateTestingApplication {
	
	@Test
	public void testHandleEnvironmentVariables() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName);
		
		environmentVariablesHandling(explorer, application.getTreeItem(), 
				OpenShiftLabel.Shell.MANAGE_ENV_VARS + applicationName,
				new String[] {OpenShiftLabel.ContextMenu.EDIT_ENV_VARS},
				new String[] {OpenShiftLabel.ContextMenu.SHOW_ENV_VARS});		
	}
	
	public static void environmentVariablesHandling(View viewOfItem, TreeItem itemToHandle,
			String manageApplicationShell, String[] contextMenuPathToEditEnvVariable,
			String[] contextMenuPathToShowEnvVariable) {
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPathToEditEnvVariable).select();
		
		new WaitUntil(new ShellIsAvailable(manageApplicationShell), TimePeriod.LONG);
		
		new DefaultShell(manageApplicationShell);

		createVariable("variable1", "value1");
		
		new DefaultShell(manageApplicationShell);
		new PushButton("Refresh").click();
		
		try {
			new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.REFRESH_ENV_VARS));
		} catch (WaitTimeoutExpiredException ex) {
			fail("Warning about possible loss of local changes has not been shown.");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.REFRESH_ENV_VARS);
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.REFRESH_ENV_VARS));
		try {
			new WaitWhile(new TableContainsItem(new DefaultTable(), "variable1", 0), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Variable has not been removed after refreshing not commited changes.");
		}
		
		createVariable("variable1", "value1");
		createVariable("variable2", "value2");
		
		new DefaultShell(manageApplicationShell);
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(manageApplicationShell), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		viewOfItem.open();
		itemToHandle.select();
		new ContextMenu(contextMenuPathToShowEnvVariable).select();
		
		try {
			new WaitUntil(new ConsoleHasText("variable1=value1"), TimePeriod.LONG);
			new WaitUntil(new ConsoleHasText("variable2=value2"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Environment variable has not been propagated correctly.");
		}
		
		viewOfItem.open();
		itemToHandle.select();
		new ContextMenu(contextMenuPathToEditEnvVariable).select();
		
		new WaitUntil(new ShellIsAvailable(manageApplicationShell), TimePeriod.LONG);
		
		new DefaultShell(manageApplicationShell);
		new DefaultTable().getItem("variable1", 0).select();
		new PushButton(OpenShiftLabel.Button.REMOVE).click();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.REMOVE_ENV_VAR), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.REMOVE_ENV_VAR);
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.REMOVE_ENV_VAR), TimePeriod.LONG);
		
		new DefaultShell(manageApplicationShell);
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(manageApplicationShell), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		viewOfItem.open();
		itemToHandle.select();
		new ContextMenu(contextMenuPathToShowEnvVariable).select();
		
		new WaitUntil(new ConsoleHasText("variable2=value2"), TimePeriod.LONG);
		
		assertFalse("There should not be specified environment variable at this moment.",
				new ConsoleView().getConsoleText().contains("variable1=value1"));
		
		viewOfItem.open();
		itemToHandle.select();
		new ContextMenu(contextMenuPathToEditEnvVariable).select();
		
		new WaitUntil(new ShellIsAvailable(manageApplicationShell), TimePeriod.LONG);
		
		new DefaultShell(manageApplicationShell);
		new DefaultTable().getItem("variable2", 0).select();
		new PushButton(OpenShiftLabel.Button.EDIT).click();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_ENV_VAR), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_ENV_VAR);
		
		new DefaultText(0).setText("variable3");
		new DefaultText(1).setText("value3");
		
		new WaitUntil(new ControlIsEnabled(new OkButton()), TimePeriod.LONG);
		
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_ENV_VAR), TimePeriod.LONG);
		
		new DefaultShell(manageApplicationShell);
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(manageApplicationShell), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		viewOfItem.open();
		itemToHandle.select();
		new ContextMenu(contextMenuPathToShowEnvVariable).select();
		
		try {
			new WaitUntil(new ConsoleHasText("variable3=value3"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Environment variable has not been modified successfully.");
		}
	}
	
	private static void createVariable(String name, String value) {
		new WaitUntil(new ControlIsEnabled(new PushButton(OpenShiftLabel.Button.ADD)), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.ADD).click();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_ENV_VAR), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_ENV_VAR);
		
		new DefaultText(0).setText(name);
		new DefaultText(1).setText(value);
		
		new WaitUntil(new ControlIsEnabled(new OkButton()), TimePeriod.LONG);
		
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_ENV_VAR), TimePeriod.LONG);
	}
}
