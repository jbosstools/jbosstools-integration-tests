package org.jboss.tools.openshift.ui.utils;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;

/**
 * Delete an application. It is possible to remove whole application by invoking method
 * perform or just remove specified part of an application - server adapter, application
 * or deployed application on OpenShift.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DeleteApplication {

	private Logger logger = new Logger(DeleteApplication.class);
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	private String username;
	private String domain;
	private String appName;
	private String project;
	
	/**
	 * Prepares application to be deleted. Project name is considered to be same
	 * as the application name. If names are different use constructor with project name.
	 * 
	 * @param username user name
	 * @param domain domain name
	 * @param appName application Name
	 */
	public DeleteApplication(String username, String domain, String appName) {
		this.username = username;
		this.domain = domain;
		this.appName = appName;
		project = appName;
	}
	
	/**
	 * Prepares application to be deleted.
	 * 
	 * @param username user name
	 * @param domain domain name
	 * @param appName application Name
	 * @param project project name
	 */
	public DeleteApplication(String username, String domain, String appName, String project) {
		this.username = username;
		this.domain = domain;
		this.appName = appName;
		this.project = project;
	}
	
	/**
	 * Deletes project, server adapter and deployed OpenShift application.
	 */
	public void perform() {
		deleteServerAdapter();
		deleteOpenShiftApplication();
		deleteProject();
	}

	public void deleteServerAdapter() {
		ServersView serversView = new ServersView();
		serversView.open();
		
		TreeItem server = treeViewerHandler.getTreeItem(new DefaultTree(), appName + " at OpenShift");
		server.select();
		
		new ContextMenu("Delete").select();	
		new DefaultShell("Delete Server").setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	public void deleteOpenShiftApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.selectApplication(username, domain, appName);
		new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_APP), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_APP).setFocus();
		new OkButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		// bcs. sometime it could be removed later as the job is finished and 7 is magic
		AbstractWait.sleep(TimePeriod.getCustom(7));
	}
	
	public boolean deleteProject() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		try {
			projectExplorer.getProject(project).delete(true);
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			return true;
		} catch (RedDeerException ex) {
			logger.error("Project " + project + " has not been deleted.");
			return false;
		}
	}
}
