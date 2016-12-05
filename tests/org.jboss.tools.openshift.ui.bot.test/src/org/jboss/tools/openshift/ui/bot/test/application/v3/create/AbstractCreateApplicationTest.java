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
import org.junit.AfterClass;
import org.junit.BeforeClass;

@OCBinary
public class AbstractCreateApplicationTest {

	public static String gitFolder = "jboss-eap-quickstarts";
	public static String projectName = "jboss-helloworld";
	public static String buildConfigName = "eap-app";
	
	// context dir for new applications is helloworld
	public static String CONTEXT_DIR = "helloworld";
	
	protected static TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	@BeforeClass
	public static void setUp() {
		TestUtils.cleanupGitFolder(gitFolder);
		TestUtils.setUpOcBinary();
		
		// If project does not exists, e.g. something went south in recreation earlier, create it
		if (!new OpenShiftProjectExists(DatastoreOS3.PROJECT1_DISPLAYED_NAME).test()) {
			new OpenShiftExplorerView().getOpenShift3Connection().createNewProject();
		}
		
		new TemplatesCreator().createOpenShiftApplicationBasedOnServerTemplate(
				OpenShiftLabel.Others.EAP_TEMPLATE, new TemplateParameter("CONTEXT_DIR", "helloworld"));
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
		if (projectExplorer.containsProject(projectName)) {
			projectExplorer.getProject(projectName).delete(true);
		}
	}
}
