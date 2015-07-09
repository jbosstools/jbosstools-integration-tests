package org.jboss.tools.openshift.ui.bot.test.application.adapter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.reddeer.condition.BrowserContainsText;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.Templates;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID903ApplicationMarkersTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID905ManageSnapshotsTest;
import org.jboss.tools.openshift.ui.bot.test.application.cartridge.ID601EmbedCartridgeTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID408ApplicationPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID705TailFilesTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID706PortForwardingTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID707HandleEnvironmentVariablesTest;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteApplication;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test capabilities of server adapter handling basic and advanced operations.
 * Similar as on application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID803ServerAdapterHandlingTest {

	private static String applicationName = "eap" + System.currentTimeMillis();
	
	private static OpenShiftExplorerView explorer;
	private static ServersView servers;
	private static TreeItem serverAdapter;
	private static TreeViewerHandler treeViewerHandler;
	
	@BeforeClass
	public static void createApplication() {
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, false, true, true);
		
		explorer = new OpenShiftExplorerView();
		
		treeViewerHandler = TreeViewerHandler.getInstance();
		
		servers = new ServersView();
		servers.open();
		serverAdapter = treeViewerHandler.getTreeItem(new DefaultTree(),
				applicationName + " at OpenShift");
	}
	
	@Test
	public void testServerAdapterShowInBrowser() {
		servers.open();
		serverAdapter.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_BROWSER).select();
		
		try {
			new WaitUntil(new BrowserContainsText("OpenShift"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been shown successfully in browser.");
		}
		
	}
	
	@Test
	public void testServerAdapterTailingFiles() {
		servers.open();
		serverAdapter.select();
		ID705TailFilesTest.tailFilesTest(servers, serverAdapter, false, "OpenShift", 
				OpenShiftLabel.ContextMenu.TAIL_FILES);
	}
	
	@Ignore // failing due to JBIDE-18147
	@Test
	public void testServerAdapterPortForwarding() {
		ID706PortForwardingTest.portForwardingTest(servers, serverAdapter,
				"OpenShift", OpenShiftLabel.ContextMenu.PORT_FORWARD);
	}
	
	@Test
	public void testServerAdapterEnvironmentVariablesManaging() {
		ID707HandleEnvironmentVariablesTest.environmentVariablesHandling(servers, 
				serverAdapter, OpenShiftLabel.Shell.MANAGE_ENV_VARS + applicationName,
				new String[] {"OpenShift", OpenShiftLabel.ContextMenu.EDIT_ENV_VARS},
				new String[] {"OpenShift", OpenShiftLabel.ContextMenu.SHOW_ENV_VARS});	
	}
	
	@Test
	public void testServerAdapterRestartApplication() {
		servers.open();
		serverAdapter.select();
		
		new ContextMenu("OpenShift", OpenShiftLabel.ContextMenu.RESTART_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESTART_APPLICATION),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.RESTART_APPLICATION);
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESTART_APPLICATION),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(Datastore.USERNAME, Datastore.DOMAIN, 
				applicationName, "OpenShift"), TimePeriod.VERY_LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been restarted successfully.");
		}
	}
	
	@Test
	public void testServerAdapterEmbedCartridge() {
		ID601EmbedCartridgeTest.embedCartridge(servers, serverAdapter, applicationName, "OpenShift",
				OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE);
	}
	
	@Test
	public void testApplicationDetails() {
		ID408ApplicationPropertiesTest.applicationDetails(servers, serverAdapter, applicationName, 
				"JBoss Enterprise Application Platform 6 (jbosseap-6)", "OpenShift", 
				OpenShiftLabel.ContextMenu.APPLICATION_DETAILS);
	}
	
	@Test
	public void testApplicationMarkers() {
		ID903ApplicationMarkersTest.markersTest(servers, serverAdapter, applicationName, 
				OpenShiftLabel.ContextMenu.CONFIGURE_MARKERS);
	}
	
	@Test
	public void testManageSnapshots() {
		String[] contextMenuPath = new String[] {"OpenShift", OpenShiftLabel.ContextMenu.SAVE_SNAPSHOT[0],
				OpenShiftLabel.ContextMenu.SAVE_SNAPSHOT[1]};
		ID905ManageSnapshotsTest.manageSnapshotsTest(servers, serverAdapter, applicationName,
				contextMenuPath);
	}
	
	@AfterClass
	public static void deleteProject() {
		deleteApplicationViaServerAdapter();
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).deleteProject();
	}
	
	private static void deleteApplicationViaServerAdapter() {
		servers.open();
		serverAdapter.select();
		new ContextMenu("OpenShift", OpenShiftLabel.ContextMenu.DELETE_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_SERVER_REMOVE),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_SERVER_REMOVE);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_SERVER_REMOVE),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("Application should be removed and no longer visible in OpenShift explorer.",
				explorer.applicationExists(Datastore.USERNAME, Datastore.DOMAIN, applicationName));
		
		servers.open();
		try {
			treeViewerHandler.getTreeItem(new DefaultTree(), applicationName + " at OpenShift");
			fail("Server adapter should not longer be presented in servers view.");
		} catch (RedDeerException ex) {
			// PASS
		}
	}
}
