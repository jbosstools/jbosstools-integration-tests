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
package org.jboss.tools.openshift.ui.bot.test.application.v3.create;

import java.io.File;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftProjectExists;
import org.jboss.tools.openshift.reddeer.requirement.OpenShiftCommandLineToolsRequirement.OCBinary;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.wizard.v3.TemplateParameter;
import org.jboss.tools.openshift.reddeer.wizard.v3.TemplatesCreator;
import org.jboss.tools.openshift.ui.bot.test.application.v3.advanced.CreateResourcesTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

@OCBinary
public class AbstractCreateApplicationTest {

	public static String GIT_FOLDER = "jboss-eap-quickstarts";
	public static String PROJECT_NAME = "jboss-helloworld";
	public static String BUILD_CONFIG = "eap-app";
	public static String TEMPLATE_PATH = CreateResourcesTest.RESOURCES_LOCATION +
			File.separator + "eap64-basic-s2i.json";
	
	public static String DEFAULT_NEXUS_MIRROR = "http://10.8.175.83:8081/nexus/content/groups/all-in-one/";
	
	// template params
	public static String CONTEXT_DIR = "helloworld";
	public static String SOURCE_REPO_URL = "https://github.com/mlabuda/jboss-eap-quickstarts";
	
	protected static TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	@BeforeClass
	public static void setUp() {
		TestUtils.cleanupGitFolder(GIT_FOLDER);
		TestUtils.setUpOcBinary();
		
		// If project does not exists, e.g. something went south in recreation earlier, create it
		if (!new OpenShiftProjectExists(DatastoreOS3.PROJECT1_DISPLAYED_NAME).test()) {
			new OpenShiftExplorerView().getOpenShift3Connection().createNewProject();
		}
		
		if (getNexusMirror() != null) {
			new TemplatesCreator().createOpenShiftApplicationBasedOnLocalTemplate(
				TEMPLATE_PATH, new TemplateParameter(OpenShiftLabel.Others.MAVEN_MIRROR_URL, getNexusMirror()));
		} else {
			new TemplatesCreator().createOpenShiftApplicationBasedOnLocalTemplate(TEMPLATE_PATH);
		}
	}
	
	/**
	 * Gets URL of Nexus Mirror. At first look up if user provided 
	 * nexus mirror URL by property {@link DatastoreOS3.KEY_NEXUS_MIRROR}. If
	 * none provided, try to use default, which is stored in 
	 * {@link AbstractCreateApplicationTest.DEFAULT_NEXUS_MIRROR}. If none of 
	 * the above works, use default, official nexus and this method returns null.
	 */
	private static String getNexusMirror() {
		if (isNexusMirrorProvided()) {
			return DatastoreOS3.NEXUS_MIRROR_URL;
		} else {
			if (isDefaultNexusMirrorWorking()) {
				return DEFAULT_NEXUS_MIRROR;
			} else {
				return null;
			}
		}
	}
	
	private static boolean isNexusMirrorProvided() {
		if (DatastoreOS3.NEXUS_MIRROR_URL == null || DatastoreOS3.NEXUS_MIRROR_URL.equals("")) {
			return false;
		}
		return TestUtils.isURLAccessible(DatastoreOS3.NEXUS_MIRROR_URL);
	}
	
	private static boolean isDefaultNexusMirrorWorking() {
		return TestUtils.isURLAccessible(DEFAULT_NEXUS_MIRROR);
	}
	
	@AfterClass
	public static void tearDown() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection  = explorer.getOpenShift3Connection();
		connection.getProject().delete();
		
		try {
			new WaitWhile(new OpenShiftProjectExists());
		} catch (WaitTimeoutExpiredException ex) {
			connection.refresh();
		
			new WaitWhile(new OpenShiftProjectExists(), TimePeriod.getCustom(5));
		}
		
		connection.createNewProject();
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		if (projectExplorer.containsProject(PROJECT_NAME)) {
			projectExplorer.getProject(PROJECT_NAME).delete(true);
		}
	}
}
