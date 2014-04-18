package org.jboss.tools.openshift.ui.bot.test.openshiftexplorer;

import java.util.Date;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.Test;

public class ManageSSH {

	public static final String SSH_KEY_NAME = "id" + new Date().getTime();

	@Test
	public void canManageSSHKeys() {
		openSSHShell();
		
		// if there exist some keys
		removeAllKeys();

		createNewSSHKey();

		closeSSHShell();
	}

	private void openSSHShell() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		TreeItem connection = explorer.getConnection();
		connection.select();
		
		ContextMenu menu = new ContextMenu(OpenShiftLabel.Labels.MANAGE_SSH_KEYS);
		menu.select();		
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		new WaitUntil(new ShellWithTextIsAvailable("Manage SSH Keys"), TimePeriod.LONG);
		
		new DefaultShell("Manage SSH Keys").setFocus();
		new PushButton("Refresh...").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	private void removeAllKeys() {
		new DefaultShell("Manage SSH Keys").setFocus();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new PushButton("Refresh...").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		// bcs. of not accessible table SOMETIMES
		AbstractWait.sleep(TimePeriod.NORMAL);
		
		while (new DefaultTable().getItems().size() > 0) {
			new DefaultTable().getItems().get(0).select();
			new WaitUntil(new ButtonWithTextIsActive(new PushButton("Remove...")), TimePeriod.LONG);
			new PushButton("Remove...").click();
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			
			new PushButton(OpenShiftLabel.Button.OK).click();
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
	}
		
	private void createNewSSHKey() {
		new PushButton("New...").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("New SSH Key"), TimePeriod.LONG);
		
		new DefaultShell("New SSH Key").setFocus();
		
		new DefaultText(0).setText(SSH_KEY_NAME);
		new DefaultText(2).setText("openshift_id");

		new PushButton(OpenShiftLabel.Button.FINISH).click();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void closeSSHShell() {
		new DefaultShell("Manage SSH Keys").setFocus();
		
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}