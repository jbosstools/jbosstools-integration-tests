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
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.junit.Test;

/**
 * Test capabilities of default server usage.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID105DefaultServerTest {

	@Test
	public void testDefaultServerUsage() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.openConnectionShell();

		new DefaultShell(OpenShiftLabel.Shell.NEW_CONNECTION);
		
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(ServerType.OPENSHIFT_2.toString());
		
		CheckBox defaultServerCheckBox = new CheckBox(0);
		LabeledCombo defaultServerCombo = 
				new LabeledCombo(OpenShiftLabel.TextLabels.SERVER);
		
		if (!defaultServerCheckBox.isChecked()) {
			defaultServerCheckBox.click();
		}
		
		assertFalse("Default server field should be read-only at this moment.",
				defaultServerCombo.isEnabled());
		

		assertTrue("Incorrect default server URL. URL is "
				+ defaultServerCombo + " but openshift.redhat.com is expected.",
				defaultServerCombo.getText().contains("openshift.redhat.com"));
		
		defaultServerCheckBox.click();
		
		assertTrue("Default server field should not be read-only at this moment.",
				defaultServerCombo.isEnabled());
		
		defaultServerCombo.setText("nonexisting.server");
		defaultServerCheckBox.click();
		
		assertTrue("Default server URL has not been restored successfully. Server is "
				+ defaultServerCombo.getText() + " but openshift.redhat.com is expected.", 
				defaultServerCombo.getText().contains("https://openshift.redhat.com"));
		
		new CancelButton().click();
	}
	
}
