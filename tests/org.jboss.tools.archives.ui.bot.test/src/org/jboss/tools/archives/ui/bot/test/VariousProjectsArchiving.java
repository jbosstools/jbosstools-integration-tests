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

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.INewObject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ErrorLogView;
import org.junit.After;
import org.junit.Test;

/**
 * Tests if creating an archive for various types of projects is possible
 * without archive errors
 * 
 * @author jjankovi
 *
 */
@Require(clearProjects = true, perspective = "Java",
		 server = @Server(state = ServerState.NotRunning, 
		 version = "6.0", operator = ">="))
public class VariousProjectsArchiving extends ArchivesTestBase {
	
	@After
	public void checkErrorLog() {
		assertClearArchivesErrorLog();
	}
	
	@Test
	public void testDynamicWebProject() {
		String project = "pr1";
		
		/* create dynamic web project */
		createDynamicWebProject(project);
		
		/* clear error view before creating an archive */
		clearErrorView();
		
		/* open view for project */
		view = viewForProject(project);
		
		/* create archive */
		view
			.getProject()
			.newJarArchive()
			.finish();
		
		/* test if archive was created and no error was thrown*/
		assertArchiveIsInView(view, project + ".jar [/" + project + "]");
	}
	
	@Test
	public void testEJBProject() {
		String project = "pr2";
		
		/* create ejb project */
		createEJBProject(project);
		
		/* clear error view before creating an archive */
		clearErrorView();
		
		/* open view for project */
		view = viewForProject(project);
		
		/* create archive */
		view
			.getProject()
			.newJarArchive()
			.finish();
		
		/* test if archive was created and no error was thrown*/
		assertArchiveIsInView(view, project + ".jar [/" + project + "]");
	}
	
	private void createDynamicWebProject(String project) {
		createProject(ActionItem.NewObject.
				WebDynamicWebProject.LABEL, project);
	}
	
	private void createEJBProject(String project) {
		createProject(ActionItem.NewObject.
				EJBEJBProject.LABEL, project);
		
	}
	
	private void createProject(INewObject object, 
			String project) {
		SWTBot bot = open.newObject(object);
		SWTBotText t = bot.textWithLabel("Project name:");
		t.setFocus();
		t.setText(project);
		bot.button(IDELabel.Button.FINISH).click();
		handlePerspectivePopUpDialog();
		util.waitForNonIgnoredJobs();
	}
	
	private void handlePerspectivePopUpDialog() {
		try {
			bot.waitForShell(IDELabel.Shell.OPEN_ASSOCIATED_PERSPECTIVE);
			bot.shell(IDELabel.Shell.OPEN_ASSOCIATED_PERSPECTIVE)
				.bot().button(IDELabel.Button.NO).click();
		} catch (WidgetNotFoundException exc) {
			//do nothing here
		}
	}
	
	private void assertClearArchivesErrorLog() {
		
		assertTrue("Error log contains archive errors", 
				countOfArchivesErrors() == 0);
	}
	
	private int countOfArchivesErrors() {
		ErrorLogView errorLog = new ErrorLogView();
		int archivesErrorsCount = 0;
		for (SWTBotTreeItem ti : errorLog.getMessages()) {
			String pluginId = ti.cell(1);
			if (pluginId.contains("org.jboss.ide.eclipse.archives")) {
				archivesErrorsCount++;
			}
		}
		return archivesErrorsCount;
	}
}
