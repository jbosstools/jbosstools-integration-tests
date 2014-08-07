package org.jboss.tools.openshift.ui.bot.test.ssh;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test capabilities of adding existing SSH key to OpenShift account.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID152AddExistingSSHKeyTest {

	@Test
	public void testAddExistingSSHKey() {
		addExistingSSHKey(Datastore.USERNAME);
	}
	
	public static void addExistingSSHKey(String username) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		explorer.getConnection(username).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.MANAGE_SSH_KEYS).select();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		
		assertTrue("There should not be any SSH keys uploaded yet.",
				new DefaultTable().getItems().size() == 0);
		
		new PushButton(OpenShiftLabel.Button.ADD_SSH_KEY).click();
		
		new DefaultShell(OpenShiftLabel.Shell.ADD_SSH_KEY);
		
		boolean setPath = Display.syncExec(new ResultRunnable<Boolean>() {

			@Override
			public Boolean run() {
				new LabeledText(OpenShiftLabel.TextLabels.NAME).setText(Datastore.SSH_KEY_NAME);
				new LabeledText(OpenShiftLabel.TextLabels.PUB_KEY).getSWTWidget().setText(
						Datastore.SSH_HOME + System.getProperty("file.separator") + Datastore.SSH_KEY_FILENAME +
						".pub");
				return true;
			}
			
		});
		
		assertTrue("Path to the SSH key has not been set up properly", setPath);
		
		new FinishButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		
		assertTrue("There should be only one SSH key uploaded.",
				new DefaultTable().getItems().size() == 1);
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MANAGE_SSH_KEYS), TimePeriod.LONG);
	}
}
