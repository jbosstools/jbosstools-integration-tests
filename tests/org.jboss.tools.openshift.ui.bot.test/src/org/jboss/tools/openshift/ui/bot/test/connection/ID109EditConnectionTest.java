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
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.Test;

/**
 * Test Edit connection shell capabilities.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID109EditConnectionTest {

	@Test
	public void testEditConnection() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();
		
		// sometimes there is Loading OS 2 connection details shell
		try {
			new DefaultShell(OpenShiftLabel.Shell.LOADING_CONNECTION_DETAILS);
			explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();
		} catch (RedDeerException ex) {
		}
		
		try {
			new ContextMenu(OpenShiftLabel.ContextMenu.EDIT_CONNECTION).select();
		} catch (SWTLayerException ex) {
			fail("Cannot open edit connection shell.");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CONNECTION);
		
		assertFalse("Edit connection shell should be aware of validated password",
				new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).getText().isEmpty());
		
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText("");
		
		assertFalse("Finish button should be disabled if password is empty", 
				new FinishButton().isEnabled());
		
		new CancelButton().click();
	}
	
}
