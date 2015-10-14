package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
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
	@RunIf(conditionClass = OS2CredentialsExist.class)
	public void testMoreAccountInOpenShiftExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		explorer.openConnectionShell();
		explorer.connectToOpenShift2(DatastoreOS2.X_SERVER,
				DatastoreOS2.X_USERNAME, DatastoreOS2.X_PASSWORD, false, false, false);
		
		try {
			explorer.getOpenShift2Connection(DatastoreOS2.X_USERNAME, DatastoreOS2.X_SERVER);
			// PASS
		} catch (JFaceLayerException ex) {
			fail("Connection has not been created. It is not listed in OpenShift explorer");
		}
	}
}
