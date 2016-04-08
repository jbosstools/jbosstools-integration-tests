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
package org.jboss.tools.openshift.reddeer.utils;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.equinox.security.ui.StoragePreferencePage;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.jboss.tools.openshift.reddeer.view.resources.AbstractOpenShiftConnection;

public class SecureStorage {
	
	public static final String PASSWORD = "password";
	
	/**
	 * Store password in secure storage for specified user.
	 * 
	 * @param username
	 */
	public static void storeOpenShiftPassword(String username, String server, ServerType serverType) {
		triggerSecureStorageOfPasswordInConnectionDialog(username, server, true, serverType);
	}
	
	/**
	 * Remove password in secure storage for specified user.
	 * 
	 * @param username
	 */
	public static void removeOpenShiftPassword(String username, String server, ServerType serverType) {
		triggerSecureStorageOfPasswordInConnectionDialog(username, server, false, serverType);
	}
	
	/**
	 * Triggers secure storage of password.
	 * 
	 * @param username user name
	 * @param server server
	 * @param storePassword store password if value is set to true, remove password if value is set to false
	 * @param serverType type of a server
	 */
	private static void triggerSecureStorageOfPasswordInConnectionDialog(String username, String server,
			boolean storePassword, ServerType serverType) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		AbstractOpenShiftConnection connection;
		if (serverType.equals(ServerType.OPENSHIFT_2)) {
			connection = explorer.getOpenShift2Connection(username, server);
		} else {
			connection = explorer.getOpenShift3Connection();
		}
		connection.select();
		
		// if loading connection shell is opened
		try {
			new DefaultShell("Loading OpenShift 2 connection details");
			connection.select();
		} catch (RedDeerException ex) {
		}
		
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT_CONNECTION).select();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CONNECTION);
		
		// Store password if it is not stored
		if (!(new CheckBox(1).isChecked()) && storePassword) {
			new CheckBox(1).click();
			new FinishButton().click();
			
			new DefaultShell(OpenShiftLabel.Shell.SECURE_STORAGE_PASSWORD).setFocus();
			
			new DefaultText(0).setText(PASSWORD);
			boolean firstStorage = true;
			try {
				new DefaultText(1).setText(PASSWORD);
			} catch (RedDeerException ex) {
				firstStorage = false;
			}
			
			new WaitUntil(new WidgetIsEnabled(new OkButton()));
			
			new OkButton().click();
			
			if (firstStorage) {
				new DefaultShell(OpenShiftLabel.Shell.SECURE_STORAGE).setFocus(); 
				new NoButton().click();	
			}
		// Remove password if it is stored
		} else if (new CheckBox(1).isChecked() && !storePassword) {
			new CheckBox(1).click();
			new FinishButton().click();
		}
			
		new WaitWhile(new JobIsRunning());
	}
	
	/**
	 * Verifies whether state of password storage for specified user is in correct state.
	 *
	 * @param username
	 * @param server URL of OpenShift server without https prefix
	 * @param shouldExists if true, password should be present in secure storage, if false, there should
	 * 			be no password in secure storage for specified connection
	 * @param serverType OpenShift v2 or v3 server
	 */
	public static void verifySecureStorageOfPassword(String username, String server, boolean shouldExists, ServerType serverType) {
		WorkbenchPreferenceDialog workbenchPreferenceDialog = new WorkbenchPreferenceDialog();
		StoragePreferencePage secureStoragePreferencePage = new StoragePreferencePage();
		new WorkbenchShell().setFocus();
		workbenchPreferenceDialog.open();
		new WorkbenchPreferenceDialog().select(secureStoragePreferencePage);
		
		String plugin;
		if (serverType.equals(ServerType.OPENSHIFT_2)) {
			plugin = "org.jboss.tools.openshift.express.ui";
		} else {
			plugin = "org.jboss.tools.openshift.core";
		}
		secureStoragePreferencePage.selectContentTab();
		boolean exists = secureStoragePreferencePage.passwordExists("[Default Secure Storage]", plugin,
				server, username);
		
		workbenchPreferenceDialog.ok();
		
		assertTrue("Password storage verification failed", 
				shouldExists == exists);
	}
}
