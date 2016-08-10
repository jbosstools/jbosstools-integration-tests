/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.MainPreferencePage;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.Test;

/**
 * Tests global and local archive preferences
 * 
 * @author jjankovi
 *
 */
public class ArchivePreferencesTest extends ArchivesTestBase {

	private static String projectName = "ArchivePreferencesTest";
	
	@Test
	public void testArchivePreferences() {
		testGlobalArchivePreferences();
		testLocalArchivePreferences();
	}

	private void testGlobalArchivePreferences() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		MainPreferencePage archivesPreferencePage = new MainPreferencePage();
		preferenceDialog.open();
		preferenceDialog.select(archivesPreferencePage);
		checkAllSettingsInArchivePreferencePage(archivesPreferencePage);
		preferenceDialog.ok();
	
	}

	private void testLocalArchivePreferences() {
		createJavaProject(projectName);
		addArchivesSupport(projectName);
		ProjectExplorer pExplorer = new ProjectExplorer();
		pExplorer.open();
		
		pExplorer.getProject(projectName).select();
		new ContextMenu(IDELabel.Menu.PROPERTIES).select();
		String projectProperties = "Properties for " + projectName;
		new DefaultShell(projectProperties);
		new DefaultTreeItem("Project Archives").select();
		new DefaultLink("Configure Workspace Settings...").click();
		new DefaultShell(IDELabel.Shell.PREFERENCES_FILTERED);
		try {
			new DefaultCLabel("Project Archives");
			new PushButton(IDELabel.Button.CANCEL).click();
			new WaitWhile(new ShellWithTextIsAvailable(IDELabel.Shell.PREFERENCES_FILTERED));
			new DefaultShell("Properties for " + projectName);
			new PushButton(IDELabel.Button.CANCEL).click();
			new WaitWhile(new ShellWithTextIsAvailable("Properties for " + projectName));
		}catch (Exception wnfe) {
			fail("Archive global preferences page was not invoked");
		}
	}

	private void checkAllSettingsInArchivePreferencePage(
			MainPreferencePage archivesPreferencePage) {
		archivesPreferencePage.enableDefaultExcludes(
				archivesPreferencePage.isIncrementalBuilderEnabled());
		
		archivesPreferencePage.showBuildErrorDialog(
				archivesPreferencePage.isBuildErrorDialogShown());
		
		archivesPreferencePage.showOutputPathNextToPackages(
				archivesPreferencePage.isOutputPathNextToPackagesShown());
		
		archivesPreferencePage.showRootDirectoryOfFilesets(
				archivesPreferencePage.isRootDirectoryOfFilesetsShown());
		
		archivesPreferencePage.showProjectAtTheRoot(
				archivesPreferencePage.isProjectAtTheRootShown());
		
		archivesPreferencePage.showAllProjectsThatContainPackages(
				archivesPreferencePage.areAllProjectsThatContainPackagesShown());
		
		archivesPreferencePage.showNodeInAllProjects(
				archivesPreferencePage.isNodeInAllProjectShown());
		
		archivesPreferencePage.enableDefaultExcludes(
				archivesPreferencePage.isDefaultExcludesEnabled());
	}
	
}
