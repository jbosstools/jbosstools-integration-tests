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
package org.jboss.tools.archives.ui.bot.test.context;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.archives.ui.bot.test.dialog.ArchivePublishSettingsDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.EditArchiveDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.FolderCreatingDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.NewJarDialog;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;

/**
 * 
 * @author jjankovi
 *
 */
public class ArchiveContextMenu {

	public NewJarDialog createNewJarArchive(SWTBotTree tree, 
			SWTBotTreeItem item) {
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "New Archive", false)).menu("JAR");
		menu.click();
		return new NewJarDialog();
	}

	public void buildProjectFull(SWTBotTree tree, 
			SWTBotTreeItem item) {
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "Build Project (Full)", false));
		menu.click();
	}
	
	public void buildArchiveFull(SWTBotTree tree, 
			SWTBotTreeItem item) {
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "Build Archive (Full)", false));
		menu.click();
	}
	
	public FolderCreatingDialog createFolder(SWTBotTree tree, 
			SWTBotTreeItem item) {
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "New Folder", false));
		menu.click();
		return new FolderCreatingDialog();
	}
	
	public EditArchiveDialog editArchive(SWTBotTree tree, 
			SWTBotTreeItem item) {
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "Edit Archive", false));
		menu.click();
		return new EditArchiveDialog();
	}
	
	public void deleteArchive(SWTBotTree tree, 
			SWTBotTreeItem item) {
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "Delete Archive", false));
		menu.click();
		SWTBotFactory.getUtil().waitForNonIgnoredJobs();
	}
	
	public ArchivePublishSettingsDialog publishToServer(SWTBotTree tree, 
			SWTBotTreeItem item, boolean returnDialog) {
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "Publish To Server", false));
		menu.click();
		if (returnDialog) {
			return new ArchivePublishSettingsDialog();
		} else {
			return null;
		}
	}
	
	public ArchivePublishSettingsDialog editPublishSettings(SWTBotTree tree, 
			SWTBotTreeItem item) {
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "Edit publish settings...", false));
		menu.click();
		return new ArchivePublishSettingsDialog();
	}
	
}
