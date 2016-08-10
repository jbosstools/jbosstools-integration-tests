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
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if creating, modifying and deleting a fileset via 
 * archives view and explorer is possible.
 * 
 * @author jjankovi
 *
 */
public class FilesetTest extends ArchivesTestBase {

	private static final String PROJECT_1 = "FilesetTest1";
	private static final String PROJECT_2 = "FilesetTest2";
	
	private static final String PROJECT_1_ARCHIVE = PROJECT_1 + ".jar";
	
	private static final String PROJECT_1_ARCHIVE_PATH = PROJECT_1_ARCHIVE + 
			" [/" + PROJECT_1 + "]";
	
	private static final String PROJECT_2_ARCHIVE = PROJECT_2 + ".jar";
	
	private static final String PROJECT_2_ARCHIVE_PATH = PROJECT_2_ARCHIVE+ 
			" [/" + PROJECT_2 + "]";
	
	private static final String INCLUDES_1 = ".project";
	private static final String EXCLUDES_1 = "*.jar";
	
	private static final String INCLUDES_2 = "**";
	private static final String EXCLUDES_2 = "*jar";
	
	private static final String INCLUDES_NEW = ".classpath";
	
	@BeforeClass
	public static void setup(){
		createJavaProject(PROJECT_1);
		addArchivesSupport(PROJECT_1);
		createArchive(PROJECT_1, PROJECT_1_ARCHIVE, true);
		
		createJavaProject(PROJECT_2);
		addArchivesSupport(PROJECT_2);
		createArchive(PROJECT_2, PROJECT_2_ARCHIVE, true);
	}
	
	@Test
	public void testCreatingFileset() {
		view = viewForProject(PROJECT_1);
		Archive archiveInView = view.getProject(PROJECT_1).getArchive(PROJECT_1_ARCHIVE_PATH);
		archiveInView.newFileset().setIncludes(INCLUDES_1).setExcludes(EXCLUDES_2).setFlatten(true).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_1, PROJECT_1_ARCHIVE_PATH, 
				formatFileset(PROJECT_1, INCLUDES_1, EXCLUDES_2)));
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_1);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_1_ARCHIVE_PATH);
		archiveInExplorer.newFileset().setIncludes(INCLUDES_2).setExcludes(EXCLUDES_2).setFlatten(true).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_1, "Project Archives", PROJECT_1_ARCHIVE_PATH, 
				formatFileset(PROJECT_1, INCLUDES_2, EXCLUDES_2)));
		
	}
	
	@Test
	public void testModifyingFileset() {
		view = viewForProject(PROJECT_2);
		Archive archiveInView = view.getProject(PROJECT_2).getArchive(PROJECT_2_ARCHIVE_PATH);
		createFileset(archiveInView, PROJECT_2, PROJECT_2_ARCHIVE_PATH, INCLUDES_1, EXCLUDES_1);
		createFileset(archiveInView, PROJECT_2, PROJECT_2_ARCHIVE_PATH, INCLUDES_2, EXCLUDES_2);
		
		String fileset = formatFileset(PROJECT_2, INCLUDES_1, EXCLUDES_1);
		String newFileset = formatFileset(PROJECT_2, INCLUDES_NEW, EXCLUDES_1);
		archiveInView.getFileset(fileset, false).editFileset()
		.setIncludes(INCLUDES_NEW).setExcludes(EXCLUDES_1).finish();
		

		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_2, PROJECT_2_ARCHIVE_PATH, 
				fileset));	
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_2, PROJECT_2_ARCHIVE_PATH, 
				newFileset));
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_2);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_2_ARCHIVE_PATH);
		fileset = formatFileset(PROJECT_2, INCLUDES_2, EXCLUDES_2);
		newFileset = formatFileset(PROJECT_2, INCLUDES_NEW, EXCLUDES_2);
		archiveInExplorer.getFileset(fileset, true).editFileset()
		.setIncludes(INCLUDES_NEW).setExcludes(EXCLUDES_2).finish();
		
		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_2, "Project Archives", PROJECT_2_ARCHIVE_PATH, 
				fileset));	
		new WaitUntil(new TreeContainsItem(new DefaultTree(), PROJECT_2, "Project Archives", PROJECT_2_ARCHIVE_PATH, 
				newFileset));
	}
	
	@Test
	public void testDeletingFileset() {
		view = viewForProject(PROJECT_2);
		Archive archiveInView = view.getProject(PROJECT_2).getArchive(PROJECT_2_ARCHIVE_PATH);
		createFileset(archiveInView, PROJECT_2, PROJECT_2_ARCHIVE_PATH, INCLUDES_1, EXCLUDES_1);
		createFileset(archiveInView, PROJECT_2, PROJECT_2_ARCHIVE_PATH, INCLUDES_2, EXCLUDES_2);
		
		archiveInView.getFileset(formatFileset(PROJECT_2, INCLUDES_1, EXCLUDES_1), false).deleteFileset(true);
		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_2, PROJECT_2_ARCHIVE_PATH, 
				formatFileset(PROJECT_2, INCLUDES_1, EXCLUDES_1)), TimePeriod.LONG);	
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_2);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_2_ARCHIVE_PATH);
		archiveInExplorer.getFileset(formatFileset(PROJECT_2, INCLUDES_2, EXCLUDES_2), true).deleteFileset(true);
		new WaitWhile(new TreeContainsItem(new DefaultTree(), PROJECT_2, "Project Archives", PROJECT_2_ARCHIVE_PATH, 
				formatFileset(PROJECT_2, INCLUDES_2, EXCLUDES_2)), TimePeriod.LONG);	
	}
	
	private String formatFileset(String projectName, String includes, String excludes) {
		return "+[" + includes + "] -[" + excludes + "] : /" + projectName;
	}
	
	private void createFileset(Archive archiveInView, String project, String archivePath, String inclues, String excludes){
		archiveInView.newFileset().setIncludes(inclues).setExcludes(excludes).setFlatten(true).finish();
		new WaitUntil(new TreeContainsItem(new DefaultTree(), project, archivePath, 
				formatFileset(project, inclues, excludes)));
	}
	
}
