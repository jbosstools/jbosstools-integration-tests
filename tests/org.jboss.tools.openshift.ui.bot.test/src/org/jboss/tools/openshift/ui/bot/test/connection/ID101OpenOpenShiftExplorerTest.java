package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test opening OpenShift Explorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID101OpenOpenShiftExplorerTest {

	@Test
	public void testOpenOpenShiftExplorerView() {
		try {
			OpenShiftExplorerView explorer = new OpenShiftExplorerView();
			explorer.open();
			// PASS
		} catch (CoreLayerException ex) {
			fail("OpenShift Explorer view cannot be opened.");
		}
	}
}
