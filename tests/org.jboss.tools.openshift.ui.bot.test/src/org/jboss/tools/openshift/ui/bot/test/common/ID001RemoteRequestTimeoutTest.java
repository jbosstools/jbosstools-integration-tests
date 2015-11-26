package org.jboss.tools.openshift.ui.bot.test.common;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.openshift.reddeer.perspective.JBossPerspective;
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
		
		// Must be typeText, bcs. after insertion the Apply button is not enabled
		// and workaround for fedora bcs. of keyboard events
		while (!page.getRemoteRequestTimeout().equals("360")) {
			page.setRemoteRequestTimeout("");
			page.typeRemoteRequestTimeout("360");
		}
		
		page.apply();
		preferenceDialog.ok();
	}
}
