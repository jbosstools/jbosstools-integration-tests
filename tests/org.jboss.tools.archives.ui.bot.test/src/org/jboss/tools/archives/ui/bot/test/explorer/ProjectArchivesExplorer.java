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
package org.jboss.tools.archives.ui.bot.test.explorer;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.archives.ui.bot.test.context.ArchiveContextMenu;
import org.jboss.tools.archives.ui.bot.test.dialog.ArchivePublishSettingsDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.EditArchiveDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.FilesetDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.FolderCreatingDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.NewJarDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.UserLibrariesFilesetDialog;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.TreeItemContainsNode;
import org.jboss.tools.ui.bot.ext.helper.TreeHelper;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;

/**
 * 
 * @author jjankovi
 *
 */
public class ProjectArchivesExplorer {

	private ArchiveContextMenu contextTool = null;
	private SWTBotTreeItem explorer = null;
	private SWTBot bot = null;
	
	public ProjectArchivesExplorer(String project) {
		contextTool = new ArchiveContextMenu();
		explorer = openExplorer(project);
		explorer.expand();
	}
	
	public NewJarDialog createNewJarArchive() {
		return contextTool.createNewJarArchive(
				this.bot().tree(), explorer);
	}
	
	public void buildProjectFull() {
		contextTool.buildProjectFull(
				this.bot().tree(), explorer);
	}
	
	public void buildArchiveFull(String archive) {
		contextTool.buildArchiveFull(
				this.bot().tree(), getArchive(archive));
	}
	
	public FolderCreatingDialog createFolder(String archive) {
		return contextTool.createFolder(
				this.bot().tree(), getArchive(archive));
	}
	
	public FilesetDialog createFileset(String archive) {
		return contextTool.createFileset(
				this.bot().tree(), getArchive(archive));
	}
	
	public UserLibrariesFilesetDialog createUserLibraryFileset(String archive) {
		return contextTool.createUserLibraryFileset(
				this.bot().tree(), getArchive(archive));
	}
	
	public EditArchiveDialog editArchive(String archive) {
		return contextTool.editArchive(
				this.bot().tree(), getArchive(archive));
	}
	
	public void deleteArchive(boolean withContextMenu, String archive) {
		contextTool.deleteArchive(
				this.bot().tree(), getArchive(archive), withContextMenu);
	}
	
	public ArchivePublishSettingsDialog publishToServer(
			boolean returnDialog, String archive) {
		return contextTool.publishToServer(
				this.bot().tree(), getArchive(archive), returnDialog);
	}
	
	public ArchivePublishSettingsDialog editPublishSettings(String archive) {
		return contextTool.editPublishSettings(
				this.bot().tree(), getArchive(archive));
	}
	
	public boolean itemExists(String... path) {
		try {
			SWTBotTreeItem ti = explorer;
			explorer.collapse();
			for (String pathItem : path) {
				ti.expand();
				ti = ti.getNode(pathItem);
			}
			return true;
		} catch (WidgetNotFoundException exc) {
			return false;
		}
	}
	
	private SWTBotTreeItem getArchive(String archive) {
		bot.waitUntil(new TreeItemContainsNode(explorer, archive), Timing.time5S());
		return explorer.getNode(archive);
	}
	
	private SWTBot bot() {
		return bot;
	}
	
	private SWTBotTreeItem openExplorer(String project) {
		ProjectExplorer pe = SWTBotFactory.getProjectexplorer();
		pe.show();
		bot = pe.bot();
		pe.selectProject(project);
		return TreeHelper.expandNode(pe.bot(), project, "Project Archives");
	}
	
	
}
