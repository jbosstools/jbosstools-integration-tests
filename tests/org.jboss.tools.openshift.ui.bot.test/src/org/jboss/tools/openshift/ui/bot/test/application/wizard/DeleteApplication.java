package org.jboss.tools.openshift.ui.bot.test.application.wizard;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.common.logging.Logger;
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
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProjectExplorer;
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

	private Logger logger = new Logger(DeleteApplication.class);
	
	private String appName;
	
	/**
	 * Prepare application to be deleted.
	 * 
	 * @param appName application Name
	 * @param appTypeTree use OpenShiftLabel.AppType.*Tree type
	 */
	public DeleteApplication(String appName) {
		this.appName = appName;
	}
	
	/**
	 * Delete project, server adapter and deployed OpenShift application 
	 */
	public void perform() {
		deleteServerAdapter();
		deleteOpenShiftApplication();
		deleteProject();
	}

	public void deleteServerAdapter() {
		ServersView serversView = new ServersView();
		serversView.open();
		
		List<TreeItem> servers = new DefaultTree().getItems();
		TreeItem server = null;
		for (TreeItem item: servers) {
			String serverName = item.getText().split(" ")[0];
			if (serverName.equals(appName)) {
				server = item;
				break;
			}
		}	
		
		server.select();
		new ContextMenu("Delete").select();	
		new DefaultShell("Delete Server").setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	public void deleteOpenShiftApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem connection = explorer.getConnection();
		connection.select();
		new ContextMenu(OpenShiftLabel.Labels.REFRESH).select();

		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);	
		
		explorer.selectApplication(appName);
		new ContextMenu(OpenShiftLabel.Labels.DELETE_APP).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_APP), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_APP).setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		// bcs. sometime it could be removed later as the job is finished
		AbstractWait.sleep(TimePeriod.NORMAL);
		
		assertFalse("Application still present in the OpenShift Explorer!",
				explorer.applicationExists(appName));
	}
	
	public void deleteProject() {
		CustomizedProjectExplorer projectExplorer = new CustomizedProjectExplorer();
		projectExplorer.open();
		
		boolean success = projectExplorer.getProject(appName).delete();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		// It is eclipse issue that deleting does not work. If test get to this place
		// it has passed although delete app. from workspace didn't worked.
		if (!success) {
			logger.error("Project " + appName + " has not been deleted.");
		}
	}
}
