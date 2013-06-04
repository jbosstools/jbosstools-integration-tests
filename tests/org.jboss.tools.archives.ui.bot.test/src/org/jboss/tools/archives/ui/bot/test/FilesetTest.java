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

import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.jboss.tools.archives.ui.bot.test.condition.FilesetIsInArchive;
import org.junit.After;
import org.junit.Test;

/**
 * Tests if creating, modifying and deleting a fileset via 
 * archives view and explorer is possible.
 * 
 * @author jjankovi
 *
 */
public class FilesetTest extends ArchivesTestBase {

	private static final String PROJECT_1 = "pr2";
	private static final String PROJECT_2 = "pr5";
	
	private static final String PROJECT_1_ARCHIVE_PATH = PROJECT_1 + ".jar" + 
			" [/" + PROJECT_1 + "]";
	private static final String PROJECT_2_ARCHIVE_PATH = PROJECT_2 + ".jar" + 
			" [/" + PROJECT_2 + "]";
	
	private static final String INCLUDES_1 = ".project";
	private static final String EXCLUDES_1 = "*.jar";
	
	private static final String INCLUDES_2 = "**";
	private static final String EXCLUDES_2 = "*jar";
	
	private static final String INCLUDES_NEW = ".classpath";
	
	@After
	public void cleanUp() {
		for (Project project : projectExplorer.getProjects()) {
			project.delete(true);
		}
	}
	
	@Test
	public void testCreatingFileset() {
		importArchiveProjectWithoutRuntime(PROJECT_1);
		
		view = viewForProject(PROJECT_1);
		Archive archiveInView = view.getProject().getArchive(PROJECT_1_ARCHIVE_PATH);
		testCreatingFileset(archiveInView, PROJECT_1, INCLUDES_1, EXCLUDES_2);
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_1);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_1_ARCHIVE_PATH);
		testCreatingFileset(archiveInExplorer, PROJECT_1, INCLUDES_2, EXCLUDES_2);
		
	}
	
	@Test
	public void testModifyingFileset() {
		importArchiveProjectWithoutRuntime(PROJECT_2);
		
		view = viewForProject(PROJECT_2);
		Archive archiveInView = view.getProject().getArchive(PROJECT_2_ARCHIVE_PATH);
		testModifyFileset(archiveInView, PROJECT_2, 
				formatFileset(PROJECT_2, INCLUDES_1, EXCLUDES_1), INCLUDES_NEW, EXCLUDES_1);
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_2);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_2_ARCHIVE_PATH);
		testModifyFileset(archiveInExplorer, PROJECT_2, 
				formatFileset(PROJECT_2, INCLUDES_2, EXCLUDES_2), INCLUDES_NEW, EXCLUDES_2);
	}
	
	@Test
	public void testDeletingFileset() {
		importArchiveProjectWithoutRuntime(PROJECT_2);
		
		view = viewForProject(PROJECT_2);
		Archive archiveInView = view.getProject().getArchive(PROJECT_2_ARCHIVE_PATH);
		testDeleteFileset(archiveInView, formatFileset(PROJECT_2, INCLUDES_1, EXCLUDES_1));
		
		ProjectArchivesExplorer explorer = explorerForProject(PROJECT_2);
		Archive archiveInExplorer = explorer.getArchive(PROJECT_2_ARCHIVE_PATH);
		testDeleteFileset(archiveInExplorer, formatFileset(PROJECT_2, INCLUDES_2, EXCLUDES_2));
	}
	
	private void testCreatingFileset(Archive archive, String projectName, 
			String includes, String excludes) {
		
		archive.newFileset().setIncludes(includes)
			.setExcludes(excludes).setFlatten(true).finish();
		
		try {
			new WaitUntil(new FilesetIsInArchive(
					archive, formatFileset(projectName, includes, excludes)));
		} catch (TimeoutException te) {
			fail("'" + formatFileset(projectName, includes, excludes) 
					 + "' was not created under archive '" 
					 + archive.getName() + "'!");
		}
	}
	
	private void testModifyFileset(Archive archive, String projectName, 
			String filesetName, String includes, String excludes) {
		
		archive.getFileset(filesetName).editFileset()
			.setIncludes(includes).setExcludes(excludes).finish();
	
		try {
			new WaitWhile(new FilesetIsInArchive(archive, filesetName));
			new WaitUntil(new FilesetIsInArchive(archive, 
					formatFileset(projectName, includes, excludes)));
		} catch (TimeoutException te) {
			fail("'" + filesetName
					 + "' was not modified to '" 
					 + formatFileset(projectName, includes, excludes) 
					 + "' under archive '" + archive.getName() + "'!");
		}
	}
	
	private void testDeleteFileset(Archive archive, String filesetName) {
		
		archive.getFileset(filesetName).deleteFileset(true);
		
		try {
			new WaitWhile(new FilesetIsInArchive(archive, filesetName));
		} catch (TimeoutException te) {
			fail("'" + filesetName 
					 + "' was not deleted under archive '" 
					 + archive.getName() + "'!");
		}
	}
	
	private String formatFileset(String projectName, String includes, String excludes) {
		return "+[" + includes + "] -[" + excludes + "] : /" + projectName;
	}
	
}
