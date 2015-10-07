package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreV3;
import org.junit.Test;

public class CreateNewOpenShiftv3ConnectionTest {
	
	@Test
	@RunIf(conditionClass = ConnectionCredentialsExists.class)
	public void testCreateNewV3BasicConnection() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.openConnectionShell();
		try {
			explorer.connectToOpenShift3Basic(DatastoreV3.OPENSHIFT_SERVER, DatastoreV3.OPENSHIFT_USERNAME, 
					DatastoreV3.OPENSHIFT_PASSWORD, false, false);
		} catch (RedDeerException ex) {
			fail("Creating an OpenShift v3 basic connection failed.");
		}
		
		assertTrue("Connection does not exist in OpenShift Explorer view", 
				explorer.connectionExists(DatastoreV3.OPENSHIFT_USERNAME));
	}
	
	@Test
	@RunIf(conditionClass = ConnectionCredentialsExists.class)
	public void testCreateNewV3OAuthConnection() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.openConnectionShell();
		try {
			explorer.connectToOpenShift3OAuth(DatastoreV3.OPENSHIFT_SERVER2, DatastoreV3.OPENSHIFT_TOKEN2, false, false);
		} catch (RedDeerException ex) {
			fail("Creating an OpenShift v3 basic connection failed.");
		}
		
		assertTrue("Connection does not exist in OpenShift Explorer view", 
				explorer.connectionExists(DatastoreV3.OPENSHIFT_USERNAME));
	}
}
