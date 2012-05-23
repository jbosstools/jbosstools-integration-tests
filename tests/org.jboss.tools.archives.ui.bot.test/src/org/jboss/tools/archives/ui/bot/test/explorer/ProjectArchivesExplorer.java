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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.archives.ui.bot.test.context.ArchiveContextMenu;
import org.jboss.tools.archives.ui.bot.test.dialog.ArchivePublishSettingsDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.EditArchiveDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.NewJarDialog;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
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
		SWTBotTree tree = this.bot().tree();
		return contextTool.createNewJarArchive(tree, explorer);
	}
	
	public void buildProjectFull() {
		SWTBotTree tree = this.bot().tree();
		contextTool.buildProjectFull(tree, explorer);
	}
	
	public void buildArchiveFull(String archive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = explorer.getNode(archive);
		contextTool.buildArchiveFull(tree, treeItem);
	}
	
	public EditArchiveDialog editArchive(String archive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = explorer.getNode(archive);
		return contextTool.editArchive(tree, treeItem);
	}
	
	public void deleteArchive(String archive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = explorer.getNode(archive);
		contextTool.deleteArchive(tree, treeItem);
	}
	
	public ArchivePublishSettingsDialog publishToServer(String archive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = explorer.getNode(archive);
		return contextTool.publishToServer(tree, treeItem);
	}
	
	public ArchivePublishSettingsDialog editPublishSettings(String archive) {
		SWTBotTree tree = this.bot().tree();
		SWTBotTreeItem treeItem = explorer.getNode(archive);
		return contextTool.editPublishSettings(tree, treeItem);
	}
	
	public boolean itemExists(String... path) {
		try {
			SWTBotTreeItem ti = explorer;
			for (String pathItem : path) {
				ti = ti.getNode(pathItem);
			}
			return true;
		} catch (WidgetNotFoundException exc) {
			return false;
		}
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
