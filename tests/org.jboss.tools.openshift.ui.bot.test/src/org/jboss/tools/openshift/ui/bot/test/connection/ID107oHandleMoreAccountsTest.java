package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.junit.Test;

/**
 * Only OpenShift Online test! Test capabilites of handling more accounts in
 * OpenShift Explorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID107oHandleMoreAccountsTest {

	@Test
	public void testMoreAccountInOpenShiftExplorer() {
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
