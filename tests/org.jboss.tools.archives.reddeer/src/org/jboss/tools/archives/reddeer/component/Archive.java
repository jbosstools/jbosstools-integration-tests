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

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.archives.reddeer.archives.jdt.integration.LibFilesetDialog;
import org.jboss.tools.archives.reddeer.archives.ui.ArchivePublishDialog;
import org.jboss.tools.archives.reddeer.archives.ui.EditArchiveDialog;
import org.jboss.tools.archives.reddeer.archives.ui.FilesetDialog;
import org.jboss.tools.archives.reddeer.archives.ui.NewFolderDialog;
import org.jboss.tools.archives.reddeer.archives.ui.NewJarDialog;

/**
 * Archive retrieved from Project Archives view/explorer
 * 
 * @author jjankovi
 *
 */
public class Archive {

	private TreeItem archive;
	private ArchiveContextMenuAction menuAction;
	
	public Archive(TreeItem archive) {
		this.archive = archive;
		menuAction = new ArchiveContextMenuAction();
	}
	
	public String getName() {
		return archive.getText();
	}
	
	public NewJarDialog newJarArchive() {
		archive.select();
		return menuAction.createNewJarArchive();
		
	}
	
	public NewFolderDialog newFolder() {
		archive.select();
		return menuAction.createFolder();
	}
	
	public FilesetDialog newFileset() {
		archive.select();
		return menuAction.createFileset();
	}
	
	public LibFilesetDialog newUserLibraryFileset() {
		archive.select();
		return menuAction.createUserLibraryFileset();
	}
	
	public void buildArchiveFull() {
		archive.select();
		menuAction.buildArchiveFull();
	}
	
	public EditArchiveDialog editArchive() {
		archive.select();
		return menuAction.editArchive();
	}
	
	public void deleteArchive(boolean withContextMenu) {
		archive.select();
		menuAction.deleteArchive(withContextMenu);
	}
	
//	public void deleteArchives(boolean withContextMenu, String... archives) {
//		SWTBotTreeItem[] items = new SWTBotTreeItem[archives.length];
//		
//		int index = 0;
//		for (String archive : archives) {
//			items[index] = getArchive(archive);
//			index++;
//		}
//		deleteArchives(this.bot().tree(), withContextMenu, items);
//	}
	
	public ArchivePublishDialog publishToServer() {
		archive.select();
		return menuAction.publishToServer();
	}
	
	public ArchivePublishDialog editPublishSettings() {
		archive.select();
		return menuAction.editPublishSettings();
	}
	
	public Archive getArchive(String archiveName) {
		return new Archive(archive.getItem(archiveName));
	}
	
	public Folder getFolder(String folderName) {
		return new Folder(archive.getItem(folderName));
	}
	
	public Fileset getFileset(String filesetName) {
		return new Fileset(archive.getItem(filesetName));
	}
	
	public UserLibraryFileset getUserLibraryFileset(String userLibraryFilesetName) {
		return new UserLibraryFileset(archive.getItem(userLibraryFilesetName));
	}


//	public boolean itemExists(String... path) {
//		try {
//			new DefaultTreeItem(globalPath(path));
//			return true;
//		} catch (Exception sle) {
//			return false;
//		}
//	}
	
//	private String[] globalPath(String... path) {
//		String[] globalPath = new String[path.length + 2];
//		globalPath[0] = explorer.getPath()[0];
//		globalPath[1] = explorer.getPath()[1];
//		for (int i = 2; i < globalPath.length; i++) {
//			globalPath[i] = path[i-2];
//		}
//		return globalPath;
//	}
	
}
