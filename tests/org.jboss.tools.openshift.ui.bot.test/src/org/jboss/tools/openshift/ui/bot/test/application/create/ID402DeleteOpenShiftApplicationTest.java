/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.handler.TreeViewerHandler;
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
		ServersView2 serversView = new ServersView2();
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
