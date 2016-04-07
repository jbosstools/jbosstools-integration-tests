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
package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test removing an OpenShift connection from OpenShift explorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID106RemoveConnectionTest {

	private OpenShiftExplorerView explorer;
	
	@Before
	public void initializeExplorer() {
		explorer = new OpenShiftExplorerView();
	}
	
	@Test
	public void testRemoveConnection() {
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();
		
		// sometimes there is Loading OS 2 connection details shell
		try {
			new DefaultShell(OpenShiftLabel.Shell.LOADING_CONNECTION_DETAILS);
			explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();
		} catch (RedDeerException ex) {
		}
		
		new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_CONNECTION).select();
		
		new DefaultShell(OpenShiftLabel.Shell.REMOVE_CONNECTION);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.REMOVE_CONNECTION),
				TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("Connection is still presented in OpenShift explorer",
				explorer.connectionExists(DatastoreOS2.USERNAME));
	}
	
	@After
	public void recreateConnection() {
		if (!explorer.connectionExists(DatastoreOS2.USERNAME)) {
			explorer.openConnectionShell();
			explorer.connectToOpenShift2(DatastoreOS2.SERVER, DatastoreOS2.USERNAME,
					System.getProperty("openshift.password"), false, false, false);
		}
	}
	
}
