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

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if building archive node via archives view is possible
 * 
 * @author jjankovi
 *
 */
public class BuildingArchiveNode extends ArchivesTestBase {

	private static String projectName = "BuildingArchiveNode";
	
	private static final String PATH_SUFFIX = " [/" + projectName + "]"; 
	private static final String ARCHIVE_NAME = projectName + ".jar";
	private static final String ARCHIVE_PATH = ARCHIVE_NAME + PATH_SUFFIX;
	
	@BeforeClass
	public static void setup() {
		createJavaProject(projectName);
		addArchivesSupport(projectName);
		createArchive(projectName, ARCHIVE_NAME, true);
	}
	
	@Test
	public void testBuildingArchiveNode() {
		view = viewForProject(projectName);
		view.getProject(projectName).getArchive(ARCHIVE_PATH);
		view.buildArchiveNode();
		projectExplorer.open();
		assertTrue(projectExplorer.getProject(projectName).containsItem(ARCHIVE_NAME));
	}
	
}
