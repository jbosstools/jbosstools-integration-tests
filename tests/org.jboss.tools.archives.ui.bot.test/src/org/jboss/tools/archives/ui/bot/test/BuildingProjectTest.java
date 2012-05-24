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
public class BuildingProjectTest extends ArchivesTestBase {

	private static String projectName = "pr1";
	
	@BeforeClass
	public static void setup() {
		importProjectWithoutRuntime(projectName);
	}
	
	@Test
	public void testBuildingProjectWithView() {
		viewForProject(projectName).buildProjectFull(projectName);
	}
	
	@Test
	public void testBuildingProjectWithExplorer() {
		explorerForProject(projectName).buildProjectFull();
	}
	
}
