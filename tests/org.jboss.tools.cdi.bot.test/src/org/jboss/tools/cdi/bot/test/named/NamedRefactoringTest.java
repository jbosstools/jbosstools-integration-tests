/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.named;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.jsf.JSFTestBase;
import org.jboss.tools.cdi.bot.test.uiutils.CDIWizardHelper;
import org.jboss.tools.cdi.bot.test.uiutils.CollectionsUtil;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on @Named refactoring  
 * 
 * @author Jaroslav Jankovic
 * 
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class NamedRefactoringTest extends JSFTestBase {

	private static final String MANAGED_BEAN_1 = "ManagedBean1";
	private static final String MANAGED_BEAN_2 = "ManagedBean2";
	private static final String INDEX_XHTML_1= "index1.xhtml";
	private static final String INDEX_XHTML_2= "index2.xhtml";
	private static final String INDEX_XHTML_3= "index3.xhtml";
	private static final String NEW_NAMED_PARAM = "bean2";	
	private CDIWizardHelper wizard = new CDIWizardHelper();

	@After
	public void waitForJobs() {
		editResourceUtil.deletePackage(getProjectName(), getPackageName());
		editResourceUtil.deleteWebFolder(getProjectName());
	}
	
	@Test
	public void testNamedAnnotationWithParamRefactor() {
		wizard.createCDIBeanComponentWithContent(MANAGED_BEAN_1, 
				getPackageName(), null, "/resources/jsf/ManagedBeanParamNamed.java.cdi");		
		
		createXHTMLPageWithContent(INDEX_XHTML_1, "/resources/jsf/index1.xhtml.cdi");
		createXHTMLPageWithContent(INDEX_XHTML_3, "/resources/jsf/index3.xhtml.cdi");
	
		Collection<String> affectedFiles = changeNamedAnnotation(MANAGED_BEAN_1, 
				NEW_NAMED_PARAM);
		Collection<String> expectedAffectedFiles = Arrays.asList(
				MANAGED_BEAN_1 + ".java", INDEX_XHTML_1, INDEX_XHTML_3);
	
		for (String affectedFile : affectedFiles) {
			new DefaultEditor(affectedFile).save();
		}
	
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
		
		wizard.createCDIBeanComponentWithContent(MANAGED_BEAN_2, 
				getPackageName(), null, "/resources/jsf/ManagedBeanNoParamNamed.java.cdi");	
		
		createXHTMLPageWithContent(INDEX_XHTML_2, "/resources/jsf/index2.xhtml.cdi");
		createXHTMLPageWithContent(INDEX_XHTML_3, "/resources/jsf/index3.xhtml.cdi");
		
		Collection<String> affectedFiles = changeNamedAnnotation(MANAGED_BEAN_2, NEW_NAMED_PARAM);
		Collection<String> expectedAffectedFiles = Arrays.asList(
				MANAGED_BEAN_2 + ".java", INDEX_XHTML_2, INDEX_XHTML_3);
	
		for (String affectedFile : affectedFiles) {
			new DefaultEditor(affectedFile).save();
		}
	
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
		
		wizard.createCDIBeanComponentWithContent(MANAGED_BEAN_2, 
				getPackageName(), null, "/resources/jsf/ManagedBeanNoParamNamed.java.cdi");	
		
		createXHTMLPageWithContent(INDEX_XHTML_2, "/resources/jsf/index1.xhtml.cdi");		
			
		Collection<String> affectedFiles = changeNamedAnnotation(MANAGED_BEAN_2, NEW_NAMED_PARAM);
		Collection<String> expectedAffectedFiles = Arrays.asList(MANAGED_BEAN_2 + ".java");
	
		for (String affectedFile : affectedFiles) {
			new DefaultEditor(affectedFile).save();
		}
	
		assertEquals(expectedAffectedFiles.size(), affectedFiles.size());
		assertTrue(CollectionsUtil.compareTwoCollectionsEquality(
				expectedAffectedFiles, affectedFiles));
		
		assertTrue(new TextEditor(MANAGED_BEAN_2 + ".java").getText().
			contains("@Named(\"" + NEW_NAMED_PARAM + "\""));
		
		assertTrue(!new TextEditor(INDEX_XHTML_2).getText().
				contains("#{" + NEW_NAMED_PARAM));
				
	}

	

}
