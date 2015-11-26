package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.AuthenticationMethod;
import org.junit.Test;

public class CreateNewConnectionTest {
	
	@Test
	@RunIf(conditionClass = ConnectionCredentialsExists.class)
	public void testCreateNewV3BasicConnection() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		DatastoreOS3.AUTH_METHOD = AuthenticationMethod.BASIC;
		
		explorer.openConnectionShell();
		try {
			explorer.connectToOpenShift3Basic(DatastoreOS3.SERVER, DatastoreOS3.USERNAME, 
					DatastoreOS3.PASSWORD, false, false);
		} catch (RedDeerException ex) {
			fail("Creating an OpenShift v3 basic connection failed.");
		}
		
		assertTrue("Connection does not exist in OpenShift Explorer view", 
				explorer.connectionExists(DatastoreOS3.USERNAME));
	}
	
	@Test
	@RunIf(conditionClass = ConnectionCredentialsExists.class)
	public void testCreateNewV3OAuthConnection() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		DatastoreOS3.AUTH_METHOD = AuthenticationMethod.OAUTH;
		
		explorer.openConnectionShell();
		try {
			explorer.connectToOpenShift3OAuth(DatastoreOS3.SERVER, DatastoreOS3.TOKEN, false, false);
		} catch (RedDeerException ex) {
			fail("Creating an OpenShift v3 basic connection failed." + ex.getCause());
		}
	
		assertTrue("Connection does not exist in OpenShift Explorer view", 
				explorer.connectionExists(DatastoreOS3.USERNAME));
	}
}
