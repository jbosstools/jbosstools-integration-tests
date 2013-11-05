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
import org.jboss.tools.archives.reddeer.archives.ui.FilesetDialog;
import org.jboss.tools.archives.reddeer.archives.ui.NewFolderDialog;
import org.jboss.tools.archives.reddeer.archives.ui.NewJarDialog;

/**
 * Folder retrieved from Project Archives view/explorer
 * 
 * @author jjankovi
 *
 */
public class Folder {
	
	private TreeItem folder;
	private ArchiveContextMenuAction menuAction;
	
	public String getName() {
		return folder.getText();
	}
	
	public Folder(TreeItem folder) {
		this.folder = folder;
		menuAction = new ArchiveContextMenuAction();
	}
	
	public NewJarDialog newJarArchive() {
		folder.select();
		return menuAction.createNewJarArchive();
	}
	
	public NewFolderDialog newFolder() {
		folder.select();
		return menuAction.createFolder();
	}
	
	public FilesetDialog newFileset() {
		folder.select();
		return menuAction.createFileset();
	}
	
	public LibFilesetDialog newUserLibraryFileset() {
		folder.select();
		return menuAction.createUserLibraryFileset();
	}
	
	public NewFolderDialog editFolder() {
		folder.select();
		return menuAction.editFolder();
	}
	
	public void deleteFolder(boolean withContextMenu) {
		folder.select();
		menuAction.deleteFolder(withContextMenu);
	}
	
	public Archive getArchive(String archiveName) {
		return new Archive(folder.getItem(archiveName));
	}
	
	public Folder getFolder(String folderName) {
		return new Folder(folder.getItem(folderName));
	}
	
	public Fileset getFileset(String filesetName) {
		return new Fileset(folder.getItem(filesetName));
	}
	
	public UserLibraryFileset getUserLibraryFileset(String userLibraryFilesetName) {
		return new UserLibraryFileset(folder.getItem(userLibraryFilesetName));
	}
	
}
