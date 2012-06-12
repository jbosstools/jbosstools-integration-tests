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

import org.jboss.tools.archives.ui.bot.test.dialog.FilesetDialog;
import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class FilesetTest extends ArchivesTestBase {

	private static String projectName = "pr2";
	
	private final String PATH_SUFFIX = " [/" + projectName + "]"; 
	private final String ARCHIVE_NAME = projectName + ".jar";
	private final String ARCHIVE_PATH = 
			ARCHIVE_NAME + PATH_SUFFIX;
	
	@BeforeClass
	public static void setup() {
		importProjectWithoutRuntime(projectName);
	}
	
	
	@Test
	public void testCreatingFileSetInView() {
		String includes = "**";
		String excludes = "*.jar";
		
		/* prepare view for testing */
		ProjectArchivesView view = viewForProject(projectName);
		
		/* create folder */
		String fileset = createFileset(view.createFileset(projectName, ARCHIVE_PATH), 
				includes, excludes, true);
		
		/* test if folder was created */
		assertItemExistsInView(view, projectName, ARCHIVE_PATH, fileset + projectName);
	}
	
	@Test
	public void testCreatingFileSetInExplorer() {
		String includes = "**";
		String excludes = "*.jar, .svn/**";
		
		/* prepare view for testing */
		ProjectArchivesExplorer explorer = explorerForProject(projectName);
		
		/* create folder */
		String fileset = createFileset(explorer.createFileset(ARCHIVE_PATH), 
				includes, excludes, true);
		
		/* test if folder was created */
		assertItemExistsInExplorer(explorer, ARCHIVE_PATH, fileset + projectName);
	}
	
	private String createFileset(FilesetDialog dialog, String includes,
			String excludes, boolean setFlatten) {
		dialog.setIncludes(includes).setExcludes(excludes).
			   setFlatten(setFlatten).finish();
		return "+[" + includes + "] -[" + excludes + "] : /";
	}
	
}
