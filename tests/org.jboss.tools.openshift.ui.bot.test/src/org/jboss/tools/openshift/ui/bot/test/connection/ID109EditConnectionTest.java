package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
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
		
		explorer.getConnection(Datastore.USERNAME).select();
		try {
			new ContextMenu(OpenShiftLabel.ContextMenu.EDIT_CONNECTION).select();
		} catch (SWTLayerException ex) {
			fail("Cannot open edit connection shell.");
		}
		
		new DefaultShell("");
		
		assertFalse("Edit connection shell should be aware of validated password",
				new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).getText().isEmpty());
		
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText("");
		
		assertFalse("Finish button should be disabled if password is empty", 
				new FinishButton().isEnabled());
		
		new CancelButton().click();
	}
	
}
