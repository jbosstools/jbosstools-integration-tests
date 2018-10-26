/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beans.named.teplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.CDIRefactorWizard;
import org.jboss.tools.cdi.reddeer.condition.ContextMenuIsEnabled;
import org.jboss.tools.cdi.reddeer.uiutils.CollectionsUtil;
import org.jboss.tools.cdi.reddeer.xhtml.NewXHTMLFileWizardPage;
import org.jboss.tools.cdi.reddeer.xhtml.NewXHTMLWizard;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.After;
import org.junit.Test;

public abstract class NamedRefactoringTemplate extends CDITestBase {//extends JSFTestBase{

	private static final String MANAGED_BEAN_1 = "ManagedBean1";
	private static final String MANAGED_BEAN_2 = "ManagedBean2";
	private static final String INDEX_XHTML_1= "index1.xhtml";
	private static final String INDEX_XHTML_2= "index2.xhtml";
	private static final String INDEX_XHTML_3= "index3.xhtml";
	private static final String NEW_NAMED_PARAM = "bean2";

	@After
	public void waitForJobs() {
		deleteAllProjects();
	}
	
	@Test
	public void testNamedAnnotationWithParamRefactor() {
		beansHelper.createClass(MANAGED_BEAN_1, getPackageName());
		editResourceUtil.replaceClassContentByResource(MANAGED_BEAN_1+".java",
				readFile("resources/jsf/ManagedBeanParamNamed.java.cdi"), false);
		
		createXHTMLPage(INDEX_XHTML_1);
		createXHTMLPage(INDEX_XHTML_3);
	
		Collection<String> affectedFiles = changeNamedAnnotation(MANAGED_BEAN_1, 
				NEW_NAMED_PARAM);
		Collection<String> expectedAffectedFiles = Arrays.asList(
				MANAGED_BEAN_1 + ".java", INDEX_XHTML_1, INDEX_XHTML_3);
	
		assertEquals(expectedAffectedFiles.size(), affectedFiles.size());
		assertTrue(CollectionsUtil.compareTwoCollectionsEquality(
				expectedAffectedFiles, affectedFiles));
		
		assertTrue(new TextEditor(MANAGED_BEAN_1 + ".java").getText().
			contains("@Named(\"" + NEW_NAMED_PARAM + "\""));
		
		assertTrue(new TextEditor(INDEX_XHTML_1).getText().
				contains("#{" + NEW_NAMED_PARAM));
		
		assertTrue(new TextEditor(INDEX_XHTML_3).getText().
				contains("#{" + NEW_NAMED_PARAM));
		
	}
	
	@Test
	public void testNamedAnnotationWithoutParamRefactor() {
		
		beansHelper.createClass(MANAGED_BEAN_2, getPackageName());
		editResourceUtil.replaceClassContentByResource(MANAGED_BEAN_2+".java",
				readFile("resources/jsf/ManagedBeanNoParamNamed.java.cdi"), false);

		createXHTMLPage(INDEX_XHTML_2);
		createXHTMLPage(INDEX_XHTML_3);
		
		Collection<String> affectedFiles = changeNamedAnnotation(MANAGED_BEAN_2, NEW_NAMED_PARAM);
		Collection<String> expectedAffectedFiles = Arrays.asList(
				MANAGED_BEAN_2 + ".java", INDEX_XHTML_2, INDEX_XHTML_3);
	
		assertEquals(expectedAffectedFiles.size(), affectedFiles.size());
		assertTrue(CollectionsUtil.compareTwoCollectionsEquality(
				expectedAffectedFiles, affectedFiles));
		
		assertTrue(new TextEditor(MANAGED_BEAN_2 + ".java").getText().
			contains("@Named(\"" + NEW_NAMED_PARAM + "\""));
		
		assertTrue(new TextEditor(INDEX_XHTML_2).getText().
				contains("#{" + NEW_NAMED_PARAM));
		
		assertTrue(new TextEditor(INDEX_XHTML_3).getText().
				contains("#{" + NEW_NAMED_PARAM));
		
	}
	
	@Test
	public void testNamedAnnotationWithoutELRefactoring() {
		
		beansHelper.createClass(MANAGED_BEAN_2, getPackageName());
		editResourceUtil.replaceClassContentByResource(MANAGED_BEAN_2+".java",
				readFile("resources/jsf/ManagedBeanNoParamNamed.java.cdi"), false);	
		
		createXHTMLPage(INDEX_XHTML_2);		
			
		Collection<String> affectedFiles = changeNamedAnnotation(MANAGED_BEAN_2, NEW_NAMED_PARAM);
		Collection<String> expectedAffectedFiles = Arrays.asList(MANAGED_BEAN_2 + ".java", INDEX_XHTML_2);
	
		assertEquals(expectedAffectedFiles.size(), affectedFiles.size());
		assertTrue(CollectionsUtil.compareTwoCollectionsEquality(
				expectedAffectedFiles, affectedFiles));
		
		assertTrue(new TextEditor(MANAGED_BEAN_2 + ".java").getText().
			contains("@Named(\"" + NEW_NAMED_PARAM + "\""));
		
		assertTrue(new TextEditor(INDEX_XHTML_2).getText().
				contains("#{" + NEW_NAMED_PARAM));
				
	}
	
	private void createXHTMLPage(String pageName) {
		NewXHTMLWizard xhtmlWizard = new NewXHTMLWizard();
		xhtmlWizard.open();
		NewXHTMLFileWizardPage page = new NewXHTMLFileWizardPage(xhtmlWizard);
		page.setDestination(getProjectName() + "/" 
				+ IDELabel.WebProjectsTree.WEB_CONTENT 
				+ "/pages");
		page.setName(pageName);
		xhtmlWizard.finish();
		editResourceUtil.replaceClassContentByResource(pageName,
				readFile("resources/jsf/"+pageName+".cdi"), false);
	}
	
	private List<String> changeNamedAnnotation(String className, String newNamed) {
		List<String> affectedFiles = new ArrayList<String>();
		String text = null;
		TextEditor activeEditor = new TextEditor(className+".java");
		for(int i=0;i<activeEditor.getNumberOfLines();i++){
			String line = activeEditor.getTextAtLine(i);
			if (line.contains("@Named") &&
					!line.contains("//") && !line.contains("*")) {
				text = line;
				break;
			}
		}
		if (text == null) {
			fail("There is no Named annotation in class:" + className);
		}
		String parsed = null;
		if (!text.contains("\"")) {
			parsed= className.substring(0,1).toLowerCase() + className.substring(1);
		} else {
			parsed= text.split("\"")[1];
		}
		
		String renameContextMenuText = "Rename '" + 
				parsed + 
				"' Named Bean ";
		//TODO
		new TextEditor(className + ".java").selectText(text);
		new WaitUntil(new ContextMenuIsEnabled(IDELabel.Menu.CDI_REFACTOR,renameContextMenuText));
		new ContextMenu().getItem(IDELabel.Menu.CDI_REFACTOR,renameContextMenuText).select();
		new DefaultShell("Refactoring");	
		
		CDIRefactorWizard cdiRefactorWizard = new CDIRefactorWizard();
		cdiRefactorWizard.setName(newNamed);
		cdiRefactorWizard.next();
		affectedFiles = cdiRefactorWizard.getAffectedFiles();
		cdiRefactorWizard.finish();
		return affectedFiles;
	}
}

