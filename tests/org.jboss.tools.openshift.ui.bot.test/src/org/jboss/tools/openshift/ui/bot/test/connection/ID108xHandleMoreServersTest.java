package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
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
	@RunIf(conditionClass = OSE2CredentialsExist.class)
	public void testHandleMoreServers() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();

		explorer.openConnectionShell();
		explorer.connectToOpenShift2(Datastore.X_SERVER,
				Datastore.X_USERNAME, Datastore.X_PASSWORD, false, false, false);

		try {
			explorer.getOpenShift2Connection(Datastore.X_USERNAME, Datastore.X_SERVER);
			// PASS
		} catch (JFaceLayerException ex) {
			fail("Connection has not been created. It is not listed in OpenShift explorer");
		}
	}	
}
