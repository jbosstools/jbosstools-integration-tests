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
package org.jboss.tools.archives.ui.bot.test;

import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.jboss.tools.archives.ui.bot.test.condition.FolderIsInArchive;
import org.junit.After;
import org.junit.Test;

/**
 * Tests if creating, modifying and deleting a folder via 
 * archives view and explorer is possible.
 * 
 * @author jjankovi
 *
 */
public class FolderTest extends ArchivesTestBase {

	private static final String PROJECT_1 = "pr2";
	private static final String PROJECT_2 = "pr5";
	
	private static final String PROJECT_1_ARCHIVE_PATH = PROJECT_1 + ".jar" + 
			" [/" + PROJECT_1 + "]";
	private static final String PROJECT_2_ARCHIVE_PATH = PROJECT_2 + ".jar" + 
			" [/" + PROJECT_2 + "]";
	
	private static final String FOLDER_1= "a";
	private static final String FOLDER_1_NEW = "new-a";
	private static final String FOLDER_2 = "b";
	private static final String FOLDER_2_NEW = "new-b";
	
	@After
	public void cleanUp() {
		for (Project project : projectExplorer.getProjects()) {
			project.delete(true);
		}
	}
	
	@Test
	public void testCreatingFolder() {
		importArchiveProjectWithoutRuntime(PROJECT_1);
		
		view = viewForProject(PROJECT_1);
		Archive archiveInView = view.getProject().getArchive(PROJECT_1_ARCHIVE_PATH);
		testCreatingFolder(archiveInView, FOLDER_1);
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_1);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_1_ARCHIVE_PATH);
		testCreatingFolder(archiveInExplorer, FOLDER_2);
		
	}
	
	@Test
	public void testModifyingFolder() {
		importArchiveProjectWithoutRuntime(PROJECT_2);
		
		view = viewForProject(PROJECT_2);
		Archive archiveInView = view.getProject().getArchive(PROJECT_2_ARCHIVE_PATH);
		testModifyFolder(archiveInView, FOLDER_1, FOLDER_1_NEW);
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_2);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_2_ARCHIVE_PATH);
		testModifyFolder(archiveInExplorer, FOLDER_2, FOLDER_2_NEW);
	}
	
	@Test
	public void testDeletingFolder() {
		importArchiveProjectWithoutRuntime(PROJECT_2);
		
		view = viewForProject(PROJECT_2);
		Archive archiveInView = view.getProject().getArchive(PROJECT_2_ARCHIVE_PATH);
		testDeleteFolder(archiveInView, FOLDER_1);
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_2);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_2_ARCHIVE_PATH);
		testDeleteFolder(archiveInExplorer, FOLDER_2);
	}
	
	private void testCreatingFolder(Archive archive, String folderName) {
		
		archive.newFolder().setNameOfFolder(folderName).ok();
		
		try {
			new WaitUntil(new FolderIsInArchive(archive, folderName));
		} catch (TimeoutException te) {
			fail("'" + folderName 
					 + "' was not created under archive '" 
					 + archive.getName() + "'!");
		}
	}
	
	private void testModifyFolder(Archive archive, String folderName, 
			String folderNewName) {
		
		archive.getFolder(folderName).editFolder()
			.setNameOfFolder(folderNewName).ok();
		
		try {
			new WaitWhile(new FolderIsInArchive(archive, folderName));
			new WaitUntil(new FolderIsInArchive(archive, folderNewName));
		} catch (TimeoutException te) {
			fail("'" + folderName 
					 + "' was not modified to '" + folderNewName 
					 + "' under archive '" + archive.getName() + "'!");
		}
	}
	
	private void testDeleteFolder(Archive archive, String folderName) {
		
		archive.getFolder(folderName).deleteFolder(true);
		
		try {
			new WaitWhile(new FolderIsInArchive(archive, folderName));
		} catch (TimeoutException te) {
			fail("'" + folderName 
					 + "' was not deleted under archive '" 
					 + archive.getName() + "'!");
		}
	}
	
}