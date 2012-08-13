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

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.INewObject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.After;
import org.junit.Test;

/**
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
		assertClearErrorLog();
	}
	
	@Test
	public void testDynamicWebProject() {
		String project = "pr1";
		
		/* create dynamic web project */
		createDynamicWebProject(project);
		
		/* clear error view before creating an archive */
		clearErrorView();
		
		/* open view for project */
		ProjectArchivesView view = viewForProject(project);
		
		/* create archive */
		view.createNewJarArchive(project).finish();
		
		/* workaround JBIDE-11878 */
		view = viewForProject(project);
		
		/* test if archive was created and no error was thrown*/
		assertItemExistsInView(view, 
				project, project + ".jar [/" + project + "]");
	}
	
	@Test
	public void testEJBProject() {
		String project = "pr2";
		
		/* create ejb project */
		createEJBProject(project);
		
		/* clear error view before creating an archive */
		clearErrorView();
		
		/* open view for project */
		ProjectArchivesView view = viewForProject(project);
		
		/* create archive */
		view.createNewJarArchive(project).finish();
		
		/* workaround JBIDE-11878 */
		view = viewForProject(project);
		
		/* test if archive was created and no error was thrown*/
		assertItemExistsInView(view, 
				project, project + ".jar [/" + project + "]");
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
}
