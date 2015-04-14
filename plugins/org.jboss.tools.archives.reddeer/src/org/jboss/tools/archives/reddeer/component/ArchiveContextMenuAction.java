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
package org.jboss.tools.archives.reddeer.component;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.jdt.integration.LibFilesetDialog;
import org.jboss.tools.archives.reddeer.archives.ui.ArchivePublishDialog;
import org.jboss.tools.archives.reddeer.archives.ui.EditArchiveDialog;
import org.jboss.tools.archives.reddeer.archives.ui.FilesetDialog;
import org.jboss.tools.archives.reddeer.archives.ui.NewFolderDialog;
import org.jboss.tools.archives.reddeer.archives.ui.NewJarDialog;

/**
 * ArchiveContextMenuActions serves as a place for methods used
 * by various components
 * 
 * @author jjankovi
 *
 */
public class ArchiveContextMenuAction {

	public NewJarDialog createNewJarArchive() {
		new ContextMenu("New Archive", "JAR").select();
		return new NewJarDialog();
	}
	
	public EditArchiveDialog editArchive() {
		new ContextMenu("Edit Archive").select();
		return new EditArchiveDialog();
	}
	
	public void deleteArchive(boolean withContextMenu) {
		deleteComponent("Delete Archive", withContextMenu);
	}

	public void buildProjectFull() {
		new ContextMenu("Build Project (Full)").select();
	}
	
	public void buildArchiveFull() {
		new ContextMenu("Build Archive (Full)").select();
	}
	
	public NewFolderDialog createFolder() {
		new ContextMenu("New Folder").select();
		return new NewFolderDialog();
	}
	
	public NewFolderDialog editFolder() {
		new ContextMenu("Edit Folder").select();
		return new NewFolderDialog();
	}
	
	public void deleteFolder(boolean withContextMenu) {
		deleteComponent("Delete Folder", withContextMenu);
	}
	
	public FilesetDialog createFileset() {
		new ContextMenu("New Fileset").select();
		return new FilesetDialog();
	}
	
	public FilesetDialog editFileset() {
		new ContextMenu("Edit Fileset").select();
		return new FilesetDialog();
	}
	
	public void deleteFileset(boolean withContextMenu) {
		deleteComponent("Delete Fileset", withContextMenu);
	}
	
	public LibFilesetDialog createUserLibraryFileset() {
		new ContextMenu("New User Library Fileset").select();
		return new LibFilesetDialog();
	}
	
	public LibFilesetDialog editUserLibraryFileset() {
		new ContextMenu("Edit Fileset").select();
		return new LibFilesetDialog();
	}
	
	public void deleteUserLibraryFileset(boolean withContextMenu) {
		deleteComponent("Delete Fileset", withContextMenu);
	}
	
	
//	public void deleteArchives(SWTBotTree tree,
//			boolean withContextMenu, SWTBotTreeItem... items) {
//		tree.select(items);
//		if (withContextMenu) { 
//			new ContextMenu("Delete Archive").select();
//		} else {
//			items[0].pressShortcut(Keystrokes.DELETE);
//		}
//		
//		handleDeleteDialog();
//		new WaitWhile(new JobIsRunning());
//	}
	
	public ArchivePublishDialog publishToServer() {
		new ContextMenu("Publish To Server").select();
		if (new DefaultShell().getText().equals("Archive Publish Settings")) {
			return new ArchivePublishDialog();
		}
		return null;
	}
	
	public ArchivePublishDialog editPublishSettings() {
		new ContextMenu("Edit publish settings...").select();
		return new ArchivePublishDialog();
	}
	
	public void deleteComponent(String contextMenu, boolean withContextMenu) {
		if (withContextMenu) {
			new ContextMenu(contextMenu).select();
		} else {
//			item.select();
//			item.pressShortcut(Keystrokes.DELETE);
		}
		
		handleDeleteDialog();
		new WaitWhile(new JobIsRunning());
	}
	
	private void handleDeleteDialog() {
		try {
			new DefaultShell("Delete selected nodes?");
			new PushButton("Yes").click();
		} catch (SWTLayerException sle) {
			//do nothing here
		}
	}
	
}
