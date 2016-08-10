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
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.EditArchiveDialog;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if modifying an archive via 
 * archives view and explorer is possible.
 * 
 * @author jjankovi
 *
 */
public class ModifyingArchiveTest extends ArchivesTestBase {

	private static String project = "pr3";
	private static final String ARCHIVE_NAME_1 = project + "a.jar";
	private static final String ARCHIVE_NAME_2 = project + "b.jar";
	private static final String ARCHIVE_NAME_1_NEW = project + "a-new.jar";
	private static final String ARCHIVE_NAME_2_NEW = project + "b-new.jar";
	
	private static final String PATH_SUFFIX = " [/" + project + "]"; 
	private static final String PATH_ARCHIVE_1 = ARCHIVE_NAME_1 + PATH_SUFFIX;
	private static final String PATH_ARCHIVE_2 = ARCHIVE_NAME_2 + PATH_SUFFIX;
	private static final String PATH_ARCHIVE_1_NEW = ARCHIVE_NAME_1_NEW + PATH_SUFFIX;
	private static final String PATH_ARCHIVE_2_NEW = ARCHIVE_NAME_2_NEW + PATH_SUFFIX;
	
	@BeforeClass
	public static void setup(){
		createJavaProject(project);
		addArchivesSupport(project);
		createArchive(project, ARCHIVE_NAME_1, true);
		createArchive(project, ARCHIVE_NAME_2, true);
	}
	
	@Test
	public void testModifyingArchiveWithView() {
		
		view = viewForProject(project);
		
		EditArchiveDialog dialog = view.getProject(project).getArchive(PATH_ARCHIVE_1).editArchive();
		editArchive(dialog, ARCHIVE_NAME_1_NEW);
		
		new WaitWhile(new TreeContainsItem(new DefaultTree(), project, PATH_ARCHIVE_1));
		new WaitUntil(new TreeContainsItem(new DefaultTree(), project, PATH_ARCHIVE_1_NEW));
	}
	
	@Test
	public void testModifyingArchiveWithExplorer() {
		ProjectArchivesExplorer explorer = new ProjectArchivesExplorer(project);
		
		EditArchiveDialog dialog = explorer.getArchive(PATH_ARCHIVE_2).editArchive();
		editArchive(dialog, ARCHIVE_NAME_2_NEW);
	
		new WaitWhile(new TreeContainsItem(new DefaultTree(), project, "Project Archives",PATH_ARCHIVE_2));
		new WaitUntil(new TreeContainsItem(new DefaultTree(), project, "Project Archives",PATH_ARCHIVE_2_NEW));
	}
	
	private void editArchive(EditArchiveDialog dialog, String newArchiveName) {
		dialog.setArchiveName(newArchiveName);
		dialog.finish();
	}
	
}
