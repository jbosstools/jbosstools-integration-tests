/*******************************************************************************
 * Copyright (c) 2010-2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.beansxml.completion.template;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.condition.EditorIsDirty;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.core.exception.WorkbenchCoreLayerException;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
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
 * @author Zbynek Cervinka, zcervink@redhat.com
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
	private static final String TEST_CLASS_NAME = "Test";
	private static final String TEST_BEAN_CLASS_NAME = "TestBean";
	private static final String TEST_INTERCEPTOR_NAME = "TestInterceptor";
	private static final String TEST_PACKAGE_NAME = "package1";
	private static final String DEFAULT_TEST_FILE_PATH = "resources/trimTestsExampleFiles/DefaultTest";
	private static final String FILE_HAS_NOT_BEEN_OPENED_MSG = " file has not been opened.";
	
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
	
	@Test
	public void testTrimUiSupport() {
		addTheTrimTagToBeansXmlFile();

		ProblemsView pv = new ProblemsView();
		pv.open();
		List<Problem> errors = pv.getProblems(ProblemType.ERROR);
		assertTrue("The Problems view contains errors.", errors.isEmpty());
		
		EditorPartWrapper beansEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beansEditor.activate();
		beansEditor.activateTreePage();
		assertTrue("Trim item is not available in the beans.xml UI tree editor.", beansEditor.isTrimItemAvailable());
		
		beansEditor.close();
	}
	
	@Test
	public void testTrimDiscoverAllScopeAnnotations() {
		addTheTrimTagToBeansXmlFile();

		String[] annotationsThatShouldBeDiscovered = { "ApplicationScoped", "SessionScoped", "ConversationScoped",
				"RequestScoped" };

		createNewClass(PROJECT_NAME, TEST_PACKAGE_NAME, "Test");
		createNewClass(PROJECT_NAME, TEST_PACKAGE_NAME, TEST_BEAN_CLASS_NAME);
		editResourceUtil.replaceClassContentByResource(TEST_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile(DEFAULT_TEST_FILE_PATH + JAVA_FILE_EXTENSION), true, false);

		// iterate over and test all the scope annotations that should be in set of discovered types
		for (String annotation : annotationsThatShouldBeDiscovered) {
			editResourceUtil.replaceClassContentByResource(TEST_BEAN_CLASS_NAME + JAVA_FILE_EXTENSION,
					readFile("resources/trimTestsExampleFiles/" + annotation + JAVA_FILE_EXTENSION),
					true, false);
			openOn("bean", TEST_BEAN_CLASS_NAME);
		}
	}
	
	@Test
	public void testTrimDiscoverAllBeanDefinitionAnnotations() {
		addTheTrimTagToBeansXmlFile();

		createNewClass(PROJECT_NAME, TEST_PACKAGE_NAME, "Test");
		editResourceUtil.replaceClassContentByResource(TEST_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile(DEFAULT_TEST_FILE_PATH + JAVA_FILE_EXTENSION), true, false);
		createNewClass(PROJECT_NAME, TEST_PACKAGE_NAME, TEST_BEAN_CLASS_NAME);

		// test the "@Dependent" and "@Singleton" annotations
		for (String annotation : new String[] { "Dependent", "Singleton" }) {
			editResourceUtil.replaceClassContentByResource(TEST_BEAN_CLASS_NAME + JAVA_FILE_EXTENSION,
					readFile("resources/trimTestsExampleFiles/" + annotation + JAVA_FILE_EXTENSION), true, false);
			openOn("bean", TEST_BEAN_CLASS_NAME);
		}
		editResourceUtil.replaceClassContentByResource(TEST_BEAN_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile("resources/trimTestsExampleFiles/DefaultTestBean" + JAVA_FILE_EXTENSION), true, false);

		// test the "@Interceptor" annotation
		createNewInterceptor(PROJECT_NAME, TEST_PACKAGE_NAME, TEST_INTERCEPTOR_NAME);
		editResourceUtil.replaceClassContentByResource(TEST_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile("resources/trimTestsExampleFiles/TestInterceptor" + JAVA_FILE_EXTENSION), true, false);
		openDeclarationOn(TEST_INTERCEPTOR_NAME, TEST_INTERCEPTOR_NAME);
		editResourceUtil.replaceClassContentByResource(TEST_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile(DEFAULT_TEST_FILE_PATH + JAVA_FILE_EXTENSION), true, false);

		// test the "@Stereotype" annotation
		createNewStereotype(PROJECT_NAME, TEST_PACKAGE_NAME, "TestStereotype");
		editResourceUtil.replaceClassContentByResource(TEST_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile("resources/trimTestsExampleFiles/TestStereotype" + JAVA_FILE_EXTENSION), true, false);
		openOnDeclarationWithoutMenu("@TestStereotype", "TestStereotype");
		editResourceUtil.replaceClassContentByResource(TEST_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile(DEFAULT_TEST_FILE_PATH + JAVA_FILE_EXTENSION), true, false);

		// test the "@Decorator" annotation
		createNewInterface(PROJECT_NAME, TEST_PACKAGE_NAME, "TestInterface");
		editResourceUtil.replaceClassContentByResource(TEST_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile("resources/trimTestsExampleFiles/TestDecorator" + JAVA_FILE_EXTENSION), true, false);
		editResourceUtil.replaceClassContentByResource(TEST_BEAN_CLASS_NAME + JAVA_FILE_EXTENSION,
				readFile("resources/trimTestsExampleFiles/Decorator" + JAVA_FILE_EXTENSION), true, false);
		openOn("bean", TEST_BEAN_CLASS_NAME);
	}

	private void addTheTrimTagToBeansXmlFile() {
		EditorPartWrapper beansEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beansEditor.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", "<trim/></beans>", false);
		new WaitUntil(new EditorIsDirty(beansEditor), false);
		beansEditor.save();
		new WaitUntil(new JobIsRunning(), TimePeriod.SHORT, false);
		new WaitWhile(new JobIsRunning(), false);
	}
	
	private void openOn(String text, String proposal) {
		TextEditor te = new TextEditor(TEST_CLASS_NAME + JAVA_FILE_EXTENSION);
		te.selectText(text);
		try {
			te.openOpenOnAssistant().chooseProposal("Open @Inject Bean " + proposal);
			new DefaultEditor(proposal + JAVA_FILE_EXTENSION);
		} catch (CoreLayerException | WorkbenchCoreLayerException e) {
			fail(proposal + JAVA_FILE_EXTENSION + FILE_HAS_NOT_BEEN_OPENED_MSG);
		}
	}
	
	private void openDeclarationOn(String text, String proposal) {
		TextEditor te = new TextEditor(TEST_CLASS_NAME + JAVA_FILE_EXTENSION);
		te.selectText(text);
		try {
			te.openOpenOnAssistant().chooseProposal("Open Declaration");
			new DefaultEditor(proposal + JAVA_FILE_EXTENSION);
		} catch (CoreLayerException | WorkbenchCoreLayerException e) {
			fail(proposal + JAVA_FILE_EXTENSION + FILE_HAS_NOT_BEEN_OPENED_MSG);
		}
	}
	
	private void openOnDeclarationWithoutMenu(String text, String proposal) {
		TextEditor te = new TextEditor(TEST_CLASS_NAME + JAVA_FILE_EXTENSION);
		te.selectText(text);
		te.activate();		
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new ShellMenuItem("Navigate", "Open Declaration").select();
			new DefaultEditor(proposal + JAVA_FILE_EXTENSION);
		} catch (CoreLayerException | WorkbenchCoreLayerException e) {
			fail(proposal + JAVA_FILE_EXTENSION + FILE_HAS_NOT_BEEN_OPENED_MSG);
		}
	}
}
