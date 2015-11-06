package org.jboss.tools.openshift.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;

public class OpenShiftProject extends AbstractOpenShiftExplorerItem {
	
	public OpenShiftProject(TreeItem projectItem) {
		super(projectItem);
	}
	
	/**
	 * Gets all resources of specific type for project.
	 * 
	 * @param resourceType resource type
	 * @return list of resources of specified type or empty list if there are no resources
	 */
	public List<OpenShiftResource> getOpenShiftResources(Resource resourceType) {
		List<OpenShiftResource> resources = new ArrayList<OpenShiftResource>();
		TreeItem resourceTypeTreeItem = item.getItem(resourceType.toString());
		resourceTypeTreeItem.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();	
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		resourceTypeTreeItem.select();
		List<TreeItem> resourcesItems = resourceTypeTreeItem.getItems();
		while (resourcesItems != null && resourcesItems.size() == 1 && 
				resourcesItems.get(0).getText().contains("Loading...")) { 
			resourcesItems = resourceTypeTreeItem.getItems();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		if (resourcesItems != null && !resourcesItems.isEmpty()) {
			for (TreeItem resourceItem: resourcesItems) {
				resources.add(new OpenShiftResource(resourceItem));
			}
		}
		
		return resources;
	}
	
	public void delete() {
		refresh();
		item.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_OS_PROJECT).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_OS_PROJECT);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_OS_PROJECT), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
