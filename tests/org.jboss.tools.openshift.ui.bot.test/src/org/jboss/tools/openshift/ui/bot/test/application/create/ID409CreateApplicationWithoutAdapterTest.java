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

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.reddeer.condition.v2.OpenShiftApplicationExists;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
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
		
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, false);
		
		explorer.activate();
		
		new WaitUntil(new OpenShiftApplicationExists(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, 
				DatastoreOS2.DOMAIN, applicationName), TimePeriod.LONG);
		
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
		DeleteUtils deleteApplication =	new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, 
				DatastoreOS2.DOMAIN, applicationName, applicationName);
		
		deleteApplication.deleteOpenShiftApplication();
		deleteApplication.deleteProject();
	}
}
