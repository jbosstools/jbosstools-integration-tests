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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.cdi.text.ext.hyperlink.AssignableBeansDialog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssignableDialogFilterTemplate extends CDITestBase {

	private String appClass = "App.java";
	
	@Before
	public void prepareClasses(){
		createWithContent("Animal");
		createWithContent("AnimalDecorator");
		createWithContent("App");
		createWithContent("Cat");
		createWithContent("Dog");
	}
	
	@After
	public void deleteAll(){
		deleteAllProjects();
	}
	
	private void createWithContent(String name){
		beansHelper.createClass(name, getPackageName());
		editResourceUtil.replaceClassContentByResource(name+".java", 
				readFile("resources/classes/AssignableDialogFilter/"+name+".java"), true);
	}

	@Test
	public void testFilterAssignableBeans() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor ed = new TextEditor(appClass);
		ed.selectText("animal");
		ContentAssistant ca = ed.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);

		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		/** test lower and upper case */
		assignDialog.typeInFilter("cat");
		assertEquals(1, assignDialog.getAllBeans().size());
		assertTrue(assignDialog
				.getAllBeans()
				.get(0)
				.equals("Cat - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));

		assignDialog.typeInFilter("CAT");
		assertTrue(assignDialog.getAllBeans().size() == 1);
		assertTrue(assignDialog
				.getAllBeans()
				.get(0)
				.equals("Cat - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));

		/** test '*' asterisk */
		assignDialog.typeInFilter("*at");
		assertTrue(assignDialog.getAllBeans().size() == 2);
		assertTrue(assignDialog.getAllBeans().contains(
				"Cat - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));
		assertTrue(assignDialog.getAllBeans().contains(
				"@Decorator AnimalDecorator - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));

		/** test '?' asterisk */
		assignDialog.typeInFilter("??g");
		assertTrue(assignDialog.getAllBeans().size() == 1);
		assertTrue(assignDialog
				.getAllBeans()
				.get(0)
				.equals("Dog - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));

		/** test non-existing bean */
		assignDialog.typeInFilter("?*?s");
		assertTrue(assignDialog.getAllBeans().size() == 0);
		assignDialog.close();

	}

	@Test
	public void testFilterNonAssignableBeans() {

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor ed = new TextEditor(appClass);
		ed.selectText("animal");
		ContentAssistant ca = ed.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);

		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assignDialog.hideDecorators();

		/** test lower and upper case */
		assignDialog.typeInFilter("animaldecorator");
		assertTrue(assignDialog.getAllBeans().size() == 0);

		assignDialog.typeInFilter("ANIMALDECORATOR");
		assertTrue(assignDialog.getAllBeans().size() == 0);

		/** test '*' asterisk */
		assignDialog.typeInFilter("*at");
		assertTrue(assignDialog.getAllBeans().size() == 1);
		assertTrue(assignDialog.getAllBeans().contains(
				"Cat - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));
		assertFalse(assignDialog.getAllBeans().contains(
				"@Decorator AnimalDecorator - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));

		/** test '?' asterisk */
		assignDialog.typeInFilter("??i");
		assertTrue(assignDialog.getAllBeans().size() == 0);
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
