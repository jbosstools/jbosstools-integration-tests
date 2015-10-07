package org.jboss.tools.openshift.ui.bot.test.connection;

import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
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
		explorer.connectToOpenShift2(Datastore.SERVER, Datastore.USERNAME,
				System.getProperty("openshift.password"), false, false, false);
		
		explorer.getOpenShift2Connection(Datastore.USERNAME, Datastore.SERVER);	
	}
	
	@Test
	@RunIf(conditionClass = OSE2CredentialsExist.class)
	public void testCreateConnectionToOpenShiftEnterprise() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.openConnectionShell();
		
		explorer.connectToOpenShift2(Datastore.SERVER, Datastore.USERNAME, 
				System.getProperty("openshift.password"), false, false, true);
		
		explorer.getOpenShift2Connection(Datastore.USERNAME, Datastore.SERVER);
	}	
}
