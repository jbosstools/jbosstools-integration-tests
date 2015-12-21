package org.jboss.tools.openshift.ui.bot.test.application.adapter;

import static org.junit.Assert.assertTrue;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

/**
 * Test capabilities of switching project deployment location.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID801SwitchProjectDeploymentTest extends IDXXXCreateTestingApplication {

	@Test
	public void testSwitchDeployment() {
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		ServersView servers = new ServersView();
		servers.open();
		
		TreeItem serverAdapter = treeViewerHandler.getTreeItem(new DefaultTree(), 
				applicationName + OpenShiftLabel.Others.getOS2ServerAdapterAppendix());
		serverAdapter.select();		
		
		new ContextMenu("Properties").select();
		
		// Shell has in name [Started] sometimes, hard to say when
		DefaultShell serverAdapterShell = null;
		try {
			serverAdapterShell = new DefaultShell(new StringContains("Properties for " + applicationName));
		} catch(Exception ex) {
			serverAdapterShell = new DefaultShell("Properties for " + applicationName +
					OpenShiftLabel.Others.getOS2ServerAdapterAppendix());
		} 
		String title = serverAdapterShell.getText();
		
		new PushButton("Switch Location").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		assertTrue("Location was not correctly switched to Servers.",
			new DefaultLabel(9).getText().equals("/Servers/" +
				applicationName + " at OpenShift 2.server"));
		
		new PushButton("Switch Location").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		assertTrue("Location was not switched to default.",
				new DefaultLabel(9).getText().equals("[workspace metadata]"));
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(title), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}
