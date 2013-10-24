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

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.core.Is;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.workbench.view.View;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesView;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if state of Project Archives view is not changed when
 * switching various views
 * 
 * @author jjankovi
 *
 */
public class ArchiveViewReSwitchingTest extends ArchivesTestBase {

	private static String projectName = "prj";
	private static LogView logView = new LogView();
	private static ConsoleView consoleView = new ConsoleView();
	private static ServersView serversView = new ServersView();
	
	@BeforeClass
	public static void setup() {
		
		/* show all tested views */
		consoleView.open();
		serversView.open();
		logView.open();
		
		/* create test project */
		createJavaProject(projectName);
	}
	
	@Test
	public void testReSwitchingView() {
		
		/* setup Project Archives view for project */
		view = viewForProject(projectName);
		
		/* test project is included in view */
		assertProjectInArchivesView(view, projectName);
		
		/* test reswitching Project Archives view with some other views */
		reSwitchAndTestArchivesViewWithViews(consoleView, serversView, logView);
		
	}
	
	private void reSwitchAndTestArchivesViewWithViews(View... views) {
		for (View view : views) {
			view.open();
			assertProjectInArchivesView(openProjectArchivesView(), projectName);
		}
	}
	
	private void assertProjectInArchivesView(ProjectArchivesView view, String projectName) {
		try {
			assertThat(view.getProject().getName(), Is.is(projectName));
		} catch (SWTLayerException sle) {
			fail(projectName + " is not shown in Project Archives view");
		}
	}
	
}
