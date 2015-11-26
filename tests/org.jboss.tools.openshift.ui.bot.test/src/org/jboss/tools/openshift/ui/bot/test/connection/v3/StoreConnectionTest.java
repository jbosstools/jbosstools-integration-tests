package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.SecureStorage;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.junit.Test;

public class StoreConnectionTest {
	
	@Test
	public void testSecureStorageForBasicConnection() {
		testSecureStorage(DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
	}
		
	private void testSecureStorage(String username, String server) {
		SecureStorage.storeOpenShiftPassword(DatastoreOS3.USERNAME, DatastoreOS3.SERVER, 
				ServerType.OPENSHIFT_3);
		
		SecureStorage.verifySecureStorageOfPassword(DatastoreOS3.USERNAME, 
				DatastoreOS3.SERVER.substring(8), true, ServerType.OPENSHIFT_3);
		
		SecureStorage.removeOpenShiftPassword(DatastoreOS3.USERNAME, DatastoreOS3.SERVER, 
				ServerType.OPENSHIFT_3);
		
		SecureStorage.verifySecureStorageOfPassword(DatastoreOS3.USERNAME,
				DatastoreOS3.SERVER.substring(8), false, ServerType.OPENSHIFT_3);
	}
}
