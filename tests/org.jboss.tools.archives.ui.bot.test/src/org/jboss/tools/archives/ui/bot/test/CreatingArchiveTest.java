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

import org.jboss.tools.archives.reddeer.archives.ui.NewJarDialog;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if creating an archive via archives view and explorer is possible
 * 
 * @author jjankovi
 *
 */
public class CreatingArchiveTest extends ArchivesTestBase {

	private static String projectName = "CreatingArchiveTest";
	
	private final String ARCHIVE_STANDARD_1 = projectName + "-standard.jar";
	private final String ARCHIVE_STANDARD_2 = projectName + "-standard-expl.jar";
	private final String ARCHIVE_NO_COMPRESSION_1 = projectName + "-nocompression.jar";
	private final String ARCHIVE_NO_COMPRESSION_2 = projectName + "-nocompression-expl.jar";
	
	private final String PATH_SUFFIX = " [/" + projectName + "]";
	private final String ARCHIVE_STANDARD_1_PATH = ARCHIVE_STANDARD_1 + PATH_SUFFIX;
	private final String ARCHIVE_STANDARD_2_PATH = ARCHIVE_STANDARD_2 + PATH_SUFFIX;
	private final String ARCHIVE_NO_COMPRESSION_1_PATH = ARCHIVE_NO_COMPRESSION_1 + PATH_SUFFIX;
	private final String ARCHIVE_NO_COMPRESSION_2_PATH = ARCHIVE_NO_COMPRESSION_2 + PATH_SUFFIX;
	
	
	@BeforeClass
	public static void setup() {
		createJavaProject(projectName);
		addArchivesSupport(projectName);
	}
	
	@Test
	public void testCreatingArchiveWithView() {
		view = viewForProject(projectName);
		NewJarDialog dialog = view.getProject(projectName).newJarArchive();
		createArchive(dialog, ARCHIVE_STANDARD_1, true);
		assertArchiveIsInView(projectName, view, ARCHIVE_STANDARD_1_PATH);
		dialog = view.getProject(projectName).newJarArchive();
		createArchive(dialog, ARCHIVE_NO_COMPRESSION_1, false);
		assertArchiveIsInView(projectName, view, ARCHIVE_NO_COMPRESSION_1_PATH);
	}
	
	@Test
	public void testCreatingArchiveWithExplorer() {
		ProjectArchivesExplorer explorer = explorerForProject(projectName);
		NewJarDialog dialog = explorer.newJarArchive();
		createArchive(dialog, ARCHIVE_STANDARD_2, true);
		assertArchiveIsInExplorer(explorer, ARCHIVE_STANDARD_2_PATH);
		dialog = explorer.newJarArchive();
		createArchive(dialog, ARCHIVE_NO_COMPRESSION_2, false);
		assertArchiveIsInExplorer(explorer, ARCHIVE_NO_COMPRESSION_2_PATH);
	}
	
}
