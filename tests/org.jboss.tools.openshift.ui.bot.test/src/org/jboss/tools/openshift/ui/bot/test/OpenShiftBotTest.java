package org.jboss.tools.openshift.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;

/**
 * Base class for OpenShift SWTBot Tests
 * 
 * @author mlabuda, sbunciak
 * 
 */
public class OpenShiftBotTest {

	private static Logger logger = new Logger(OpenShiftBotTest.class);
	
	public static void createOpenShiftApplication(final String APP_NAME,
			final String APP_TYPE) {

		try {
			createOpenShiftApplicationScaling(APP_NAME, APP_TYPE, false, true);
		} catch (OpenShiftTestException e) {
			logger.error("*** OpenShift Endpoint failure. ***", e);
			System.exit(1);
		}

	}

	public static void createScaledOpenShiftApplication(final String APP_NAME,
			final String APP_TYPE) {

		try {
			createOpenShiftApplicationScaling(APP_NAME, APP_TYPE, true, true);
		} catch (OpenShiftTestException e) {
			logger.error("*** OpenShift Endpoint failure. ***", e);
			System.exit(1);
		}
	}
	
	public static void createOpenShiftApplicationWithoutAdapter(final String APP_NAME,
			final String APP_TYPE) {

		try {
			createOpenShiftApplicationScaling(APP_NAME, APP_TYPE, false, false);
		} catch (OpenShiftTestException e) {
			logger.error("*** OpenShift Endpoint failure. ***", e);
			System.exit(1);
		}
	}

	// assumes proper setup of SSH keys
	public static void createOpenShiftApplicationScaling(final String APP_NAME,
			final String APP_TYPE, final boolean scaling, final boolean createAdapter)
			throws OpenShiftTestException {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();

		DefaultTree connection = new DefaultTree(0);
		connection.setFocus();
		connection.getItems().get(0).expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).select();
		new ContextMenu("New", "Application...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("New OpenShift Application"), TimePeriod.LONG);
		
		new DefaultShell("New OpenShift Application").setFocus();
		new LabeledText("Name:").setText(APP_NAME);
		logger.info("*** OpenShift RedDeer Tests: Application name set. ***");
		new DefaultCombo(1).setSelection(APP_TYPE);
		logger.info("*** OpenShift RedDeer Tests: Application type selected. ***");
		if (scaling) {
			new CheckBox(1).click();
		}
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		// create server adapter?
		if (!createAdapter) {
			if (new CheckBox(1).isChecked()) {
				new CheckBox(1).click();
			}
		}	
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		logger.info("*** OpenShift RedDeer Tests: Application creation started. ***");

		// workaround for 'embedding DYI' 
		// scalable apps and Jenkins app
		// FAIL ON ONLINE bcs. there is no shell for scalable app.
		// JBIDE 16040 
		if (APP_TYPE.equals(OpenShiftLabel.AppType.DIY) || 
				APP_TYPE.equals(OpenShiftLabel.AppType.JENKINS) ||
						scaling == true) {
			new WaitUntil(new ShellWithTextIsAvailable("Embedded Cartridges"), TimePeriod.VERY_LONG);
			new DefaultShell("Embedded Cartridges").setFocus();
			new PushButton(OpenShiftLabel.Button.OK).click();
		}
		
		new WaitUntil(new ShellWithTextIsAvailable("Question"), TimePeriod.LONG);
		DefaultShell question = new DefaultShell("Question");
		question.setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		// publish changes
		if (createAdapter) {
			new WaitUntil(new ShellWithTextIsAvailable("Publish " + APP_NAME + "?"), TimePeriod.LONG);
			new DefaultShell("Publish " + APP_NAME + "?").setFocus();
			new PushButton(OpenShiftLabel.Button.YES).click();
			
			new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

			// check successful build after auto git push
			ConsoleView consoleView = new ConsoleView();
			assertTrue(!consoleView.getConsoleText().isEmpty());

			if (!consoleView.getConsoleText().contains("BUILD SUCCESS")
					&& APP_TYPE.contains("JBoss")) {
				logger.error("*** OpenShift RedDeer Tests: OpenShift build output does not contain succesfull maven build. ***");
			}
			
			ServersView serverView = new ServersView();
			serverView.open();
			
			// WORKAROUND bcs SERVER IN REDDEER DOES NOT WORK
			List<TreeItem> servers = new DefaultTree().getItems();
			TreeItem server = null;
			for (TreeItem item: servers) {
				String serverName = item.getText().split("\\[")[0];
				if (serverName.equals(APP_NAME + " at OpenShift  ")) {
					server = item;
					break;
				}
			}
			
			server.select();
			new ContextMenu("Publish").select();
			
			new WaitUntil(new ShellWithTextIsAvailable("Publish " + APP_NAME + "?"), TimePeriod.LONG);	
			new DefaultShell("Publish " + APP_NAME + "?").setFocus();
			new PushButton(OpenShiftLabel.Button.YES).click();
		
			new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
			
			serverView.open();
			assertTrue("Adapter does not exists", 
					serverView.getServer(APP_NAME + " at OpenShift") != null);
			logger.info("*** OpenShift RedDeer Tests: OpenShift Server Adapter created. ***");
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public static void deleteOpenShiftApplication(final String APP_NAME,
			final String APP_TYPE) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem connection = new DefaultTree().getItems().get(0);
		connection.select();
		new ContextMenu(OpenShiftLabel.Labels.REFRESH).select();

		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);	
		
		connection.select();
		if (!connection.isExpanded()) {
			connection.doubleClick();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		TreeItem domainItem = connection.getItems().get(0);
		domainItem.select();
		if (!domainItem.isExpanded()) {
			domainItem.doubleClick();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		int applicationCount = domainItem.getItems().size();
		domainItem.getItem(APP_NAME + " " + APP_TYPE).select();
		new ContextMenu(OpenShiftLabel.Labels.DELETE_APP).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_APP), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_APP).setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		assertTrue("Application still present in the OpenShift Explorer!",
				domainItem.getItems().size() == applicationCount - 1);

		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		List<Project> projects = projectExplorer.getProjects();
		for (Project project: projects) {
			project.delete(true);
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
		
		assertFalse("The project still exists!",
				projectExplorer.getProjects().size() > 0);

		ServersView serversView = new ServersView();
		serversView.open();
		
		// Workaround bcs. of reddeer server deletion does not work on openshift adapters
		// soon (hopefuly) will be used:
		// serversView.getServer(APP_NAME + " at OpenShift").delete();
		List<TreeItem> servers = new DefaultTree().getItems();
		TreeItem server = null;
		for (TreeItem item: servers) {
			String serverName = item.getText().split("\\[")[0];
			if (serverName.equals(APP_NAME + " at OpenShift  ")) {
				server = item;
				break;
			}
		}	
		
		server.select();
		new ContextMenu("Delete").select();	
		new WaitUntil(new ShellWithTextIsAvailable("Delete Server"), TimePeriod.NORMAL);
		new DefaultShell("Delete Server").setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

}
