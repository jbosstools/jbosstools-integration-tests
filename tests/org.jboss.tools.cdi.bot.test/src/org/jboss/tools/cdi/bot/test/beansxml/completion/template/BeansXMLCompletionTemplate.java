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

package org.jboss.tools.cdi.bot.test.beansxml.completion.template;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.beansxml.template.BeansXMLValidationTemplate;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on code completion in beans.xml
 * 
 * @author Jaroslav Jankovic
 * 
 */
public abstract class BeansXMLCompletionTemplate extends CDITestBase {
	
	private static final List<String> INTERCEPTOR_NAMES = Arrays.asList(
			"I1", "I2", "I3");
	private static final List<String> DECORATORS_NAMES = Arrays.asList(
			"D1", "D2", "D3");
	private static final List<String> ALTERNATIVES_NAMES = Arrays.asList(
			"A1", "A2", "A3");
	private static final List<String> STEREOTYPES_NAMES = Arrays.asList(
			"S1", "S2", "S3");
	
	private List<String> beans_xml_tags;
	
	@After
	public void cleanup(){
		deleteAllProjects();
	}
	
	protected void setBeansXmlTags(List<String> tags){
		beans_xml_tags = tags;
	}

	@Test
	public void testPossibleCompletionInBeansXML() {
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.CLEAR_BEANS_XML_WITH_TAG));
		
		List<String> proposalList = editResourceUtil.getProposalList(
				IDELabel.WebProjectsTree.BEANS_XML, "<>",1);
		assertTrue(beans_xml_tags.containsAll(proposalList));
		assertTrue(proposalList.containsAll(beans_xml_tags));
	}
	
	@Test
	public void testInterceptorsCompletion() {
		for(String iName: INTERCEPTOR_NAMES){
			beansHelper.createInterceptor(iName, getPackageName(), null, false, false);
		}
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_INTERCEPTOR));
		editResourceUtil.replaceInEditor("Component", "");
				
		List<String> proposalList = editResourceUtil.getProposalList(
				IDELabel.WebProjectsTree.BEANS_XML, CDIConstants.CLASS_END_TAG);
		for (String interceptor : INTERCEPTOR_NAMES) {
			assertTrue("Proposal list contains "+proposalList,proposalList.contains(interceptor + " - " + getPackageName()));
		}
	}
	
	@Test
	public void testDecoratorsCompletion() {
		for(String dName: DECORATORS_NAMES){
			beansHelper.createDecorator(dName, getPackageName(), "java.util.set", null,
					true, false, false, false, false);
		}
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
				
		editResourceUtil.replaceInEditor("</beans>",readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_DECORATOR));
		editResourceUtil.replaceInEditor("Component", "");
			
		List<String> proposalList = editResourceUtil.getProposalList(IDELabel.WebProjectsTree.BEANS_XML, 
				CDIConstants.CLASS_END_TAG);
		for (String decorator : DECORATORS_NAMES) {
			assertTrue(proposalList.contains(decorator + " - " + getPackageName()));
		}

	}
	
	@Test
	public void testStereotypesCompletion() {
		for(String sName: STEREOTYPES_NAMES){
			beansHelper.createStereotype(sName, getPackageName(), false, false, true, 
					false, false);
		}
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_STEREOTYPE));
		editResourceUtil.replaceInEditor("Component", "");
			
		List<String> proposalList = editResourceUtil.getProposalList(IDELabel.WebProjectsTree.BEANS_XML, 
				CDIConstants.STEREOTYPE_END_TAG);
		for (String stereotype : STEREOTYPES_NAMES) {
			assertTrue("Proposal list contains "+proposalList,proposalList.contains(stereotype + " - " + getPackageName()));
		}

	}
	
	@Test
	public void testAlternativesCompletion() {
		for(String aName: ALTERNATIVES_NAMES){
			beansHelper.createBean(aName, getPackageName(), false, true, false,
					false, false, false,false,null,null);
		}
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
				
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_ALTERNATIVE));
		editResourceUtil.replaceInEditor("Component", "");
		
		List<String> proposalList = editResourceUtil.getProposalList(IDELabel.WebProjectsTree.BEANS_XML, 
				CDIConstants.CLASS_END_TAG);
		for (String alternative : ALTERNATIVES_NAMES) {
			assertTrue("Proposal list contains "+proposalList,proposalList.contains(alternative + " - " + getPackageName()));
		}

	}
	
	@Test
	public void testNonSupportedComponentCompletion() {
		
		String[] components = {"AL1", "Q1", "B1", "IB1", "Sc1"};
		
		beansHelper.createAnnotationLiteral(components[0], getPackageName(),
				false, false, false, null);
		beansHelper.createQualifier(components[1], getPackageName(), false,false);
		beansHelper.createBean(components[2], getPackageName(), false, false, false,
				false, false, false,false,null,null);
		beansHelper.createIBinding(components[3],getPackageName(), null, false,false);
		beansHelper.createScope(components[4], getPackageName(), false,
				false, false,false);
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.CLEAR_BEANS_XML_WITH_TAG));
		
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
	
}
