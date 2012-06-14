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
package org.jboss.tools.archives.ui.bot.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.archives.ui.bot.test.dialog.UserLibrariesFilesetDialog;
import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.jboss.tools.ui.bot.ext.gen.IPreference;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class UserLibrariesFilesetTest extends ArchivesTestBase {

	private static String projectName = "pr2";
	
	private final String PATH_SUFFIX = " [/" + projectName + "]"; 
	private final String ARCHIVE_NAME = projectName + ".jar";
	private final String ARCHIVE_PATH = 
			ARCHIVE_NAME + PATH_SUFFIX;
	
	private final static String USER_LIBRARY_1 = "myLibrary1";
	private final static String USER_LIBRARY_2 = "myLibrary2";
	
	@BeforeClass
	public static void setup() {
		importProjectWithoutRuntime(projectName);
		createUserLibrary(USER_LIBRARY_1);
		createUserLibrary(USER_LIBRARY_2);
	}
	
	@Test
	public void testCreatingUserLibraryFileSetInView() {
		
		/* prepare view for testing */
		ProjectArchivesView view = viewForProject(projectName);
		
		/* create folder */
		createUserLibraryFileset(view.createUserLibraryFileset(projectName, ARCHIVE_PATH), USER_LIBRARY_1);
		
		/* test if folder was created */
		assertItemExistsInView(view, projectName, ARCHIVE_PATH, USER_LIBRARY_1);
	}
	
	@Test // not implemented in tools yet
	public void testCreatingUserLibraryFileSetInExplorer() {
		/* prepare view for testing */
		ProjectArchivesExplorer explorer = explorerForProject(projectName);
		
		/* create folder */
		createUserLibraryFileset(explorer.createUserLibraryFileset(ARCHIVE_PATH), USER_LIBRARY_2);
		
		/* test if folder was created */
		assertItemExistsInExplorer(explorer, ARCHIVE_PATH, USER_LIBRARY_2);
	}
	
	private void createUserLibraryFileset(
			UserLibrariesFilesetDialog dialog, String userLibrary) {
		dialog.selectUserLibrary(userLibrary).finish();
	}
	
	private static void createUserLibrary(String userLibrary) {
		SWTBot bot = getUserLibraryPreferencePage();
		bot.button(IDELabel.Button.NEW).click();
		SWTBotShell shell = bot.shell("New User Library");
		shell.bot().text().setText(userLibrary);
		shell.bot().button(IDELabel.Button.OK).click();
		bot.button(IDELabel.Button.OK).click();
		util.waitForNonIgnoredJobs();
	}
	
	private static SWTBot getUserLibraryPreferencePage() {
		IPreference page = new IPreference() {
			
			@Override
			public String getName() {
				return "User Libraries";
			}
			
			@Override
			public List<String> getGroupPath() {
				List<String> path = new ArrayList<String>();
				path.add("Java");
				path.add("Build Path");
				return path;
			}
		};
		return open.preferenceOpen(page);
	}
	
}
