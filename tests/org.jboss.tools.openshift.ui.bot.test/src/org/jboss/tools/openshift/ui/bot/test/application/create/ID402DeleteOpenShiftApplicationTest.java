package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test deletion of an application from OpenShift and from Servers view.
 *  
 * @author mlabuda@redhat.com
 *
 */
public class ID402DeleteOpenShiftApplicationTest {

	@Test
	public void testDeleteOpenShiftApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		ServersView serversView = new ServersView();
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		String applicationName = ID401CreateNewApplicationViaExplorerTest.applicationName;
		DeleteUtils deleteApplication =  new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, applicationName, applicationName);
		
		deleteApplication.deleteOpenShiftApplication();
		try {
			explorer.open();
			explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN). 
				getApplication(ID401CreateNewApplicationViaExplorerTest.applicationName);
			fail("OpenShift application has not been deleted.");
		} catch (JFaceLayerException ex) {
			// PASS
		}
		
		
		deleteApplication.deleteServerAdapter();
		try {
			serversView.open();
			treeViewerHandler.getTreeItem(new DefaultTree(), applicationName + " at OpenShift");
			fail("Server adapter for an application should not be presented in Servers view.");
		} catch (RedDeerException ex) {
			// PASS
		}
		
		assertTrue("Project has not been deleted successfully.",
				new ProjectExplorer().containsProject(applicationName));
	}
	
}
