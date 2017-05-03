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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test create and add SSH key to OpenShift. Also verify properly set up of SSH key in Eclipse SSH2 preferences.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID150CreateNewSSHKeyTest {

	
	@Test
	public void testCreateAndAddNewSSHKey() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();

		try {
			new DefaultShell(OpenShiftLabel.Shell.LOADING_CONNECTION_DETAILS);
			new WaitWhile(new ShellIsAvailable(
					OpenShiftLabel.Shell.LOADING_CONNECTION_DETAILS), TimePeriod.LONG);
		} catch (RedDeerException ex) {
		}
		
		new ContextMenu(OpenShiftLabel.ContextMenu.MANAGE_SSH_KEYS).select();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		
		new PushButton(OpenShiftLabel.Button.CREATE_SSH_KEY).click();
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_SSH_KEY);
		
		assertFalse("Finish button should be disabled if SSH key details has not been filled.",
				new FinishButton().isEnabled());
		
		new LabeledText(OpenShiftLabel.TextLabels.NAME).setText(DatastoreOS2.SSH_KEY_NAME);
		new LabeledText(OpenShiftLabel.TextLabels.PRIVATE_NAME).setText(DatastoreOS2.SSH_KEY_FILENAME);
		
		assertTrue("Public key name has not been successfully autocompleted. Was "
				+ new LabeledText(OpenShiftLabel.TextLabels.PUBLIC_NAME).getText()
				+ " but should be " + DatastoreOS2.SSH_KEY_FILENAME + ".pub",
				new LabeledText(OpenShiftLabel.TextLabels.PUBLIC_NAME).getText().equals(
						DatastoreOS2.SSH_KEY_FILENAME + ".pub"));
		
		new WaitUntil(new ControlIsEnabled(new FinishButton()));
		
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.NEW_SSH_KEY), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		
		assertTrue("SSH Key has not been successfully created or at least it is not in required table.",
				new DefaultTable().getItem(0).getText(0).equals(DatastoreOS2.SSH_KEY_NAME));
		
		new DefaultLink().click();
		
		try {
			new DefaultShell("Preferences");
		} catch (SWTLayerException ex) {
			fail("Preferences shell was not opened.");
		}
		
		DatastoreOS2.SSH_HOME = new LabeledText("SSH2 home:").getText();
		assertTrue("SSH Key had not been added into SSH2 eclipse preferences.", 
				new LabeledText("Private keys:").getText().contains(DatastoreOS2.SSH_KEY_FILENAME));
		
		new CancelButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.MANAGE_SSH_KEYS));
	}
	
}
