package org.jboss.tools.openshift.ui.bot.test.common;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.utils.JBossPerspective;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
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
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.select("JBoss Tools", "OpenShift");
		
		LabeledText timeoutField = new LabeledText("Remote requests timeout (in seconds):");
		
		assertFalse("Remote request timeout field is not editable.", 
				timeoutField.isReadOnly());
		
		timeoutField.setText("abcd");
		assertFalse("Apply button should be disable if value is not numeric.",
				new PushButton("Apply").isEnabled());
		
		timeoutField.setText("");
		assertFalse("Apply button should be disable if value is not set.",
				new PushButton("Apply").isEnabled());
		
		// Must be typeText, bcs. after insertion the Apply button is not enabled
		// and workaround for fedora bcs. of keyboard events
		while (!timeoutField.getText().equals("360")) {
			timeoutField.setText("");
			timeoutField.typeText("360");
		}
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.APPLY)),
				TimePeriod.NORMAL);
		
		new PushButton(OpenShiftLabel.Button.APPLY).click();
		
		new OkButton().click();
	}
}
