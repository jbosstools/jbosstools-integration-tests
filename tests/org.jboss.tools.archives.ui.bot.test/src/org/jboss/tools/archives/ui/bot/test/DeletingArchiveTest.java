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

import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
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
	private static final String PATH_SUFFIX = " [/" + project + "]"; 
	
	@BeforeClass
	public static void setup() {
		importProjectWithoutRuntime(project);
	}
	
	@Test
	public void testDeletingArchiveWithView() {
		
		/* prepare view for testing */
		ProjectArchivesView view = viewForProject(project);
		
		/* delete archive in view with context menu */
		view.deleteArchive(true, project, ARCHIVE_NAME_AA + PATH_SUFFIX);
		
		/* test archive was deleted */
		assertItemNotExistsInView(view, project, ARCHIVE_NAME_AA + PATH_SUFFIX);
		
		/* delete archive in view with keyboard shortcut */
		view.deleteArchive(false, project, ARCHIVE_NAME_AB + PATH_SUFFIX);
		
		/* test archive was deleted */
		assertItemNotExistsInView(view, project, ARCHIVE_NAME_AB + PATH_SUFFIX);
	}
	
	@Test
	public void testDeletingArchiveWithExplorer() {
		
		/* prepare explorer for testing */
		ProjectArchivesExplorer explorer = explorerForProject(project);
		
		/* delete archive in explorer with context menu*/
		explorer.deleteArchive(true, ARCHIVE_NAME_BA + PATH_SUFFIX);
		
		/* test archive was deleted */
		assertItemNotExistsInExplorer(explorer, ARCHIVE_NAME_BA + PATH_SUFFIX);
		
		/* delete archive in explorer with keyboard shortcut */
		explorer.deleteArchive(false, ARCHIVE_NAME_BB + PATH_SUFFIX);
		
		/* test archive was deleted */
		assertItemNotExistsInExplorer(explorer, ARCHIVE_NAME_BB + PATH_SUFFIX);
	}
	
}
