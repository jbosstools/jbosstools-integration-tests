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

	private static String project1 = "pr1";
	
	@BeforeClass
	public static void setup() {
		JavaProjectEntity jpe = new JavaProjectEntity();
		jpe.setProjectName(project1);
		eclipse.createJavaProject(jpe);
	}
	
	@Test
	public void testCreatingArchivetWithView() {
		ProjectArchivesView view = new ProjectArchivesView();
		view.show();
		projectExplorer.selectProject(project1);
		
		/* creating JAR archive from project1 - standard way */
		NewJarDialog dialog = view.createNewJarArchive(project1);
		dialog.setArchiveName(project1 + "-standard");
		dialog.setZipStandardArchiveType();
		dialog.finish();
		assertTrue(view.itemExists(project1, 
				project1 + "-standard.jar [/" + project1 + "]"));
		
		/* creating JAR archive from project1 - no compression way */
		dialog = view.createNewJarArchive(project1);
		dialog.setArchiveName(project1 + "-nocompression");
		dialog.setNoCompressionArchiveType();
		dialog.finish();
		assertTrue(view.itemExists(project1, 
				project1 + "-nocompression.jar [/" + project1 + "]"));
	}
	
	@Test
	public void testCreatingArchiveWithExplorer() {
		ProjectArchivesExplorer explorer = new ProjectArchivesExplorer(project1);
		
		/* creating JAR archive from project1 - standard way */
		NewJarDialog dialog = explorer.createNewJarArchive();
		dialog.setArchiveName(project1 + "-standard-expl");
		dialog.setZipStandardArchiveType();
		dialog.finish();
		assertTrue(explorer.itemExists(
				project1 + "-standard-expl.jar [/" + project1 + "]"));
		
		/* creating JAR archive from project1 - no compression way */
		dialog = explorer.createNewJarArchive();
		dialog.setArchiveName(project1 + "-nocompression-expl");
		dialog.setNoCompressionArchiveType();
		dialog.finish();
		assertTrue(explorer.itemExists(
				project1 + "-nocompression-expl.jar [/" + project1 + "]"));
	}
	
}
