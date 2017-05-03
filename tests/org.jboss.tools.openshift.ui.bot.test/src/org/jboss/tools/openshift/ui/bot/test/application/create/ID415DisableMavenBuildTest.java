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

import java.util.List;

import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.ui.views.navigator.ResourceNavigator;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.openshift.reddeer.exception.OpenShiftToolsException;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.junit.After;
import org.junit.Test;


/**
 * Test capabilities of disabling maven build while creating a new application and verify that
 * this marker has been added.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID415DisableMavenBuildTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	
	@Test
	public void testCreatedApplicationHasSkipMvnBuildMarker() {
		NewOpenShift2ApplicationWizard	wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME,
				DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_EAP,
				applicationName, false, true, false, true, null, null, true, null, null, null, (String[]) null);
		wizard.postCreateSteps(false);
		
		assertTrue("There is no skip_maven_build marker under EAP project in Navigator.", 
				getOpenShiftMarker(applicationName, "skip_maven_build") != null);
	}
	
	public static TreeItem getOpenShiftMarker(String projectName, String marker) {
		ProjectItem project = getEAPProjectInNavigator(projectName);
		if (project == null) {
			throw new OpenShiftToolsException("There is no project with name " + projectName);
		}
		for (TreeItem treeItemL1: project.getTreeItem().getItems()) {
			if (treeItemL1.getText().contains(".openshift")) {
				for (TreeItem treeItemL2: treeItemL1.getItems()) {
					if (treeItemL2.getText().contains("markers")) {
						for (TreeItem treeItemL3: treeItemL2.getItems()) {
							if (treeItemL3.getText().contains(marker)) {
								return treeItemL3;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private static ProjectItem getEAPProjectInNavigator(String name) {
		ResourceNavigator navigator = new ResourceNavigator();
		navigator.open();
		List<ProjectItem> navigatorProjects = navigator.getProjectItems();
		for (ProjectItem explorerItem: navigatorProjects) {
			if (explorerItem.getText().contains(name)) {
				return explorerItem;
			}
		}
		return null;
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				applicationName, applicationName).perform();
	}
}
