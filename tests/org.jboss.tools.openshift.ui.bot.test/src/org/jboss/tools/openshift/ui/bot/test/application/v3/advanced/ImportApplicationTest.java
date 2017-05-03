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
package org.jboss.tools.openshift.ui.bot.test.application.v3.advanced;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.condition.TreeHasChildren;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.condition.ButtonWithTextIsAvailable;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.Before;
import org.junit.Test;

public class ImportApplicationTest extends AbstractCreateApplicationTest {
	
	@Before
	public void cleanGitFolder() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		if (projectExplorer.containsProject(PROJECT_NAME)) {
			projectExplorer.getProject(PROJECT_NAME).delete(true);
		}
		
		TestUtils.cleanupGitFolder(GIT_FOLDER);
	}
	
	@Test
	public void testImportOpenShiftApplicationViaOpenShiftExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		new WaitUntil(new OpenShiftResourceExists(Resource.BUILD_CONFIG), TimePeriod.LONG);
		
		explorer.getOpenShift3Connection().getProject().getOpenShiftResources(Resource.BUILD_CONFIG).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.IMPORT_APPLICATION).select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.VERY_LONG);
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		assertTrue("There should be imported kitchen sink project, but there is not",
				projectExplorer.containsProject(PROJECT_NAME));
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Test
	public void testImportOpenShiftApplicationViaShellMenu() {
		new ShellMenu("File", "Import...").select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.IMPORT), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT);
		new DefaultTreeItem("OpenShift", "Existing OpenShift Application").select();
		new NextButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		
		new NextButton().click();
		TestUtils.acceptSSLCertificate();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			
		new WaitUntil(new ButtonWithTextIsAvailable("Refresh"), TimePeriod.LONG);
		new WaitUntil(new TreeHasChildren(new DefaultTree()));
		
		
		new DefaultTreeItem(DatastoreOS3.PROJECT1_DISPLAYED_NAME + " " + DatastoreOS3.PROJECT1).getItems().
			get(0).select();
		
		new WaitUntil(new ControlIsEnabled(new NextButton()));
		
		new NextButton().click();
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.VERY_LONG);
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		assertTrue("There should be imported " + PROJECT_NAME + "project, but there is not",
				projectExplorer.containsProject(PROJECT_NAME));
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
