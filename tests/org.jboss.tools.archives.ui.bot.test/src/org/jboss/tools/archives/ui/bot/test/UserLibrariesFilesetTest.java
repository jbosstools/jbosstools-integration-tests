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

import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.condition.TreeContainsItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.condition.ProjectContainsProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if creating, modifying and deleting a user library fileset 
 * via archives view and explorer is possible
 * 
 * @author jjankovi
 *
 */
public class UserLibrariesFilesetTest extends ArchivesTestBase {

	private static String PROJECT_NAME1= "UserLibrariesFilesetTest1";
	private static String ARCHIVE_NAME1 = PROJECT_NAME1 + ".jar";
	private static String ARCHIVE_PATH1 = ARCHIVE_NAME1 + 
			" [/" + PROJECT_NAME1 + "]";
	
	private static String PROJECT_NAME2= "UserLibrariesFilesetTest2";
	private static String ARCHIVE_NAME2 = PROJECT_NAME2 + ".jar";
	private static String ARCHIVE_PATH2 = ARCHIVE_NAME2 + 
			" [/" + PROJECT_NAME2 + "]";
	
	private static String PROJECT_NAME3= "UserLibrariesFilesetTest3";
	private static String ARCHIVE_NAME3 = PROJECT_NAME3 + ".jar";
	private static String ARCHIVE_PATH3 = ARCHIVE_NAME3 + 
			" [/" + PROJECT_NAME3 + "]";
	
	private final static String USER_LIBRARY_1 = "myLibrary1";
	private final static String USER_LIBRARY_2 = "myLibrary2";
	private final static String USER_LIBRARY_3 = "myLibrary3";
	private final static String USER_LIBRARY_4 = "myLibrary4";
	
	@BeforeClass
	public static void setup() {
		createUserLibrary(USER_LIBRARY_1);
		createUserLibrary(USER_LIBRARY_2);
		createUserLibrary(USER_LIBRARY_3);
		createUserLibrary(USER_LIBRARY_4);
	}
	
	@BeforeClass
	public static void prepareWorkspace() {
		createJavaProject(PROJECT_NAME1);
		addArchivesSupport(PROJECT_NAME1);
		createArchive(PROJECT_NAME1, ARCHIVE_NAME1, true);
		
		createJavaProject(PROJECT_NAME2);
		addArchivesSupport(PROJECT_NAME2);
		createArchive(PROJECT_NAME2, ARCHIVE_NAME2, true);
		
		createJavaProject(PROJECT_NAME3);
		addArchivesSupport(PROJECT_NAME3);
		createArchive(PROJECT_NAME3, ARCHIVE_NAME3, true);
	}
	
	@Test
	public void testCreatingUserLibraryFileset() {
		view = viewForProject(PROJECT_NAME1);
		Archive archiveInView = view.getProject(PROJECT_NAME1).getArchive(ARCHIVE_PATH1);
		archiveInView.newUserLibraryFileset().
		selectUserLibrary(USER_LIBRARY_1).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_NAME1, ARCHIVE_PATH1, USER_LIBRARY_1));
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_NAME1);
		Archive archiveInExplorer = explorer.getArchive(ARCHIVE_PATH1);
		archiveInExplorer.newUserLibraryFileset().
		selectUserLibrary(USER_LIBRARY_2).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_NAME1, "Project Archives",
				ARCHIVE_PATH1, USER_LIBRARY_2));
	}
	
	@Test
	public void testModifyingUserLibraryFileset() {
		view = viewForProject(PROJECT_NAME2);
		Archive archiveInView = view.getProject(PROJECT_NAME2).getArchive(ARCHIVE_PATH2);
		
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_NAME2);
		Archive archiveInExplorer = explorer.getArchive(ARCHIVE_PATH2);
		archiveInExplorer.newUserLibraryFileset().selectUserLibrary(USER_LIBRARY_2).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_NAME2, "Project Archives",
				ARCHIVE_PATH2, USER_LIBRARY_2));
		archiveInView.getUserLibraryFileset(USER_LIBRARY_2, true).editUserLibraryFileset()
		.selectUserLibrary(USER_LIBRARY_4).finish();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new WaitWhile(new ProjectContainsProjectItem(pe.getProject(PROJECT_NAME2), 
				"Project Archives", ARCHIVE_PATH2, USER_LIBRARY_2));
		
		new WaitUntil(new ProjectContainsProjectItem(pe.getProject(PROJECT_NAME2), 
				"Project Archives", ARCHIVE_PATH2, USER_LIBRARY_4));

		archiveInView.newUserLibraryFileset().selectUserLibrary(USER_LIBRARY_1).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_NAME2, ARCHIVE_PATH2, USER_LIBRARY_1));
		archiveInView.getUserLibraryFileset(USER_LIBRARY_1, false).editUserLibraryFileset()
		.selectUserLibrary(USER_LIBRARY_3).finish();
		//bug view wont refresh
		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_NAME2, ARCHIVE_PATH2, USER_LIBRARY_1));
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_NAME2, ARCHIVE_PATH2, USER_LIBRARY_3));
	}
	
	@Test
	public void testDeletingUserLibraryFileset() {
		view = viewForProject(PROJECT_NAME3);
		Archive archiveInView = view.getProject(PROJECT_NAME3).getArchive(ARCHIVE_PATH3);
		archiveInView.newUserLibraryFileset().
		selectUserLibrary(USER_LIBRARY_1).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_NAME3, ARCHIVE_PATH3, USER_LIBRARY_1));
		
		archiveInView.getUserLibraryFileset(USER_LIBRARY_1, false).deleteUserLibraryFileset(true);
		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_NAME3, ARCHIVE_PATH3, USER_LIBRARY_1));
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_NAME3);
		Archive archiveInExplorer = explorer.getArchive(ARCHIVE_PATH3);
		archiveInExplorer.newUserLibraryFileset().
		selectUserLibrary(USER_LIBRARY_2).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_NAME3, "Project Archives",
				ARCHIVE_PATH3, USER_LIBRARY_2));
		
		archiveInView.getUserLibraryFileset(USER_LIBRARY_2, false).deleteUserLibraryFileset(true);
		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_NAME3, "Project Archives",
				ARCHIVE_PATH3, USER_LIBRARY_2));
	}
	
	private static void createUserLibrary(String userLibrary) {
		WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
		wd.open();
		wd.select("Java","Build Path","User Libraries");
		new PushButton(IDELabel.Button.NEW).click();
		DefaultShell libraryShell = new DefaultShell("New User Library");
		new DefaultText(0).setText(userLibrary);
		new PushButton(IDELabel.Button.OK).click();
		new WaitWhile(new ShellIsAvailable(libraryShell));
		wd.ok();
	}
	
}
