package org.jboss.tools.openshift.ui.bot.test.connection;

import org.jboss.tools.openshift.reddeer.utils.SecureStorage;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
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
		SecureStorage.storePassword(Datastore.USERNAME);
		
		SecureStorage.verifySecureStorageOfPassword(Datastore.USERNAME, 
				Datastore.SERVER.substring(8), true, ServerType.OPENSHIFT_2);
		
		SecureStorage.removePassword(Datastore.USERNAME);
		
		SecureStorage.verifySecureStorageOfPassword(Datastore.USERNAME, 
				Datastore.SERVER.substring(8), false, ServerType.OPENSHIFT_2);
	}	
}
