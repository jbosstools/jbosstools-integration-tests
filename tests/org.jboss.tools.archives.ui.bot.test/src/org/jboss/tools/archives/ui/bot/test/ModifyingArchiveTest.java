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

import org.jboss.tools.archives.ui.bot.test.dialog.EditArchiveDialog;
import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class ModifyingArchiveTest extends ArchivesTestBase {

	private static String project = "pr3";
	private final String ARCHIVE_NAME_1 = 
			project + "a.jar";
	private final String ARCHIVE_NAME_2 = 
			project + "b.jar";
	private final String ARCHIVE_NAME_1_NEW = 
			project + "a-new.jar";
	private final String ARCHIVE_NAME_2_NEW = 
			project + "b-new.jar";
	
	private final String PATH_SUFFIX = " [/" + project + "]"; 
	private final String PATH_ARCHIVE_1 = 
			ARCHIVE_NAME_1 + PATH_SUFFIX;
	private final String PATH_ARCHIVE_2 = 
			ARCHIVE_NAME_2 + PATH_SUFFIX;
	private final String PATH_ARCHIVE_1_NEW = 
			ARCHIVE_NAME_1_NEW + PATH_SUFFIX;
	private final String PATH_ARCHIVE_2_NEW = 
			ARCHIVE_NAME_2_NEW + PATH_SUFFIX;
	
	@BeforeClass
	public static void setup() {
		importProjectWithoutRuntime(project);
	}
	
	@Test
	public void testModifyingArchivetWithView() {
		
		/* prepare view for testing */
		ProjectArchivesView view = viewForProject(project);
		
		/* modifying archive name with Project Archive view */
		EditArchiveDialog dialog = view.editArchive(project, PATH_ARCHIVE_1);
		editArchive(dialog, ARCHIVE_NAME_1_NEW);
		
		/* test archive was modified */
		assertItemNotExistsInView(view, project, PATH_ARCHIVE_1);
		assertItemExistsInView(view, project, PATH_ARCHIVE_1_NEW);
	}
	
	@Test
	public void testModifyingArchiveWithExplorer() {
		
		/* prepare explorer for testing */
		ProjectArchivesExplorer explorer = new ProjectArchivesExplorer(project);
		
		/* modifying archive name with Project Archive explorer */
		EditArchiveDialog dialog = explorer.editArchive(PATH_ARCHIVE_2);
		editArchive(dialog, ARCHIVE_NAME_2_NEW);
		
		/* test archive was modified */
		assertItemNotExistsInExplorer(explorer, PATH_ARCHIVE_2);
		assertItemExistsInExplorer(explorer, PATH_ARCHIVE_2_NEW);
	}
	
	private void editArchive(EditArchiveDialog dialog, String newArchiveName) {
		dialog.setArchiveName(newArchiveName);
		dialog.finish();
	}
	
}
