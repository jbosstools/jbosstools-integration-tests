package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.exception.RedDeerException;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
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
		DeleteApplication deleteApplication =  new DeleteApplication(Datastore.USERNAME,
				Datastore.DOMAIN, applicationName);
		
		deleteApplication.deleteOpenShiftApplication();
		try {
			explorer.open();
			explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN, 
				ID401CreateNewApplicationViaExplorerTest.applicationName);
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
				deleteApplication.deleteProject());
	}
	
}
