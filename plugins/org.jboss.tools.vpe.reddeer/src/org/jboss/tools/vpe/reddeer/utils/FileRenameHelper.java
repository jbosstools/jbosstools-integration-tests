/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.utils;

import static org.junit.Assert.assertNotNull;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.core.handler.TreeItemHandler;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.jst.reddeer.web.ui.navigator.WebProjectsNavigator;

/**
 * Check Renaming Functionality within WebProjects View Tests if file was
 * properly renamed in WebProjects View and Title of file in Editor was renamed
 * also.
 * 
 * @author Vladimir Pakan
 *
 */
public class FileRenameHelper {
	/**
	 * Check File Renaming
	 * 
	 * @param bot
	 * @param oldFileName
	 * @param newFileName
	 * @param treePathItems
	 * @param fileTreeItemSuffix
	 * @return
	 */
	public static String checkFileRenamingWithinWebProjects(String oldFileName, String newFileName,
			String[] treePathItems, String fileTreeItemSuffix) {

		WebProjectsNavigator webProjectsNavigator = new WebProjectsNavigator();
		webProjectsNavigator.open();
		TreeItem tiParent = null;
		if (treePathItems != null && treePathItems.length > 0) {
			tiParent = webProjectsNavigator.getProject(treePathItems[0]).getTreeItem();
			for (int index = 1; index < treePathItems.length; index++) {
				tiParent = tiParent.getItem(treePathItems[index]);
			}
			tiParent.select();
		}
		assertNotNull(tiParent);
		// Open File
		TreeItem tiFile = tiParent.getItem(oldFileName + fileTreeItemSuffix);
		tiFile.select();
		TreeItemHandler.getInstance().click(tiFile.getSWTWidget());
		new ContextMenuItem("Open").select();
		tiFile.select();
		new ShellMenuItem("File", "Rename...").select();
		new DefaultShell("Rename Resource");
		new LabeledText("New name:").setText(newFileName);
		new OkButton().click();
		new WaitWhile(new JobIsRunning(), TimePeriod.DEFAULT);
		new WaitWhile(new ShellIsAvailable("Rename Resource"));
		// Check Results
		// File with Old Name doesn't exists within WebProjects View
		try {
			tiParent.getItem(oldFileName + fileTreeItemSuffix);
			return "File " + oldFileName + " was not renamed to " + newFileName + ".";
		} catch (CoreLayerException cle) {
			// do nothing
		}
		// File with New Name exists within WebProjects View
		try {
			tiParent.getItem(newFileName + fileTreeItemSuffix);
		} catch (CoreLayerException cle) {
			return "Renamed File " + newFileName + " was not found.";
		}
		try {
			new DefaultEditor(newFileName);
		} catch (CoreLayerException cle) {
			return "Editor Title was not changed to " + newFileName + " after renaming.";
		}

		return null;

	}

	/**
	 * Check File Renaming
	 * 
	 * @param oldFileName
	 * @param newFileName
	 * @param treePathItems
	 * @return
	 */
	public static String checkFileRenamingWithinWebProjects(String oldFileName, String newFileName,
			String[] treePathItems) {
		return checkFileRenamingWithinWebProjects(oldFileName, newFileName, treePathItems, "");
	}

	/**
	 * Check Project Renaming within Package Explorer
	 * @param oldProjectName
	 * @param newProjectName
	 * @param renameShellTitle
	 * @return
	 */
	public static String checkProjectRenamingWithinPackageExplorer(String oldProjectName,
			String newProjectName, String renameShellTitle) {

		PackageExplorerPart packageExplorer = new PackageExplorerPart();
		packageExplorer.open();
		packageExplorer.getProject(oldProjectName).select();
		// Rename project
		new ShellMenuItem("File","Rename...").select();
		new DefaultShell(renameShellTitle);
		new LabeledText("New name:").setText(newProjectName);
		new OkButton().click();
		new WaitWhile(new JobIsRunning());
		// Check Results
		// Project with Old Name doesn't exists within Package explorer
		if (packageExplorer.containsProject(oldProjectName)){
			return "Project " + oldProjectName + " was not renamed to " + newProjectName + ".";
		}// Project with New Name exists within Package Explorer
		if (!packageExplorer.containsProject(newProjectName)){
			return "Renamed Project " + newProjectName + " was not found.";
		}
		
		return null;
	}

}
