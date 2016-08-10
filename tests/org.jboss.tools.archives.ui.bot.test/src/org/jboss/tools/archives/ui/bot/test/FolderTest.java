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


import org.jboss.reddeer.swt.condition.TreeContainsItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if creating, modifying and deleting a folder via 
 * archives view and explorer is possible.
 * 
 * @author jjankovi
 *
 */
public class FolderTest extends ArchivesTestBase {

	private static final String PROJECT_1 = "FolderTest";
	private static final String PROJECT_2 = "FolderTest1";
	private static final String PROJECT_3 = "FolderTest2";
	
	private static final String PROJECT_1_ARCHIVE = PROJECT_1 + ".jar";
	
	private static final String PROJECT_1_ARCHIVE_PATH = PROJECT_1_ARCHIVE + 
			" [/" + PROJECT_1 + "]";
	
	private static final String PROJECT_2_ARCHIVE = PROJECT_2 + ".jar";
	
	private static final String PROJECT_2_ARCHIVE_PATH = PROJECT_2_ARCHIVE+ 
			" [/" + PROJECT_2 + "]";
	
	private static final String PROJECT_3_ARCHIVE = PROJECT_1 + ".jar";
	
	private static final String PROJECT_3_ARCHIVE_PATH = PROJECT_3_ARCHIVE + 
			" [/" + PROJECT_3 + "]";
	
	private static final String FOLDER_1= "a";
	private static final String FOLDER_1_NEW = "new-a";
	private static final String FOLDER_2 = "b";
	private static final String FOLDER_2_NEW = "new-b";

	@BeforeClass
	public static void setup(){
		createJavaProject(PROJECT_1);
		addArchivesSupport(PROJECT_1);
		createArchive(PROJECT_1, PROJECT_1_ARCHIVE, true);
		
		createJavaProject(PROJECT_2);
		addArchivesSupport(PROJECT_2);
		createArchive(PROJECT_2, PROJECT_2_ARCHIVE, true);
		
		createJavaProject(PROJECT_3);
		addArchivesSupport(PROJECT_3);
		createArchive(PROJECT_3, PROJECT_3_ARCHIVE, true);
	}
	
	@Test
	public void testCreatingFolder() {
		view = viewForProject(PROJECT_1);
		
		Archive archiveInView = view.getProject(PROJECT_1).getArchive(PROJECT_1_ARCHIVE_PATH);
		archiveInView.newFolder().setNameOfFolder(FOLDER_1).ok();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_1, PROJECT_1_ARCHIVE_PATH, FOLDER_1));
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_1);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_1_ARCHIVE_PATH);
		archiveInExplorer.newFolder().setNameOfFolder(FOLDER_2).ok();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_1, "Project Archives", PROJECT_1_ARCHIVE_PATH, FOLDER_2));
		
	}
	
	@Test
	public void testModifyingFolder() {
		view = viewForProject(PROJECT_2);
		Archive archiveInView = view.getProject(PROJECT_2).getArchive(PROJECT_2_ARCHIVE_PATH);
		createFolder(archiveInView, PROJECT_2, PROJECT_2_ARCHIVE_PATH, FOLDER_1);
		createFolder(archiveInView, PROJECT_2, PROJECT_2_ARCHIVE_PATH, FOLDER_2);
		
		archiveInView.getFolder(FOLDER_1, false).editFolder().setNameOfFolder(FOLDER_1_NEW).ok();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_2, PROJECT_2_ARCHIVE_PATH, FOLDER_1_NEW));
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_2);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_2_ARCHIVE_PATH);
		archiveInExplorer.getFolder(FOLDER_2, true).editFolder().setNameOfFolder(FOLDER_2_NEW).ok();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_2, "Project Archives", PROJECT_2_ARCHIVE_PATH, FOLDER_2_NEW));
	}
	
	@Test
	public void testDeletingFolder() {
		view = viewForProject(PROJECT_3);
		Archive archiveInView = view.getProject(PROJECT_3).getArchive(PROJECT_3_ARCHIVE_PATH);
		createFolder(archiveInView, PROJECT_3, PROJECT_3_ARCHIVE_PATH, FOLDER_1);
		createFolder(archiveInView, PROJECT_3, PROJECT_3_ARCHIVE_PATH, FOLDER_2);
		
		
		archiveInView.getFolder(FOLDER_1, false).deleteFolder(true);
		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_3, PROJECT_3_ARCHIVE_PATH, FOLDER_1));
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_3);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_3_ARCHIVE_PATH);
		archiveInExplorer.getFolder(FOLDER_2, true).deleteFolder(true);
		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_3, PROJECT_3_ARCHIVE_PATH, FOLDER_2));
	}
	
	private void createFolder(Archive archiveInView, String project, String archivePath, String folder){
		archiveInView.newFolder().setNameOfFolder(folder).ok();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), project, archivePath, folder));
	}
	
}