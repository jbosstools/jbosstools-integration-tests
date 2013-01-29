/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.utils;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ActiveEditorHasTitleCondition;
import org.jboss.tools.ui.bot.ext.condition.TreeItemContainsNode;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.JavaEEEnterpriseApplicationProject;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWSDL;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewFileWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.TreeItemAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.DynamicWebProjectWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Wizard;

public class ProjectHelper {

	private final SWTBotExt bot = new SWTBotExt();
	
	private final ProjectExplorer projectExplorer = new ProjectExplorer();
	
	private final SWTOpenExt open = new SWTOpenExt(bot);
	
	private final SWTUtilExt util = new SWTUtilExt(bot);
	
	/**
	 * Method creates basic java class for entered project with 
	 * entered package and class name
	 * @param projectName
	 * @param pkg
	 * @param cName
	 * @return
	 */
	public SWTBotEditor createClass(String projectName, String pkg, String cName) {
		new NewFileWizardAction().run().selectTemplate("Java", "Class").next();
		Wizard w = new Wizard();
		w.bot().textWithLabel("Package:").setText(pkg);
		w.bot().textWithLabel("Name:").setText(cName);
		w.bot().textWithLabel("Source folder:")
				.setText(projectName + "/src");
		w.finish();
		bot.waitUntil(new ActiveEditorHasTitleCondition(bot, cName + ".java"));
		return bot.editorByTitle(cName + ".java");
	}

	/**
	 * Method creates wsdl file for entered project with 
	 * entered package name
	 * @param projectName
	 * @param s
	 * @return
	 */
	public SWTBotEditor createWsdl(String projectName, String s) {
		SWTBot wiz1 = open.newObject(WebServicesWSDL.LABEL);
		wiz1.textWithLabel(WebServicesWSDL.TEXT_FILE_NAME).setText(s + ".wsdl");
		wiz1.textWithLabel(
				WebServicesWSDL.TEXT_ENTER_OR_SELECT_THE_PARENT_FOLDER)
				.setText(projectName + "/src");
		wiz1.button(IDELabel.Button.NEXT).click();
		open.finish(wiz1);
		return bot.editorByTitle(s + ".wsdl");
	}
	
	/**
	 * Method creates new Dynamic Web Project with entered name
	 * @param name
	 */
	public void createProject(String name) {
		new NewFileWizardAction().run()
				.selectTemplate("Web", "Dynamic Web Project").next();
		new DynamicWebProjectWizard().setProjectName(name).finish();
		util.waitForNonIgnoredJobs();
		projectExplorer.selectProject(name);
	}
	
	/**
	 * Method creates new Dynamic Web Project with entered name for
	 * ear project
	 * @param name
	 */
	public void createProjectForEAR(String name, String earProject) {
		new NewFileWizardAction().run()
				.selectTemplate("Web", "Dynamic Web Project").next();
		new DynamicWebProjectWizard().setProjectName(name).
			addProjectToEar(earProject).finish();
		util.waitForNonIgnoredJobs();
		projectExplorer.selectProject(name);
	}

	/**
	 * Method creates new EAR Project with entered name
	 * @param name
	 */
	public void createEARProject(String name) {
		SWTBot wiz = open.newObject(JavaEEEnterpriseApplicationProject.LABEL);
		wiz.textWithLabel(JavaEEEnterpriseApplicationProject.TEXT_PROJECT_NAME)
				.setText(name);
		// set EAR version
		SWTBotCombo combo = wiz.comboBox(1);
		combo.setSelection(combo.itemCount() - 1);
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.checkBox("Generate application.xml deployment descriptor").click();
		open.finish(wiz);
		util.waitForNonIgnoredJobs();
		projectExplorer.selectProject(name);
	}
	
	/**
	 * Method generates Deployment Descriptor for entered project 
	 * @param project
	 */
	public void createDD(String project) {
        SWTBotTree tree = projectExplorer.bot().tree();
        SWTBotTreeItem ti = expandProject(tree, project);
        String dd = "Deployment Descriptor: " + project;
        bot.waitUntil(new TreeItemContainsNode(ti, dd), Timing.time10S());
        ti = ti.getNode(dd);
        new TreeItemAction(ti, "Generate Deployment Descriptor Stub").run();
        util.waitForNonIgnoredJobs();
    }
	
	private SWTBotTreeItem expandProject(SWTBotTree tree, String project) {
		SWTBotTreeItem ti = null;
				
		do {
			tree.collapseNode(project);
			ti = tree.expandNode(project);
		}while (!ti.isExpanded());
		
		return ti;
	}
	
}
