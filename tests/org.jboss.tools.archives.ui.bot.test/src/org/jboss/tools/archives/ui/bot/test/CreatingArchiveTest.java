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

import org.jboss.tools.archives.ui.bot.test.dialog.NewJarDialog;
import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.jboss.tools.ui.bot.ext.entity.JavaProjectEntity;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class CreatingArchiveTest extends ArchivesTestBase {

	private static String project = "pr1";
	
	private final String ARCHIVE_STANDARD_1 = 
			project + "-standard.jar";
	private final String ARCHIVE_STANDARD_2 = 
			project + "-standard-expl.jar";
	private final String ARCHIVE_NO_COMPRESSION_1 = 
			project + "-nocompression.jar";
	private final String ARCHIVE_NO_COMPRESSION_2 = 
			project + "-nocompression-expl.jar";
	
	private final String PATH_SUFFIX = " [/" + project + "]";
	private final String ARCHIVE_STANDARD_1_PATH = 
			ARCHIVE_STANDARD_1 + PATH_SUFFIX;
	private final String ARCHIVE_STANDARD_2_PATH = 
			ARCHIVE_STANDARD_2 + PATH_SUFFIX;
	private final String ARCHIVE_NO_COMPRESSION_1_PATH = 
			ARCHIVE_NO_COMPRESSION_1 + PATH_SUFFIX;
	private final String ARCHIVE_NO_COMPRESSION_2_PATH = 
			ARCHIVE_NO_COMPRESSION_2 + PATH_SUFFIX;
	
	
	@BeforeClass
	public static void setup() {
		JavaProjectEntity jpe = new JavaProjectEntity();
		jpe.setProjectName(project);
		eclipse.createJavaProject(jpe);
	}
	
	@Test 
	public void testCreatingArchive() {
		// workaround because of JBIDE-11927
		testCreatingArchiveWithView();
		testCreatingArchiveWithExplorer();
	}
	
	private void testCreatingArchiveWithView() {
		
		/* prepare view for testing */
		ProjectArchivesView view = viewForProject(project);
		
		/* creating JAR archive from project - standard way */
		NewJarDialog dialog = view.createNewJarArchive(project);
		createArchive(dialog, ARCHIVE_STANDARD_1, true);
		
		/* test archive was created */
		assertItemExistsInView(view, project, ARCHIVE_STANDARD_1_PATH);
		
		/* creating JAR archive from project - no compression way */
		dialog = view.createNewJarArchive(project);
		createArchive(dialog, ARCHIVE_NO_COMPRESSION_1, false);
		
		/* test archive was created */
		assertItemExistsInView(view, project, ARCHIVE_NO_COMPRESSION_1_PATH);
	}
	
	private void testCreatingArchiveWithExplorer() {
		
		/* prepare explorer for testing */
		ProjectArchivesExplorer explorer = explorerForProject(project);
		
		/* creating JAR archive from project - standard way */
		NewJarDialog dialog = explorer.createNewJarArchive();
		createArchive(dialog, ARCHIVE_STANDARD_2, true);
		
		/* test archive was created */
		assertItemExistsInExplorer(explorer, ARCHIVE_STANDARD_2_PATH);
		
		/* creating JAR archive from project - no compression way */
		dialog = explorer.createNewJarArchive();
		createArchive(dialog, ARCHIVE_NO_COMPRESSION_2, false);
		
		/* test archive was created */
		assertItemExistsInExplorer(explorer, ARCHIVE_NO_COMPRESSION_2_PATH);
	}
	
	private void createArchive(NewJarDialog dialog, String archiveName, 
			boolean standardCompression) {
		dialog.setArchiveName(archiveName);
		if (standardCompression) {
			dialog.setZipStandardArchiveType();
		} else {
			dialog.setNoCompressionArchiveType();
		}
		dialog.finish();
	}
	
}
