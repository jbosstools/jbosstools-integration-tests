package org.jboss.tools.openshift.ui.bot.test.connection;

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
		explorer.openConnectionShell();
		explorer.connectToOpenShiftV2(Datastore.SERVER, Datastore.USERNAME,
				System.getProperty("user.pwd"), false, false);
		
		explorer.getConnection(Datastore.USERNAME);	
	}
}
