/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test.view;

import java.util.List;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.archives.ui.bot.test.context.ArchiveContextMenu;
import org.jboss.tools.archives.ui.bot.test.dialog.ArchivePublishSettingsDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.EditArchiveDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.FilesetDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.FolderCreatingDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.NewJarDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.UserLibrariesFilesetDialog;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.JBossToolsProjectarchives;
import org.jboss.tools.ui.bot.ext.helper.TreeHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ViewBase;

/**
 * 
 * @author jjankovi
 *
 */
public class ProjectArchivesView extends ViewBase {

	private ArchiveContextMenu contextTool = null;
	
	public ProjectArchivesView() {
		super();
		viewObject = JBossToolsProjectarchives.LABEL;
		contextTool = new ArchiveContextMenu();
	}
	
	public void buildArchiveNode(String... path) {
		open.selectTreeNode(this.bot(), path);
		show();
		toolbarButton(IDELabel.ArchivesView.BUTTON_BUILD_ARCHIVE_NODE).click();
	}
	
	public NewJarDialog createNewJarArchive(String project) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = tree.getTreeItem(project);
		return contextTool.createNewJarArchive(tree, treeItem);
	}
	
	public void buildProjectFull(String project) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = tree.getTreeItem(project);
		contextTool.buildProjectFull(tree, treeItem);
	}
	
	public void buildArchiveFull(String... pathToArchive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = TreeHelper.expandNode(this.bot(), pathToArchive);
		contextTool.buildArchiveFull(tree, treeItem);
	}
	
	public FolderCreatingDialog createFolder(String... pathToArchive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = TreeHelper.expandNode(this.bot(), pathToArchive);
		return contextTool.createFolder(tree, treeItem);
	}
	
	public FilesetDialog createFileset(String... pathToArchive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = TreeHelper.expandNode(this.bot(), pathToArchive);
		return contextTool.createFileset(tree, treeItem);
	}
	
	public UserLibrariesFilesetDialog createUserLibraryFileset(String... pathToArchive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = TreeHelper.expandNode(this.bot(), pathToArchive);
		return contextTool.createUserLibraryFileset(tree, treeItem);
	}
	
	public EditArchiveDialog editArchive(String... pathToArchive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = TreeHelper.expandNode(this.bot(), pathToArchive);
		return contextTool.editArchive(tree, treeItem);
	}
	
	public void deleteArchive(boolean withContextMenu, String... pathToArchive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = TreeHelper.expandNode(this.bot(), pathToArchive);
		contextTool.deleteArchive(tree, treeItem, withContextMenu);
	}
	
	public ArchivePublishSettingsDialog publishToServer(boolean returnDialog,
			String... pathToArchive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = TreeHelper.expandNode(this.bot(), pathToArchive);
		return contextTool.publishToServer(tree, treeItem, returnDialog);
	}
	
	public ArchivePublishSettingsDialog editPublishSettings(String... pathToArchive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = TreeHelper.expandNode(this.bot(), pathToArchive);
		return contextTool.editPublishSettings(tree, treeItem);
	}
	
	public boolean itemExists(String... path) {
		try {
			this.bot().tree(0).getTreeItem(path[0]).collapse();
			TreeHelper.expandNode(bot(), path);
			return true;
		} catch (WidgetNotFoundException exc) {
			return false;
		}
	}
	
	private SWTBotToolbarButton toolbarButton(String toolbarToolTip) {
		List<SWTBotToolbarButton> toolbarButtons = getToolbarButtons();
		for (SWTBotToolbarButton button : toolbarButtons) {
			if (button.isEnabled() && button.getToolTipText().equals(toolbarToolTip)) {
				return button;
			}
		}
		throw new WidgetNotFoundException("Toolbar button '" + toolbarToolTip + "' was not " +
				"found or enabled");
	}
	
}
