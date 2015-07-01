package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
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

		new DefaultShell("");
		
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
