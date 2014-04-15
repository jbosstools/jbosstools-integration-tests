package org.jboss.tools.openshift.ui.bot.test.application.wizard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TreeItem;
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

/**
 * Delete an application. It is possible to remove whole application by invoking method
 * perform or just remove specified part of an application - server adapter or application
 * or deployed application on OpenShift.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DeleteApplication {

	private String appName;
	private String appTypeTree;
	
	/**
	 * Prepare application to be deleted.
	 * 
	 * @param appName application Name
	 * @param appTypeTree use OpenShiftLabel.AppType.*Tree type
	 */
	public DeleteApplication(String appName, String appTypeTree) {
		this.appName = appName;
		this.appTypeTree = appTypeTree;
	}
	
	/**
	 * Delete project, server adapter and deployed OpenShift application 
	 */
	public void perform() {
		deleteServerAdapter();
		deleteOpenShiftApplication();
	//	deleteProject();
	}

	public void deleteServerAdapter() {
		ServersView serversView = new ServersView();
		serversView.open();
		
		List<TreeItem> servers = new DefaultTree().getItems();
		TreeItem server = null;
		for (TreeItem item: servers) {
			String serverName = item.getText().split("\\[")[0];
			if (serverName.equals(appName + " at OpenShift  ")) {
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
	
	public void deleteOpenShiftApplication() {
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
		domainItem.getItem(appName + " " + appTypeTree).select();
		new ContextMenu(OpenShiftLabel.Labels.DELETE_APP).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_APP), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_APP).setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		// bcs. sometime it could be removed later as the job is finished
		AbstractWait.sleep(TimePeriod.SHORT);
		
		assertTrue("Application still present in the OpenShift Explorer!",
				domainItem.getItems().size() == applicationCount - 1);
	}
	
	public void deleteProject() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		List<Project> projects = projectExplorer.getProjects();
		for (Project project: projects) {
			// Split required bcs of git details in project name
			if (project.getName().split(" ")[0].equals(appName)) {
				project.delete(true);
				new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
				break;
			}
		}
		
		assertFalse("The project still exists!", projectExplorer.containsProject(appName));
	}
}
