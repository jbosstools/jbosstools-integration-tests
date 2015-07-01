package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
import org.junit.After;
import org.junit.Test;

/**
 * Test creation of an application without server adapter.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID409CreateApplicationWithoutAdapterTest {
	
	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Test
	public void testCreateApplicationWithoutServerAdapter() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, false);
		
		explorer.activate();
		assertTrue("Application " + applicationName + " has not been created", 
				explorer.applicationExists(Datastore.USERNAME, Datastore.DOMAIN, applicationName));
		
		ServersView serversView = new ServersView();
		serversView.open();
		
		try {
			treeViewerHandler.getTreeItem(new DefaultTree(), applicationName + " at OpenShift");
			fail("There is server adapter for application " + applicationName +
					" although there should not be");
		} catch (RedDeerException ex) {
			// pass
		}
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		try {
			projectExplorer.getProject(applicationName);
			// pass
		} catch (EclipseLayerException ex) {
			fail("There is no project of OpenShift application.");
		}
	}
	
	@After
	public void deleteApplication() {
		DeleteApplication deleteApplication = 
				new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		
		deleteApplication.deleteOpenShiftApplication();
		deleteApplication.deleteProject();
	}
}
