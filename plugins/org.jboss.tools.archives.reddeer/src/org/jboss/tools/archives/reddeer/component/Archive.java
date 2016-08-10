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

import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.condition.TreeContainsItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
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
	private TreeItem archiveProject;
	
	public Archive(TreeItem archiveProject, TreeItem archive) {
		this.archive = archive;
		this.archiveProject = archiveProject;
	}
	
	public String getName() {
		return archive.getText();
	}
	
	public NewJarDialog newJarArchive() {
		archive.select();
		new ContextMenu("New Archive", "JAR").select();
		return new NewJarDialog();
		
	}
	
	public NewFolderDialog newFolder() {
		archive.select();
		new ContextMenu("New Folder").select();
		return new NewFolderDialog();
	}
	
	public FilesetDialog newFileset() {
		archive.select();
		new ContextMenu("New Fileset").select();
		return new FilesetDialog();
	}
	
	public LibFilesetDialog newUserLibraryFileset() {
		archive.select();
		new ContextMenu("New User Library Fileset").select();
		return new LibFilesetDialog();
	}
	
	public void buildArchiveFull() {
		archive.select();
		new ContextMenu("Build Archive (Full)").select();
		new WaitWhile(new JobIsRunning());
	}
	
	public EditArchiveDialog editArchive() {
		archive.select();
		new ContextMenu("Edit Archive").select();
		return new EditArchiveDialog();
	}
	
	public void deleteArchive(boolean withContextMenu) {
		archive.select();
		if (withContextMenu) {
			new ContextMenu("Delete Archive").select();
		} else {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.DEL);
		}
		Shell dShell = new DefaultShell("Delete selected nodes?");
		new PushButton("Yes").click();
		new WaitWhile(new ShellIsAvailable(dShell));
		new WaitWhile(new JobIsRunning());
	}
	
	public ArchivePublishDialog publishToServer() {
		archive.select();
		new ContextMenu("Publish To Server").select();
		if (new DefaultShell().getText().equals("Archive Publish Settings")) {
			return new ArchivePublishDialog();
		}
		new WaitWhile(new JobIsRunning());
		return null;
	}
	
	public ArchivePublishDialog editPublishSettings() {
		archive.select();
		new ContextMenu("Edit publish settings...").select();
		return new ArchivePublishDialog();
	}
	
	public Archive getArchive(String archiveName) {
		new WaitUntil(new TreeContainsItem(archive.getParent(), archiveProject.getText(), archive.getText()));
		return new Archive(archiveProject, archive.getItem(archiveName));
	}
	
	public Folder getFolder(String folderName, boolean explorer) {
		waitForArchiveItem(folderName, explorer);
		return new Folder(archive.getItem(folderName));
	}
	
	public Fileset getFileset(String filesetName, boolean explorer) {
		waitForArchiveItem(filesetName, explorer);
		return new Fileset(archive.getItem(filesetName));
	}
	
	public UserLibraryFileset getUserLibraryFileset(String userLibraryFilesetName, boolean explorer) {
		waitForArchiveItem(userLibraryFilesetName, explorer);
		return new UserLibraryFileset(archive.getItem(userLibraryFilesetName));
	}
	
	private void waitForArchiveItem(String item, boolean explorer){
		if(!explorer){
			new WaitUntil(new TreeContainsItem(archive.getParent(), archiveProject.getText(), archive.getText(), item));
		} else {
			new WaitUntil(new TreeContainsItem(archive.getParent(), archiveProject.getText(), "Project Archives", archive.getText(), item));
		}
	}
}
