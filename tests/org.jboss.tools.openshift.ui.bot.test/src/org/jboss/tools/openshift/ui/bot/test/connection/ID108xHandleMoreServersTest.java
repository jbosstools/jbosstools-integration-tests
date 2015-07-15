package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.junit.Test;

/**
 * OpenShift Enterprise only! Test capabilities to work with more servers.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID108xHandleMoreServersTest {

	@Test
	public void testHandleMoreServers() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();

		explorer.openConnectionShell();
		explorer.connectToOpenShiftV2(Datastore.X_SERVER,
				Datastore.X_USERNAME, Datastore.X_PASSWORD, false, false);

		try {
			explorer.getConnection(Datastore.X_USERNAME);
			// PASS
		} catch (JFaceLayerException ex) {
			fail("Connection has not been created. It is not listed in OpenShift explorer");
		}
	}	
}
