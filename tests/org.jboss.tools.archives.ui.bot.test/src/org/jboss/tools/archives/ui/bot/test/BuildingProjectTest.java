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

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests if building project via archives view and explorer is possible
 * 
 * @author jjankovi
 *
 */
public class BuildingProjectTest extends ArchivesTestBase {

	private static String projectName = "pr1";
	private final String ARCHIVE_NAME = projectName + ".jar";
	
	@Before
	public void setup() {
		importArchiveProjectWithoutRuntime(projectName);
	}
	
	@Test
	public void testBuildingProjectWithView() {
		viewForProject(projectName)
			.getProject()
			.buildProjectFull();
		projectExplorer.open();
		assertTrue(projectExplorer.getProject(projectName).containsItem(ARCHIVE_NAME));
	}
	
	@Test
	public void testBuildingProjectWithExplorer() {
		explorerForProject(projectName)
			.buildProjectFull();
		projectExplorer.open();
		assertTrue(projectExplorer.getProject(projectName).containsItem(ARCHIVE_NAME));
	}
	
}
