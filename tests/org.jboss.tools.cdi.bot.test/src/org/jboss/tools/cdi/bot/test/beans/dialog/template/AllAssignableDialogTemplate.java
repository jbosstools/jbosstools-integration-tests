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

package org.jboss.tools.cdi.bot.test.beans.dialog.template;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.cdi.text.ext.hyperlink.AssignableBeansDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.Before;
import org.junit.Test;

public class AllAssignableDialogTemplate extends CDITestBase {

	private String appClass = "App.java";
	
	@Before
	public void prepareClasses(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if(!pe.getProject(PROJECT_NAME).containsResource("Java Resources","src",getPackageName())){
			createWithContent("AbstractManager");
			createWithContent("App");
			createWithContent("Basic");
			createWithContent("BasicManager");
			createWithContent("D1");
			createWithContent("I1");
			createWithContent("IBinding");
			createWithContent("IManager");
			createWithContent("Manager");
			createWithContent("Manager1");
			createWithContent("Manager2");
			createWithContent("Manager3");
			EditorPartWrapper b = beansXMLHelper.openBeansXml(PROJECT_NAME);
			b.addClasses("cdi.Manager1");
			b.save();
		}
	}
	
	private static void createWithContent(String name){
		beansHelper.createClass(name, PACKAGE_NAME);
		editResourceUtil.replaceClassContentByResource(name+".java", 
				readFile("resources/classes/AssignableDialog/"+name+".java"), true);
	}

	@Test
	public void testDecorator() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem("src", "main", "java",
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		ContentAssistant ca = editor.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Decorator D1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));

		assignDialog.hideDecorators();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"@Decorator D1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assignDialog.close();

	}

	@Test
	public void testInterceptor() {

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem("src", "main", "java",
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		ContentAssistant ca = editor.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Interceptor I1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assignDialog.hideInterceptors();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"@Interceptor I1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assignDialog.close();

	}

	@Test
	public void testUnselectedAlternative() {

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem("src", "main", "java",
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		ContentAssistant ca = editor.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Alternative BasicManager - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assignDialog.hideUnselectedAlternatives();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"@Alternative BasicManager - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assignDialog.close();

	}

	@Test
	public void testUnavailableProducer() {

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem("src", "main", "java",
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		ContentAssistant ca = editor.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Produces BasicManager.getManager() - " + getPackageName()
						+ " - /" + getProjectName() + "/src/main/java"));
		assignDialog.hideUnavailableProducers();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"@Produces BasicManager.getManager() - " + getPackageName()
						+ " - /" + getProjectName() + "/src/main/java"));
		assignDialog.close();

	}

	@Test
	public void testSpecializedBeans() {

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem("src", "main", "java",
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		ContentAssistant ca = editor.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"AbstractManager - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assignDialog.hideSpecializedBeans();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"AbstractManager - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assignDialog.close();

	}

	@Test
	public void testAmbiguousBeans() {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem("src", "main", "java",
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("managerImpl");
		ContentAssistant ca = editor.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 3);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Alternative Manager1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assertTrue(assignDialog.getAllBeans().contains(
				"Manager2 - " + getPackageName() + " - /" + getProjectName()
						+ "/src/main/java"));
		assertTrue(assignDialog.getAllBeans().contains(
				"Manager3 - " + getPackageName() + " - /" + getProjectName()
						+ "/src/main/java"));
		assignDialog.hideAmbiguousBeans();
		assertTrue(assignDialog.getAllBeans().size() == 1);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Alternative Manager1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src/main/java"));
		assertFalse(assignDialog.getAllBeans().contains(
				"Manager2 - " + getPackageName() + " - /" + getProjectName()
						+ "/src/main/java"));
		assertFalse(assignDialog.getAllBeans().contains(
				"Manager3 - " + getPackageName() + " - /" + getProjectName()
						+ "/src/main/java"));
		assignDialog.close();

	}
	
	private void assignDialogShowAll(AssignableBeansDialog assignDialog){
		assignDialog.showAmbiguousBeans();
		assignDialog.showDecorators();
		assignDialog.showInterceptors();
		assignDialog.showSpecializedBeans();
		assignDialog.showUnavailableBeans();
		assignDialog.showUnavailableProducers();
		assignDialog.showUnselectedAlternatives();
	}

}
