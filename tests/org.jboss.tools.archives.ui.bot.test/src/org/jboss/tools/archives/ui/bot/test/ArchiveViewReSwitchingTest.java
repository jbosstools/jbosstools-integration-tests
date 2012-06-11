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
import org.jboss.tools.ui.bot.ext.entity.JavaProjectEntity;
import org.jboss.tools.ui.bot.ext.view.ErrorLogView;
import org.jboss.tools.ui.bot.ext.view.ViewBase;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * 
 * @author jjankovi
 *
 */
public class ArchiveViewReSwitchingTest extends ArchivesTestBase {

	private static String projectName = "prj";
	private static ErrorLogView errors = new ErrorLogView();
	
	@BeforeClass
	public static void setup() {
		
		/* show all tested views */
		console.show();
		servers.show();
		errors .show();
		
		/* create test project */
		JavaProjectEntity project = new JavaProjectEntity();
		project.setProjectName(projectName);
		eclipse.createJavaProject(project);
	}
	
	@Test
	public void testReSwitchingView() {
		
		/* setup Project Archives view for project */
		ProjectArchivesView view = viewForProject(projectName);
		
		/* test project is included in view */
		assertItemExistsInView(view, projectName);
		
		/* test reswitching Project Archives view with some other views */
		reSwitchAndTestArchivesViewWithViews(console, servers, errors);
		
	}
	
	private void reSwitchAndTestArchivesViewWithViews(ViewBase... views) {
		for (ViewBase view : views) {
			view.show();
			ProjectArchivesView archiveView = openProjectArchivesView();
			assertItemExistsInView(archiveView, projectName);
		}
	}
	
}
