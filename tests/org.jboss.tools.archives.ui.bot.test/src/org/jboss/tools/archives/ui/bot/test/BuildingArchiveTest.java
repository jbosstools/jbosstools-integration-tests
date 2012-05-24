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

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class BuildingArchiveTest extends ArchivesTestBase{

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
	public void testBuildingArchiveWithView() {
		viewForProject(projectName)
			.buildArchiveFull(projectName, ARCHIVE_PATH);
	}
	
	@Test
	public void testBuildingArchiveWithExplorer() {
		explorerForProject(projectName)
			.buildArchiveFull(ARCHIVE_PATH);
	}
	
}
