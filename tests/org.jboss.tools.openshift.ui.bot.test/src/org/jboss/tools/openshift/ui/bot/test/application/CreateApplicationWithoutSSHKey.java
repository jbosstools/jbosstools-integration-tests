package org.jboss.tools.openshift.ui.bot.test.application;

import static org.junit.Assert.fail;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.connection.ManageSSH;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateApplicationWithoutSSHKey {

	private String SSH_KEY_NAME = "newid" + System.currentTimeMillis();
	
	@Before
	public void removeAllSSHKeys() {
		ManageSSH.openSSHShell();
		ManageSSH.removeAllKeys();
		ManageSSH.closeSSHShell();
	}
	
	@Test
	public void tryCreateApplicationWithoutSSHKey() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getConnection().select();
		
		new ContextMenu("New", "Application...").select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("No SSH Keys"), TimePeriod.NORMAL);
			// pass
		} catch (WaitTimeoutExpiredException ex) {
			fail("There is no SSH key. You should not be able to create app.");
		}
		
		new DefaultShell("No SSH Keys").setFocus();
		new PushButton(OpenShiftLabel.Button.CANCEL).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void recreateSSHKey() {
		ManageSSH.openSSHShell();
		ManageSSH.createNewSSHKey(SSH_KEY_NAME);
		ManageSSH.closeSSHShell();
	}
	
}
