package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of removing domain.
 * 
 * @author mlabuda@redhat.com
 */
public class DeleteDomain {

	@Test
	public void canDestroyDomain() {
		destroyDomain(false);
	}
	
	@After
	public void recreateDomain() {
		CreateDomain.createDomain(false, false);
	}
	
	public static void destroyDomain(boolean multipleDomain) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		TreeItem connection = explorer.getConnection();
		connection.select();
		
		new ContextMenu(OpenShiftLabel.Labels.REFRESH).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.select();
		new ContextMenu(OpenShiftLabel.Labels.MANAGE_DOMAINS).select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Domains"), TimePeriod.LONG);
		
		new DefaultShell("Domains").setFocus();

		removeDomain();
		
		new DefaultShell("Domains").setFocus();
		
		if (multipleDomain) {
			removeDomain();
			new DefaultShell("Domains").setFocus();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertTrue("*** Domain has not been deleted. ***", new DefaultTable(0).rowCount() == 0);
		
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private static void removeDomain() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new DefaultTable(0).select(0);
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton("Remove...")), TimePeriod.LONG);
		
		new PushButton("Remove...").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Domain deletion"), TimePeriod.LONG);
		
		new DefaultShell("Domain deletion").setFocus();
		if (!new CheckBox(0).isChecked()) {
			new CheckBox(0).click();
		}
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

}
