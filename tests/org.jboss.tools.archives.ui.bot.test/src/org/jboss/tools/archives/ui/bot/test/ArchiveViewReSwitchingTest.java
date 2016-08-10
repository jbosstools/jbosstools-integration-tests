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

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.workbench.api.View;
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

	private static String projectName = "ArchiveViewReSwitchingTest";
	private static LogView errorsView = new LogView();
	private static ConsoleView consoleView = new ConsoleView();
	private static ServersView serversView = new ServersView();
	
	@BeforeClass
	public static void setup() {
		consoleView.open();
		serversView.open();
		errorsView.open();
		createJavaProject(projectName);
	}
	
	@Test
	public void testReSwitchingView() {
		view = viewForProject(projectName);
		assertProjectInArchivesView(view, projectName);
		reSwitchAndTestArchivesViewWithViews(consoleView, serversView, errorsView);
		
	}
	
	private void reSwitchAndTestArchivesViewWithViews(View... views) {
		for (View view : views) {
			view.open();
			assertProjectInArchivesView(openProjectArchivesView(), projectName);
		}
	}
	
	private void assertProjectInArchivesView(ProjectArchivesView view, String projectName) {
		try {
			view.activate();
			view.getProject(projectName);
		} catch (RedDeerException sle) {
			fail(projectName + " is not shown in Project Archives view");
		}
	}
	
}
