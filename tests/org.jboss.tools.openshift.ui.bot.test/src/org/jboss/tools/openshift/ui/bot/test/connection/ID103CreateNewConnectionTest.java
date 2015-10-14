package org.jboss.tools.openshift.ui.bot.test.connection;

import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
import org.junit.Test;

/**
 * Test creating a new OpenShift Online connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID103CreateNewConnectionTest {

	@Test
	@RunIf(conditionClass = OS2CredentialsExist.class)
	public void testConnectToOpenShift() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.openConnectionShell();
		explorer.connectToOpenShift2(DatastoreOS2.SERVER, DatastoreOS2.USERNAME,
				System.getProperty("openshift.password"), false, false, false);
		
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER);	
	}
	
	@Test
	@RunIf(conditionClass = OSE2CredentialsExist.class)
	public void testCreateConnectionToOpenShiftEnterprise() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.openConnectionShell();
		
		explorer.connectToOpenShift2(DatastoreOS2.SERVER, DatastoreOS2.USERNAME, 
				System.getProperty("openshift.password"), false, false, true);
		
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER);
	}	
}
