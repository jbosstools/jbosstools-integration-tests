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

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.jboss.tools.archives.ui.bot.test.condition.UserLibraryFilesetIsInArchive;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if creating, modifying and deleting a user library fileset 
 * via archives view and explorer is possible
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
public class UserLibrariesFilesetTest extends ArchivesTestBase {

	private static String PROJECT_NAME= "pr2";
	private static String ARCHIVE_PATH = PROJECT_NAME + ".jar" + 
			" [/" + PROJECT_NAME + "]";
	
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
	
	@Before
	public void prepareWorkspace() {
		importArchiveProjectWithoutRuntime(PROJECT_NAME);
	}
	
	@After
	public void cleanUp() {
		for (Project project : projectExplorer.getProjects()) {
			project.delete(true);
		}
	}
	
	@Test
	public void testCreatingUserLibraryFileset() {
		view = viewForProject(PROJECT_NAME);
		Archive archiveInView = view.getProject().getArchive(ARCHIVE_PATH);
		testCreatingUserLibraryFileset(archiveInView, USER_LIBRARY_1);
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_NAME);
		Archive archiveInExplorer = explorer.getArchive(ARCHIVE_PATH);
		testCreatingUserLibraryFileset(archiveInExplorer, USER_LIBRARY_2);
	}
	
	@Test
	public void testModifyingUserLibraryFileset() {
		view = viewForProject(PROJECT_NAME);
		Archive archiveInView = view.getProject().getArchive(ARCHIVE_PATH);
		testCreatingUserLibraryFileset(archiveInView, USER_LIBRARY_1);
		testModifyUserLibraryFileset(archiveInView, USER_LIBRARY_1, USER_LIBRARY_3);
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_NAME);
		Archive archiveInExplorer = explorer.getArchive(ARCHIVE_PATH);
		testCreatingUserLibraryFileset(archiveInExplorer, USER_LIBRARY_2);
		testModifyUserLibraryFileset(archiveInExplorer, USER_LIBRARY_2, USER_LIBRARY_4);
	}
	
	@Test
	public void testDeletingUserLibraryFileset() {
		view = viewForProject(PROJECT_NAME);
		Archive archiveInView = view.getProject().getArchive(ARCHIVE_PATH);
		testCreatingUserLibraryFileset(archiveInView, USER_LIBRARY_1);
		testDeleteUserLibraryFileset(archiveInView, USER_LIBRARY_1);
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_NAME);
		Archive archiveInExplorer = explorer.getArchive(ARCHIVE_PATH);
		testCreatingUserLibraryFileset(archiveInExplorer, USER_LIBRARY_2);
		testDeleteUserLibraryFileset(archiveInExplorer, USER_LIBRARY_2);
	}
	
	private void testCreatingUserLibraryFileset(Archive archive, 
			String userLibraryFileset) {
		
		archive.newUserLibraryFileset().
			selectUserLibrary(userLibraryFileset).finish();
		
		try {
			new WaitUntil(new UserLibraryFilesetIsInArchive(
				archive, userLibraryFileset));
		} catch (WaitTimeoutExpiredException te) {
			fail("'" + userLibraryFileset
					 + "' was not created under archive '" 
					 + archive.getName() + "'!");
		}
	}
	
	private void testModifyUserLibraryFileset(Archive archive,
			String userLibrary1, String userLibrary2) {
		
		archive.getUserLibraryFileset(userLibrary1)
			.editUserLibraryFileset()
			.selectUserLibrary(userLibrary2).finish();
		
		try {
			new WaitUntil(new UserLibraryFilesetIsInArchive(archive, userLibrary1));
			new WaitWhile(new UserLibraryFilesetIsInArchive(archive, userLibrary2));
		} catch (WaitTimeoutExpiredException te) {
			fail("'" + userLibrary1 + "' was not modified to '" 
					 + userLibrary2 + "' under archive '" 
					 + archive.getName() + "'!");
		}
	}

	private void testDeleteUserLibraryFileset(Archive archive,
			String userLibrary) {
		
		archive.getUserLibraryFileset(userLibrary).deleteUserLibraryFileset(true);
		
		try {
			new WaitWhile(new UserLibraryFilesetIsInArchive(archive, userLibrary));
		} catch (WaitTimeoutExpiredException te) {
			fail("'" + userLibrary 
					 + "' was not deleted under archive '" 
					 + archive.getName() + "'!");
		}
	}
	
	private static void createUserLibrary(String userLibrary) {
		openUserLibraryPreferencePage();
		new PushButton(IDELabel.Button.NEW).click();
		new DefaultShell("New User Library");
		new DefaultText(0).setText(userLibrary);
		new PushButton(IDELabel.Button.OK).click();
		new PushButton(IDELabel.Button.OK).click();
		new WaitWhile(new JobIsRunning());
	}
	
	private static void openUserLibraryPreferencePage() {
		new ShellMenu("Window","Preferences").select();
		new DefaultShell("Preferences");
		new DefaultTreeItem("Java","Build Path","User Libraries").select();
	}
	
}
