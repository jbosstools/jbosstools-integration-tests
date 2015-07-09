package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.Test;

/**
 * Test opening new connection dialog via tool item and context menu.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID102OpenNewConnectionShellTest {
	
	OpenShiftExplorerView explorer = new OpenShiftExplorerView();
	
	@Test
	public void testOpenConnectionShellViaLinkTest() {
		explorer.open();
		
		try {
			new DefaultLink(OpenShiftLabel.TextLabels.NEW_CONNECTION).click();
			// PASS
		} catch (SWTLayerException ex) {
			fail("New Connection shell cannot be opened via context menu.");
		}
		
		verifyShell();
	}
	
	@Test
	public void testOpenConnectionShellViaToolItemTest() {
		explorer.open();
		
		try {
			new DefaultToolItem(OpenShiftLabel.Others.CONNECT_TOOL_ITEM).click();
			// PASS
		} catch (SWTLayerException ex) {
			fail("New Connection shell cannot be opened via view tool item.");
		}
		
		verifyShell();
	}
	
	private void verifyShell() {
		try {
			new DefaultShell("");
			// To be sure, that it is correct shell
			new LabeledText("Username:");
		} catch (SWTLayerException ex) {
			fail("New Connectin shell was not opened.");
		}
		
		new CancelButton().click();
	}
}
