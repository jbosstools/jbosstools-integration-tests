package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.fail;


import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;
import org.junit.Test;

public class ResourcesTest {

	@Test
	public void testResourcesExistence() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		OpenShiftProject project = explorer.getOpenShift3Connection().getProject();
		project.expand();
		TreeItem projectItem = project.getTreeItem();
		
		verifyResourceExistence(projectItem, Resource.BUILD_CONFIG);
		verifyResourceExistence(projectItem, Resource.BUILD);
		verifyResourceExistence(projectItem, Resource.DEPLOYMENT_CONFIG);
		verifyResourceExistence(projectItem, Resource.IMAGE_STREAM);
		verifyResourceExistence(projectItem, Resource.POD);
		verifyResourceExistence(projectItem, Resource.REPLICATION_CONTROLLER);
		verifyResourceExistence(projectItem, Resource.ROUTE);
		verifyResourceExistence(projectItem, Resource.SERVICE); 
	}
	
	private void verifyResourceExistence(TreeItem projectItem, Resource resource) {
		try {
			projectItem.getItem(resource.toString());
		} catch (RedDeerException ex) {
			fail("Resource " + resource.toString() + " does not exist under a specified project item.");
		}	
	}	
}
