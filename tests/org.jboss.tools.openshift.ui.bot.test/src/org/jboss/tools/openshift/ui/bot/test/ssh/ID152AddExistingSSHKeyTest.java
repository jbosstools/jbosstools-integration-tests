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
package org.jboss.tools.openshift.ui.bot.test.ssh;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.Test;

/**
 * Test capabilities of adding existing SSH key to OpenShift account.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID152AddExistingSSHKeyTest {

	@Test
	public void testAddExistingSSHKey() {
		addExistingSSHKey(DatastoreOS2.USERNAME, DatastoreOS2.SERVER);
	}
	
	public static void addExistingSSHKey(String username, String server) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		explorer.getOpenShift2Connection(username, server).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.MANAGE_SSH_KEYS).select();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		
		assertTrue("There should not be any SSH keys uploaded yet.",
				new DefaultTable().getItems().size() == 0);
		
		new PushButton(OpenShiftLabel.Button.ADD_SSH_KEY).click();
		
		new DefaultShell(OpenShiftLabel.Shell.ADD_SSH_KEY);
		
		boolean setPath = Display.syncExec(new ResultRunnable<Boolean>() {

			@Override
			public Boolean run() {
				new LabeledText(OpenShiftLabel.TextLabels.NAME).setText(DatastoreOS2.SSH_KEY_NAME);
				new LabeledText(OpenShiftLabel.TextLabels.PUB_KEY).getSWTWidget().setText(
						DatastoreOS2.SSH_HOME + System.getProperty("file.separator") + DatastoreOS2.SSH_KEY_FILENAME +
						".pub");
				return true;
			}
			
		});
		
		assertTrue("Path to the SSH key has not been set up properly", setPath);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ADD_SSH_KEY), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		
		assertTrue("There should be only one SSH key uploaded.",
				new DefaultTable().getItems().size() == 1);
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MANAGE_SSH_KEYS), TimePeriod.LONG);
	}
}
