/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jsf.ui.bot.test.templates;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.handler.TreeItemHandler;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectFirstPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectSecondPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectWizard;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.jst.reddeer.web.ui.navigator.WebProjectsNavigator;
import org.junit.Test;
/**
 * Test creating new JSF project template from existing project
 * 
 * @author Vladimir Pakan
 *
 */
public class CreateNewTemplateFromJSFProject extends JSFAutoTestCase {

	private static final String TEST_PAGE_NAME = "CreateNewTemplateFromJSFProject.jsp";
	private static final String TEMPLATE_NAME = "JsfTestTemplate";
	private static final String TEMPLATE_IMPLEMENTATION = "JSF 1.2";
	private static final String TEMPLATE_TEST_PROJECT_NAME = "newTemplateProject";
	@Test
	public void testCreateNewJSFProject() {
		setProjectName(JBT_TEST_PROJECT_NAME);
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).select();
		createJspPage(CreateNewTemplateFromJSFProject.TEST_PAGE_NAME,JBT_TEST_PROJECT_NAME,"WebContent", "pages");
		WebProjectsNavigator webProjectNavigator = new WebProjectsNavigator();
		webProjectNavigator.open();
		Project testProject = webProjectNavigator.getProject(JBT_TEST_PROJECT_NAME);
		testProject.select();
		TreeItemHandler.getInstance().click(testProject.getTreeItem().getSWTWidget());
		new ContextMenu("JBoss Tools JSF","Save As Template...").select();
		// create template
		new DefaultShell("Add JSF Project Template");
		new DefaultText(0).setText(CreateNewTemplateFromJSFProject.TEMPLATE_NAME);
		new DefaultCombo(0).setText(CreateNewTemplateFromJSFProject.TEMPLATE_IMPLEMENTATION);
		new FinishButton().click();
		// create project using newly created template
		JSFNewProjectWizard jsfNewProjectWizard = new JSFNewProjectWizard();
		jsfNewProjectWizard.open();
		JSFNewProjectFirstPage jsfNewProjectFirstPage = new JSFNewProjectFirstPage();
		jsfNewProjectFirstPage.setProjectName(CreateNewTemplateFromJSFProject.TEMPLATE_TEST_PROJECT_NAME);
		jsfNewProjectFirstPage.setJSFEnvironment(CreateNewTemplateFromJSFProject.TEMPLATE_IMPLEMENTATION);
		jsfNewProjectFirstPage.setProjectTemplate(CreateNewTemplateFromJSFProject.TEMPLATE_NAME);
		jsfNewProjectWizard.next();
		new JSFNewProjectSecondPage()
				.setRuntime(serverRequirement.getRuntimeNameLabelText(serverRequirement.getConfig()));
		jsfNewProjectWizard.finish();
		new WaitWhile(new JobIsRunning());
		packageExplorer.open();
		Project templateTestProject = packageExplorer.getProject(CreateNewTemplateFromJSFProject.TEMPLATE_TEST_PROJECT_NAME);
		templateTestProject.select();
		new ContextMenu("Refresh").select();
		new WaitWhile(new JobIsRunning());
		templateTestProject.getProjectItem("WebContent", "pages", CreateNewTemplateFromJSFProject.TEST_PAGE_NAME).open();
		templateTestProject.delete(true);

	}

}
