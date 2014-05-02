package org.jboss.tools.openshift.ui.bot.util;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.After;
import org.junit.Test;

/** 
 * Purpose of this class is to be called after all tests - it just remove domain
 * 
 * @author mlabuda
 *
 */
public class CleanUp {

	@Test
	public void test() {
		// NOTHING TO DO
	}
	
	@After
	public void cleanUp() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.close();
		explorer.open();
		
		TreeItem connection = explorer.getConnection();
		connection.select();
		
		connection.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).select();
		new ContextMenu("Delete Domain...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Domain deletion"));
		
		new DefaultShell("Domain deletion").setFocus();
		if (!new CheckBox(0).isChecked()) {
			new CheckBox(0).click();
		}
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
}
