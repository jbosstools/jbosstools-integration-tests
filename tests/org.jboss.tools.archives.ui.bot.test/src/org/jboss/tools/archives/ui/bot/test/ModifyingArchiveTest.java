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

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.archives.reddeer.archives.ui.EditArchiveDialog;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.condition.ArchiveIsInExplorer;
import org.jboss.tools.archives.ui.bot.test.condition.ArchiveIsInView;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if modifying an archive via 
 * archives view and explorer is possible.
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
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
		importArchiveProjectWithoutRuntime(project);
	}
	
	@Test
	public void testModifyingArchiveWithView() {
		
		/* prepare view for testing */
		view = viewForProject(project);
		
		/* modifying archive name with Project Archive view */
		EditArchiveDialog dialog = view
				.getProject()
				.getArchive(PATH_ARCHIVE_1)
				.editArchive();
		editArchive(dialog, ARCHIVE_NAME_1_NEW);
		
		/* test archive was modified */
		new WaitUntil(new ArchiveIsInView(PATH_ARCHIVE_1_NEW, view));
		assertArchiveIsNotInView(view, PATH_ARCHIVE_1);
	}
	
	@Test
	public void testModifyingArchiveWithExplorer() {
		
		/* prepare explorer for testing */
		ProjectArchivesExplorer explorer = new ProjectArchivesExplorer(project);
		
		/* modifying archive name with Project Archive explorer */
		EditArchiveDialog dialog = explorer
				.getArchive(PATH_ARCHIVE_2)
				.editArchive();
		editArchive(dialog, ARCHIVE_NAME_2_NEW);
		
		/* test archive was modified */
		new WaitUntil(new ArchiveIsInExplorer(PATH_ARCHIVE_2_NEW, explorer));
		assertArchiveIsNotInExplorer(explorer, PATH_ARCHIVE_2);
	}
	
	private void editArchive(EditArchiveDialog dialog, String newArchiveName) {
		dialog.setArchiveName(newArchiveName);
		dialog.finish();
	}
	
}
