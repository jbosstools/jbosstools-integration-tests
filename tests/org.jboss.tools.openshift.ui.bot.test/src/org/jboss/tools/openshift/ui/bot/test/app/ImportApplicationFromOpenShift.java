package org.jboss.tools.openshift.ui.bot.test.app;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImportApplicationFromOpenShift {

	private String DIY_APP = "diyapp" + System.currentTimeMillis();
	
	@Before
	public void createApp() {
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		OpenShiftBotTest.createOpenShiftApplicationWithoutAdapter(DIY_APP, OpenShiftLabel.AppType.DIY);
		
		removeApp();
	}
	
	@Test
	public void importApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem connection = new DefaultTree().getItems().get(0);
		connection.select();
		new ContextMenu(OpenShiftLabel.Labels.REFRESH).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		if (!connection.isExpanded()) {
			connection.doubleClick();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		TreeItem domain = connection.getItems().get(0);
		domain.select();
		if (!domain.isExpanded()) {
			domain.doubleClick();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		domain.getItem(DIY_APP + " " + OpenShiftLabel.AppType.DIY_TREE).select();
		new ContextMenu(OpenShiftLabel.Labels.EXPLORER_IMPORT_APP).select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Import OpenShift Application"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Import OpenShift Application").setFocus();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Publish " + DIY_APP + "?"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Publish " + DIY_APP + "?").setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		ConsoleView console = new ConsoleView();
		assertFalse(console.getConsoleText().isEmpty());
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		assertTrue("Project has not been imported into workspace", 
				projectExplorer.getProjects().get(0).getName().split(" ")[0].equals(DIY_APP));
		
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
		OpenShiftBotTest.deleteOpenShiftApplication(DIY_APP, OpenShiftLabel.AppType.DIY_TREE);
	}
	
	private void removeApp() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		Project project = projectExplorer.getProjects().get(0);
		project.select();
	    new ContextMenu("Delete").select();
		new DefaultShell("Delete Resources");
		if (!new CheckBox(0).isChecked()) {
			new CheckBox(0).click();
		}
		DefaultShell shell = new DefaultShell();
		String deleteShellText = shell.getText();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(deleteShellText),TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}	
}
