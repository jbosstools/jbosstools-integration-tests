package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.exception.RedDeerException;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test creating a new OpenShift Online connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID103oCreateNewConnectionTest {

	@BeforeClass
	public static void storeCredentials() {
		Datastore.SERVER = System.getProperty("libra.server"); 
		Datastore.USERNAME = System.getProperty("user.name");
	}
	
	@Test
	public void testConnectToOpenShift() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		try {
			explorer.openConnectionShell();
			explorer.connectToOpenShift(Datastore.SERVER, Datastore.USERNAME,
					System.getProperty("user.pwd"), false, false);
			// PASS
		} catch (RedDeerException ex) {
			fail("Cannot create new connection.");
		}
		
		try {
			explorer.getConnection(Datastore.USERNAME);
			// PASS
		} catch (JFaceLayerException ex) {
			fail("Connection has not been created. It is not listed in OpenShift explorer");
		}
	}
}
