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
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests if deleting an archive via archives view and explorer is possible.
 * Deleting is performed by:
 * 1. Menu item (Edit -> Delete)
 * 2. Del key
 * 3. by multideletion event
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
public class DeletingArchiveTest extends ArchivesTestBase {

	private static String project = "pr4";
	private static final String ARCHIVE_NAME_AA = 
			project + "aa.jar";
	private static final String ARCHIVE_NAME_AB = 
			project + "ab.jar";
	private static final String ARCHIVE_NAME_BA = 
			project + "ba.jar";
	private static final String ARCHIVE_NAME_BB = 
			project + "bb.jar";
	private static final String ARCHIVE_NAME_CA = 
			project + "ca.jar";
	private static final String ARCHIVE_NAME_CB = 
			project + "cb.jar";
	private static final String ARCHIVE_NAME_DA = 
			project + "da.jar";
	private static final String ARCHIVE_NAME_DB = 
			project + "db.jar";
	private static final String PATH_SUFFIX = " [/" + project + "]"; 
	
	@Before
	public void setup() {
		importArchiveProjectWithoutRuntime(project);
	}
	
	@Test
	public void testDeletingArchiveWithView() {
		
		/* prepare view for testing */
		view = viewForProject(project);
		
		/* delete archive in view with context menu */
		view
			.getProject()
			.getArchive(ARCHIVE_NAME_AA + PATH_SUFFIX)
			.deleteArchive(true);
		
		/* test archive was deleted */
		assertArchiveIsNotInView(view, ARCHIVE_NAME_AA + PATH_SUFFIX);
		
		/* delete archive in view with keyboard shortcut */
//		view.deleteArchive(false, project, ARCHIVE_NAME_AB + PATH_SUFFIX);
		
		/* test archive was deleted */
//		assertItemNotExistsInView(view, project, ARCHIVE_NAME_AB + PATH_SUFFIX);
	}
	
	@Test
	public void testDeletingArchiveWithExplorer() {
		
		/* prepare explorer for testing */
		ProjectArchivesExplorer explorer = explorerForProject(project);
		
		/* delete archive in explorer with context menu*/
		explorer
			.getArchive(ARCHIVE_NAME_BA + PATH_SUFFIX)
			.deleteArchive(true);
		
		/* test archive was deleted */
		assertArchiveIsNotInExplorer(explorer, ARCHIVE_NAME_BA + PATH_SUFFIX);
		
		/* delete archive in explorer with keyboard shortcut */
//		explorer.deleteArchive(false, ARCHIVE_NAME_BB + PATH_SUFFIX);
		
		/* test archive was deleted */
//		assertItemNotExistsInExplorer(explorer, ARCHIVE_NAME_BB + PATH_SUFFIX);
	}
	
//	@Test
//	public void testMultideletionWithView() {
//		
//		/* prepare view for testing */
//		ProjectArchivesView view = viewForProject(project);
//		
//		/* delete archives in view with context menu */
//		view.deleteArchives(true, Arrays.asList(new String[]{
//				ARCHIVE_NAME_CA + PATH_SUFFIX, 
//				ARCHIVE_NAME_CB + PATH_SUFFIX}), 
//				project);
//		
//		/* test that specific archives were deleted with multiselection */
//		assertItemNotExistsInView(view, project, ARCHIVE_NAME_CA + PATH_SUFFIX);
//		assertItemNotExistsInView(view, project, ARCHIVE_NAME_CB + PATH_SUFFIX);
//		
//	}
//	
//	@Test
//	public void testMultideletionWithExplorer() {
//		
//		/* prepare explorer for testing */
//		ProjectArchivesExplorer explorer = explorerForProject(project);
//		
//		/* delete archives in explorer with context menu */
//		explorer.deleteArchives(false, ARCHIVE_NAME_DA + PATH_SUFFIX, ARCHIVE_NAME_DB + PATH_SUFFIX);
//		
//		/* test that specific archives were deleted with multiselection */
//		assertItemNotExistsInExplorer(explorer, ARCHIVE_NAME_DA + PATH_SUFFIX);
//		assertItemNotExistsInExplorer(explorer, ARCHIVE_NAME_DB + PATH_SUFFIX);
//		
//	}
	
	
	
}
