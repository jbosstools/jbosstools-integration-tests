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
package org.jboss.tools.openshift.ui.bot.test.common;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.common.reddeer.perspectives.JBossPerspective;
import org.jboss.tools.openshift.reddeer.preference.page.OpenShift2PreferencePage;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test seting up time out for OpenShift remote requests.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID001RemoteRequestTimeoutTest {

	
	@Before
	public void openJBossPerspective() {
		new JBossPerspective().open();
	}
	
	@Test
	public void testSetRemoteRequestTimeout() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		OpenShift2PreferencePage page = new OpenShift2PreferencePage();
		preferenceDialog.open();
		preferenceDialog.select(page);
		
		
		
		assertFalse("Remote request timeout field is not editable.", 
				new LabeledText(OpenShiftLabel.TextLabels.REMOTE_REQUEST_TIMEOUT).isReadOnly());
		
		page.setRemoteRequestTimeout("abcd");
		assertFalse("Apply button should be disable if value is not numeric.",
				new PushButton("Apply").isEnabled());
		
		page.clearRemoteRequestTimeout();
		assertFalse("Apply button should be disable if value is not set.",
				new PushButton("Apply").isEnabled());
		
		while (!page.getRemoteRequestTimeout().equals("360")) {
			page.setRemoteRequestTimeout("");
			page.typeRemoteRequestTimeout("360");
		}
		
		page.apply();
		preferenceDialog.ok();
	}
}
