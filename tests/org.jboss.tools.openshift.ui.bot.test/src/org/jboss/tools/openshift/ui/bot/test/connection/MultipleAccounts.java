package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.jboss.reddeer.eclipse.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
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
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
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
		Iterator<TreeItem> itemIterator = new DefaultTree().getItems().iterator();
		while (itemIterator.hasNext()) {
			if (treeViewerHandler.getNonStyledText(itemIterator.next()).equals(username)) {
				connectionExist = true;
				break;
			}
		}
		
		assertTrue("Second connection has not been estabished", connectionExist);
	}
	
	@After
	public void removeAccount() {
		new DefaultTree().getItems().get(index).select();
		new ContextMenu("Remove Connection...").select();;
		
		new DefaultShell("Remove connection").setFocus();
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}
