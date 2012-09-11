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
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.TreeContainsNode;
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
	
	private SWTBotTree viewTree = null;
	
	public ProjectArchivesView() {
		super();
		viewObject = JBossToolsProjectarchives.LABEL;
		show();
		contextTool = new ArchiveContextMenu();
		viewTree = this.bot().tree();
	}
	
	public void buildArchiveNode(String... path) {
		show();
		open.selectTreeNode(this.bot(), path);
		toolbarButton(IDELabel.ArchivesView.BUTTON_BUILD_ARCHIVE_NODE).click();
	}
	
	public NewJarDialog createNewJarArchive(String project) {
		return contextTool.createNewJarArchive(
				viewTree, getArchive(project));
	}
	
	public void buildProjectFull(String project) {
		contextTool.buildProjectFull(
				viewTree, getArchive(project));
	}
	
	public void buildArchiveFull(String... pathToArchive) {
		contextTool.buildArchiveFull(
				viewTree, getArchive(pathToArchive));
	}
	
	public FolderCreatingDialog createFolder(String... pathToArchive) {
		return contextTool.createFolder(
				viewTree, getArchive(pathToArchive));
	}
	
	public FilesetDialog createFileset(String... pathToArchive) {
		return contextTool.createFileset(
				viewTree, getArchive(pathToArchive));
	}
	
	public UserLibrariesFilesetDialog createUserLibraryFileset(String... pathToArchive) {
		return contextTool.createUserLibraryFileset(
				viewTree, getArchive(pathToArchive));
	}
	
	public EditArchiveDialog editArchive(String... pathToArchive) {
		return contextTool.editArchive(
				viewTree, getArchive(pathToArchive));
	}
	
	public void deleteArchive(boolean withContextMenu, String... pathToArchive) {
		contextTool.deleteArchive(
				viewTree, getArchive(pathToArchive), withContextMenu);
	}
	
	public ArchivePublishSettingsDialog publishToServer(boolean returnDialog,
			String... pathToArchive) {
		return contextTool.publishToServer(
				viewTree, getArchive(pathToArchive), returnDialog);
	}
	
	public ArchivePublishSettingsDialog editPublishSettings(String... pathToArchive) {
		return contextTool.editPublishSettings(
				viewTree, getArchive(pathToArchive));
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
	
	private SWTBotTreeItem getArchive(String... pathToArchive) {
		bot.waitUntil(new TreeContainsNode(viewTree, pathToArchive), Timing.time5S());
		return TreeHelper.expandNode(this.bot(), pathToArchive);
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
