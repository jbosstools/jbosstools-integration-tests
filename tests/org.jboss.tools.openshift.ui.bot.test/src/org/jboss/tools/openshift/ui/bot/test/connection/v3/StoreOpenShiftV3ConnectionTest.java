package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.tools.openshift.reddeer.utils.SecureStorage;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreV3;
import org.junit.Test;

public class StoreOpenShiftV3ConnectionTest {
	
	@Test
	@RunIf(conditionClass = ConnectionCredentialsExists.class)
	public void testSecureStorageForBasicConnection() {
		testSecureStorage(DatastoreV3.OPENSHIFT_USER, DatastoreV3.OPENSHIFT_SERVER);
	}
	
	@Test
	@RunIf(conditionClass = ConnectionCredentialsExists.class)
	public void testSecureStorageForOAuthConnection() {
		testSecureStorage(DatastoreV3.OPENSHIFT_USER2, DatastoreV3.OPENSHIFT_SERVER2);
	}
	
	private void testSecureStorage(String username, String server) {
		SecureStorage.storePassword(username);
		
		SecureStorage.verifySecureStorageOfPassword(username, server.substring(8), true, ServerType.OPENSHIFT_3);
		
		SecureStorage.removePassword(username);
		
		SecureStorage.verifySecureStorageOfPassword(username, server.substring(8), false, ServerType.OPENSHIFT_3);
	}
}
