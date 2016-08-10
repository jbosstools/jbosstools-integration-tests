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

import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.junit.BeforeClass;
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
public class DeletingArchiveTest extends ArchivesTestBase {

	private static String projectName = "DeletingArchiveTest";
	private static final String ARCHIVE_NAME_AA = projectName + "aa.jar";
	private static final String ARCHIVE_NAME_AB = projectName + "ab.jar";
	private static final String ARCHIVE_NAME_BA = projectName + "ba.jar";
	private static final String ARCHIVE_NAME_BB = projectName + "bb.jar";
	private static final String ARCHIVE_NAME_CA = projectName + "ca.jar";
	private static final String ARCHIVE_NAME_CB = projectName + "cb.jar";
	private static final String ARCHIVE_NAME_DA = projectName + "da.jar";
	private static final String ARCHIVE_NAME_DB = projectName + "db.jar";
	private static final String PATH_SUFFIX = " [/" + projectName + "]"; 
	
	@BeforeClass
	public static void setup() {
		createJavaProject(projectName);
		addArchivesSupport(projectName);
		createArchive(projectName, ARCHIVE_NAME_AA, true);
		createArchive(projectName, ARCHIVE_NAME_AB, true);
		createArchive(projectName, ARCHIVE_NAME_BA, true);
		createArchive(projectName, ARCHIVE_NAME_BB, true);
		createArchive(projectName, ARCHIVE_NAME_CA, true);
		createArchive(projectName, ARCHIVE_NAME_CB, true);
		createArchive(projectName, ARCHIVE_NAME_DA, true);
		createArchive(projectName, ARCHIVE_NAME_DB, true);
	}
	
	@Test
	public void testDeletingArchiveWithView() {
		view = viewForProject(projectName);
		view.getProject(projectName).getArchive(ARCHIVE_NAME_AA + PATH_SUFFIX).deleteArchive(true);
		assertArchiveIsNotInView(projectName, view, ARCHIVE_NAME_AA + PATH_SUFFIX);
	}
	
	@Test
	public void testDeletingArchiveWithExplorer() {
		ProjectArchivesExplorer explorer = explorerForProject(projectName);
		explorer.getArchive(ARCHIVE_NAME_BA + PATH_SUFFIX).deleteArchive(true);
		assertArchiveIsNotInExplorer(explorer, ARCHIVE_NAME_BA + PATH_SUFFIX);
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
