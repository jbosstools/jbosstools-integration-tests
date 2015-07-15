package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.Test;

/**
 * Test refresh connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID112RefreshConnectionTest {

	@Test
	public void testRefreshConnection() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		explorer.getConnection(Datastore.USERNAME);
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();
		
		assertTrue("Connection is not refreshed - or there is no job for it", new JobIsRunning().test());
	}
	
}
