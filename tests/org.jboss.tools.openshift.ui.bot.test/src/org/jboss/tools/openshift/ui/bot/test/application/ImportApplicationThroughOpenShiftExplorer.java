package org.jboss.tools.openshift.ui.bot.test.application;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProjectExplorer;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ImportApplicationThroughOpenShiftExplorer {

	private String DIY_APP = "diyapp" + System.currentTimeMillis();
	
	@Before
	public void createApp() {
		new NewApplicationTemplates(false).createApplicationWithoutImportingIntoWorkspace(
				OpenShiftLabel.AppType.DIY, DIY_APP, false, true);
	}
	
	@Test
	public void importApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem connection = new DefaultTree().getItems().get(0);
		connection.select();
		new ContextMenu(OpenShiftLabel.Labels.REFRESH).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		explorer.getApplication(DIY_APP).select();
		
		new ContextMenu(OpenShiftLabel.Labels.EXPLORER_IMPORT_APP).select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Import OpenShift Application"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Import OpenShift Application").setFocus();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new NewApplicationWizard().postCreateSteps(DIY_APP, false, true);
		
		ConsoleView console = new ConsoleView();
		assertFalse(console.getConsoleText().isEmpty());
		
		CustomizedProjectExplorer projectExplorer = new CustomizedProjectExplorer();
		projectExplorer.open();
		assertTrue("Project has not been imported into workspace", 
				projectExplorer.containsProject(DIY_APP));
		
		ServersView serverView = new ServersView();
		serverView.open();
		
		List<TreeItem> servers = new DefaultTree().getItems();
		TreeItem server = null;
		for (TreeItem item: servers) {
			String serverName = item.getText().split(" ")[0];
			if (serverName.equals(DIY_APP)) {
				server = item;
				break;
			}
		}
		
		assertTrue("Server adapter has not been created for imported application ",server != null);
	}
	
	@After
	public void deleteApp() {
		new DeleteApplication(DIY_APP).perform();
	}
}
