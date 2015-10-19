package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.Test;

public class ResourcesTest {

	@Test
	public void testResourcesExistence() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		OpenShiftProject project = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, 
				DatastoreOS3.SERVER).getProject(DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		project.expand();
		TreeItem projectItem = project.getTreeItem();
		
		verifyResourceExistence(projectItem, "Build Configs");
		verifyResourceExistence(projectItem, "Builds");
		verifyResourceExistence(projectItem, "Deployment Configs");
		verifyResourceExistence(projectItem, "Image Streams");
		verifyResourceExistence(projectItem, "Pods");
		verifyResourceExistence(projectItem, "Replication Controllers");
		verifyResourceExistence(projectItem, "Routes");
		verifyResourceExistence(projectItem, "Services");
	}
	
	private void verifyResourceExistence(TreeItem projectItem, String resourceLabel) {
		try {
			projectItem.getItem(resourceLabel);
		} catch (RedDeerException ex) {
			fail("Resource " + resourceLabel + " does not exist under a specified project item.");
		}	
	}	
}
