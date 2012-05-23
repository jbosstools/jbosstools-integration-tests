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

import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class BuildingArchiveNode extends ArchivesTestBase {

	private static String projectName = "pr2";
	private String archiveName = projectName + ".jar [/" + projectName + "]";
	
	@BeforeClass
	public static void setup() {
		importProjectWithoutRuntime(projectName);
	}
	
	@Test
	public void testBuildingArchiveNode() {
		ProjectArchivesView view = new ProjectArchivesView();
		view.show();
		projectExplorer.selectProject(projectName);
		view.buildArchiveNode(projectName, archiveName);
	}
	
}
