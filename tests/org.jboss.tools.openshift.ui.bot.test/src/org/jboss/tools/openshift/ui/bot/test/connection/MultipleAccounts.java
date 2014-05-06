package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of having more accounts to OpenShift server in OpenShift explorer
 * 
 * @author mlabuda@redhat.com
 *
 */
public class MultipleAccounts {

	private static final String server = "openshift.redhat.com";
	private static final String username = "equo@mail.muni.cz";
	private static final String password = "rhqetestjbds19";
	
	private int index;
	
	@Before
	public void beforeTest() {
		AbstractWait.sleep(TimePeriod.NORMAL);
	}
	
	@Test
	public void testMultipleAccountsCapabilities() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.openConnectionShell();
		explorer.connectToOpenShift(server, username, password, false);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		// bcs. it is not tracked as job and it takes more time then all jobs are finished
		AbstractWait.sleep(TimePeriod.getCustom(5));
		
		explorer.open();
		
		boolean connectionExist = false;
		for (int i=0; i<new DefaultTree().getItems().size(); i++) {
			if (new DefaultTree().getItems().get(i).getText().split(" ")[0].equals(username)) {
				connectionExist = true;
				index = i;
				break;
			}
		}
		assertTrue("Second connection has not been estabished", connectionExist);
	}
	
	@After
	public void removeAccount() {
		new DefaultTree().getItems().get(index).select();
		new ContextMenu("Remove Connection...").select();;
		
		new WaitUntil(new ShellWithTextIsAvailable("Remove connection"), TimePeriod.NORMAL);
		
		new DefaultShell("Remove connection").setFocus();
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}
