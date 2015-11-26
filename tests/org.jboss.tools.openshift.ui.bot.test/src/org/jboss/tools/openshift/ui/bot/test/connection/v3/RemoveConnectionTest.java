package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.AuthenticationMethod;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.junit.After;
import org.junit.Test;

public class RemoveConnectionTest {

	private boolean connectionRemoved = false;
	private OpenShiftExplorerView explorer = new OpenShiftExplorerView();
	
	@Test
	public void testRemoveConnection() {
		explorer.open();
		
		explorer.getOpenShift3Connection().select();
		new ContextMenu(OpenShiftLabel.ContextMenu.REMOVE_CONNECTION).select();
		
		new DefaultShell(OpenShiftLabel.Shell.REMOVE_CONNECTION);
		new OkButton().click();
		
		connectionRemoved = true;
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.REMOVE_CONNECTION),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("Connection is still presented in OpenShift explorer",
				explorer.connectionExists(DatastoreOS3.USERNAME, DatastoreOS3.SERVER));
	}
	
	@After
	public void testRecreateConnection() {
		explorer.open();
		if (connectionRemoved) {
			explorer.openConnectionShellViaToolItem();
			if (DatastoreOS3.AUTH_METHOD.equals(AuthenticationMethod.BASIC)) {
				explorer.connectToOpenShift(DatastoreOS3.SERVER, DatastoreOS3.USERNAME, DatastoreOS3.PASSWORD, 
						false, false, ServerType.OPENSHIFT_3, AuthenticationMethod.BASIC, false);
			} else if (DatastoreOS3.AUTH_METHOD.equals(AuthenticationMethod.OAUTH)) {
				explorer.connectToOpenShift(DatastoreOS3.SERVER, DatastoreOS3.USERNAME, DatastoreOS3.TOKEN, false,
						false, ServerType.OPENSHIFT_3, AuthenticationMethod.OAUTH, false);
			}
		}
	}
}
