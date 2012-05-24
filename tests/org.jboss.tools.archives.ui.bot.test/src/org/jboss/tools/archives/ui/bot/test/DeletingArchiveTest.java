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

	private static String project = "pr3";
	private String pathToArchive1 = project + "a.jar [/" + project + "]";
	private String pathToArchive2 = project + "b.jar [/" + project + "]";
	
	@BeforeClass
	public static void setup() {
		importProjectWithoutRuntime(project);
	}
	
	@Test
	public void testDeletingArchivetWithView() {
		ProjectArchivesView view = viewForProject(project);
		view.deleteArchive(project, pathToArchive1);
		assertFalse(view.itemExists(project, pathToArchive1));
	}
	
	@Test
	public void testDeletingArchiveWithExplorer() {
		ProjectArchivesExplorer explorer = explorerForProject(project);
		explorer.deleteArchive(pathToArchive2);
		assertFalse(explorer.itemExists(pathToArchive1));
	}
	
}
