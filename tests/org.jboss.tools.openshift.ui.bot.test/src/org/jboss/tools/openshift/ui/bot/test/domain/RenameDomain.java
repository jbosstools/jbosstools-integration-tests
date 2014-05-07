package org.jboss.tools.openshift.ui.bot.test.domain;

import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.Test;

/**
 * Test capabilites of renaming domain name.
 * 
 * @author mlabuda@redhat.com
 */
public class RenameDomain {

	@Test
	public void canRenameDomain() {
		renameDomain();
	}
	
	private void renameDomain() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		TreeItem connection = explorer.getConnection();
		connection.select();
		
		new ContextMenu(OpenShiftLabel.Labels.MANAGE_DOMAINS).select();

		new WaitUntil(new ShellWithTextIsAvailable("Domains"), TimePeriod.LONG);
		
		new DefaultShell("Domains").setFocus();
		
		String oldDomainName = new DefaultTable(0).getItem(0).getText();
		String newDomainName = oldDomainName.substring(0, oldDomainName.length() - 2);
		
		new DefaultTable(0).getItem(0).select();
		new PushButton("Edit...").click();
		new DefaultText(0).setText(newDomainName);
		new PushButton("Finish").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		Logger logger = new Logger(this.getClass());
		
		logger.info("*** OpenShift RedDeer Tests: Domain renamed. ***");

		new WaitUntil(new ShellWithTextIsAvailable("Domains"), TimePeriod.LONG);
		
		new DefaultShell("Domains").setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.select();
		new ContextMenu(OpenShiftLabel.Labels.REFRESH).select();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

}
