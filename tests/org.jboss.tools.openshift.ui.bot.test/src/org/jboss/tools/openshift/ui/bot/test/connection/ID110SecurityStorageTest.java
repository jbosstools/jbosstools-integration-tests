package org.jboss.tools.openshift.ui.bot.test.connection;

import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.SecureStorage;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.junit.Test;

/**
 * Test capabilities of secured storage for OpenShift Tools plugin.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID110SecurityStorageTest {
	
	@Test
	public void testPasswordsSecuredStorage() {
		SecureStorage.storeOpenShiftPassword(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, ServerType.OPENSHIFT_2);
		
		SecureStorage.verifySecureStorageOfPassword(DatastoreOS2.USERNAME, 
				DatastoreOS2.SERVER.substring(8), true, ServerType.OPENSHIFT_2);
		
		SecureStorage.removeOpenShiftPassword(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, ServerType.OPENSHIFT_2);
		
		SecureStorage.verifySecureStorageOfPassword(DatastoreOS2.USERNAME, 
				DatastoreOS2.SERVER.substring(8), false, ServerType.OPENSHIFT_2);
	}	
}
