package org.jboss.tools.openshift.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.TreeItem;

public class OpenShift3Connection extends AbstractOpenShiftConnection {
	
	public OpenShift3Connection(TreeItem connectionItem) {
		super(connectionItem);
	}

	/**
	 * Gets project of a OpenShift 3 connection with specified user.
	 *
	 * @param projectName projectName attribute of a OpenShift 3 connection or displayed 
	 * 			name of a project if it was provided during the creation time
	 * @return OpenShift project
	 */
	public OpenShiftProject getProject(String projectName) {
		activateOpenShiftExplorerView();
		item.select();
		item.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		return new OpenShiftProject(treeViewerHandler.getTreeItem(item, projectName));
	}
	
	/**
	 * Gets all projects existing on a connection.
	 * 
	 * @return list of all projects for a connection.
	 */
	public List<OpenShiftProject> getAllProjects() {
		List<OpenShiftProject> projects = new ArrayList<OpenShiftProject>();
		activateOpenShiftExplorerView();
		item.select();
		item.expand();
		for (TreeItem treeItem: item.getItems()) {
			projects.add(new OpenShiftProject(treeItem));
		}
		return projects;
	}
	
	/**
	 * Finds out whether a project with specified project name exists or not.
	 * 
	 * @param projectName project name
	 * @return true if project exists, false otherwise
	 */
	public boolean projectExists(String projectName) {
		try {
			getProject(projectName);
			return true;
		} catch (RedDeerException ex) {
			return false;
		}
	}
}
