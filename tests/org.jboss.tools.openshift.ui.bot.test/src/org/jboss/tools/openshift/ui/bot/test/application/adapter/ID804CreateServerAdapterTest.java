package org.jboss.tools.openshift.ui.bot.test.application.adapter;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.Templates;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test capabilities of creating a server adapter via servers view and OpenShiftExplorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID804CreateServerAdapterTest {

	private static String applicationName = "diy" + System.currentTimeMillis();
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	@BeforeClass
	public static void createApplication() {
		new Templates(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, false);
	}
	
	@Test
	public void createAdapterViaServersView() {
		ServersView servers = new ServersView();
		servers.open();
		
		try {
			new DefaultTree();
			new ContextMenu("New", "Server").select();
		} catch (CoreLayerException ex) {
			// There is no server, so create server via link
			new DefaultLink("No servers are available. Click this link to create a new server...").click();
		}
		
		new DefaultShell("New Server");
		
		new DefaultTreeItem("OpenShift", "OpenShift Server Adapter").select();
		
		new WaitUntil(new ButtonWithTextIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();
		
		for (String connection: new LabeledCombo("Connection:").getItems()) {
			if (connection.contains(Datastore.USERNAME)) {
				new LabeledCombo("Connection:").setSelection(connection);
				break;
			}
		}
		
		new LabeledCombo("Application Name:").setSelection(applicationName);
		new LabeledCombo("Deploy Project:").setSelection(applicationName);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("New Server"), TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		
		TreeItem adapter = null;
		try {
			adapter = treeViewerHandler.getTreeItem(new DefaultTree(), applicationName + " at OpenShift");
		} catch (RedDeerException ex) {
			fail("Server adapter has not been created.");
		}
		
		deleteAdapter(adapter);
	}
	
	@Test
	public void createAdapterViaExplorer() {
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(Datastore.USERNAME, Datastore.SERVER).getDomain(Datastore.DOMAIN).
			getApplication(applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_SERVER_ADAPTER).select();
		
		new WaitUntil(new ShellWithTextIsAvailable("New Server"), TimePeriod.LONG);

		new DefaultShell("New Server");
		
		new NextButton().click();
		
		new WaitUntil(new ButtonWithTextIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("New Server"), TimePeriod.LONG);
		
		ServersView servers = new ServersView();
		servers.open();
		
		TreeItem adapter = null;
		try {
			adapter = treeViewerHandler.getTreeItem(new DefaultTree(), 
					applicationName + " at OpenShift");
		} catch (RedDeerException ex) {
			fail("Server adapter has not been created successfully.");
		}
		
		deleteAdapter(adapter);
	}
	
	private void deleteAdapter(TreeItem adapter) {
		adapter.select();
		
		new ContextMenu("Delete").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Delete Server"), TimePeriod.NORMAL);
		
		new DefaultShell("Delete Server");
		new OkButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@AfterClass
	public static void deleteApplication() {
		DeleteUtils deleteApplication = 
				new DeleteUtils(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, 
						applicationName, applicationName);
		deleteApplication.deleteProject();
		deleteApplication.deleteOpenShiftApplication();
	}
}
