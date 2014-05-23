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

package org.jboss.tools.cdi.bot.test.beansxml;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.annotations.CDIWizardType;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.Test;

/**
 * Test operates on code completion in beans.xml
 * 
 * @author Jaroslav Jankovic
 * 
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class BeansXMLCompletionTest extends CDITestBase {
	
	private static final List<String> INTERCEPTOR_NAMES = Arrays.asList(
			"I1", "I2", "I3");
	private static final List<String> DECORATORS_NAMES = Arrays.asList(
			"D1", "D2", "D3");
	private static final List<String> ALTERNATIVES_NAMES = Arrays.asList(
			"A1", "A2", "A3");
	private static final List<String> STEREOTYPES_NAMES = Arrays.asList(
			"S1", "S2", "S3");
	
	private static final List<String> BEANS_XML_TAGS = Arrays.asList(
			"alternatives", "decorators", "interceptors");

	@Test
	public void testPossibleCompletionInBeansXML() {
		
		beansHelper.createBeansXMLWithEmptyTag(getProjectName());
		LOGGER.info("Clear beans.xml was created");
				
		checkAutoCompletion(3, 0, "<>", IDELabel.WebProjectsTree.BEANS_XML, BEANS_XML_TAGS);				
	}
	
	@Test
	public void testInterceptorsCompletion() {

		wizard.createCDIComponents(CDIWizardType.INTERCEPTOR, getPackageName(), 
				INTERCEPTOR_NAMES, null, false);
				
		beansHelper.createBeansXMLWithInterceptor(getProjectName(), getPackageName(), null);
		LOGGER.info("Beans.xml with interceptors tag was created");
				
		List<String> proposalList = editResourceUtil.getProposalList(
				IDELabel.WebProjectsTree.BEANS_XML, CDIConstants.CLASS_END_TAG);
		for (String interceptor : INTERCEPTOR_NAMES) {
			assertTrue(proposalList.contains(interceptor + " - " + getPackageName()));
		}
	}
	
	@Test
	public void testDecoratorsCompletion() {
		
		wizard.createCDIComponents(CDIWizardType.DECORATOR, getPackageName(), 
				DECORATORS_NAMES, "java.util.Set", false);
				
		beansHelper.createBeansXMLWithDecorator(getProjectName(), getPackageName(), null);
		LOGGER.info("Beans.xml with decorators tag was created");
			
		List<String> proposalList = editResourceUtil.getProposalList(IDELabel.WebProjectsTree.BEANS_XML, 
				CDIConstants.CLASS_END_TAG);
		for (String decorator : DECORATORS_NAMES) {
			assertTrue(proposalList.contains(decorator + " - " + getPackageName()));
		}

	}
	
	@Test
	public void testStereotypesCompletion() {
		
		wizard.createCDIComponents(CDIWizardType.STEREOTYPE, getPackageName(), 
				STEREOTYPES_NAMES, "alternative", false);
				
		beansHelper.createBeansXMLWithStereotype(getProjectName(), getPackageName(), null);
		LOGGER.info("Beans.xml with stereotype tag was created");
			
		List<String> proposalList = editResourceUtil.getProposalList(IDELabel.WebProjectsTree.BEANS_XML, 
				CDIConstants.STEREOTYPE_END_TAG);
		for (String stereotype : STEREOTYPES_NAMES) {
			assertTrue(proposalList.contains(stereotype + " - " + getPackageName()));
		}

	}
	
	@Test
	public void testAlternativesCompletion() {
		
		wizard.createCDIComponents(CDIWizardType.BEAN, getPackageName(), 
				ALTERNATIVES_NAMES, "alternative", false);
				
		beansHelper.createBeansXMLWithAlternative(getProjectName(), getPackageName(), null);
		LOGGER.info("Beans.xml with alternative tag was created");
		
		List<String> proposalList = editResourceUtil.getProposalList(IDELabel.WebProjectsTree.BEANS_XML, 
				CDIConstants.CLASS_END_TAG);
		for (String alternative : ALTERNATIVES_NAMES) {
			assertTrue(proposalList.contains(alternative + " - " + getPackageName()));
		}

	}
	
	@Test
	public void testNonSupportedComponentCompletion() {
		
		String[] components = {"AL1", "Q1", "B1", "IB1", "Sc1"};
		
		wizard.createCDIComponent(CDIWizardType.ANNOTATION_LITERAL, components[0], 
				getPackageName(), null);
		wizard.createCDIComponent(CDIWizardType.QUALIFIER, components[1], 
				getPackageName(), null);
		wizard.createCDIComponent(CDIWizardType.BEAN, components[2], 
				getPackageName(), null);
		wizard.createCDIComponent(CDIWizardType.INTERCEPTOR_BINDING, components[3], 
				getPackageName(), null);
		wizard.createCDIComponent(CDIWizardType.SCOPE, components[4], 
				getPackageName(), null);
		
		beansHelper.createBeansXMLWithEmptyTag(getProjectName());
		LOGGER.info("Clear beans.xml with empty tag was created");
		
		List<String> proposalList = editResourceUtil.getProposalList(
				IDELabel.WebProjectsTree.BEANS_XML, "<>");
		List<String> nonSupportedComponents = Arrays.asList(components);
		
		for (String nonSupportedComponent : nonSupportedComponents) {
			for (String proposalOption : proposalList) {				
				assertFalse((nonSupportedComponent + " - " + getPackageName()).
						equals(proposalOption));
			}
		}
		
	}
	
	/**
	 * Method checks auto completion proposals. First, it 
	 * types provided text on location provided by parameters.
	 * Then checks all items in proposal list by 
	 * ContentAssistHelper.checkContentAssistContent() method.
	 * At the end, it removes inserted text(due to possible formating error) 
	 * @param row
	 * @param column
	 * @param text
	 * @param expectedProposalList
	 */
	private void checkAutoCompletion(int row, int column, String text, String editorTitle,
			List<String> expectedProposalList) {
		Editor te = new DefaultEditor(editorTitle);
		DefaultStyledText t = new DefaultStyledText();
		t.insertText(row, column, text);
		t.selectText(text);
		
		ContentAssistant cs = te.openContentAssistant();
		List<String> props = cs.getProposals();
		cs.close();
		assertTrue(props.containsAll(expectedProposalList));
		editResourceUtil.replaceInEditor(editorTitle, text, "");
	}
	
}
