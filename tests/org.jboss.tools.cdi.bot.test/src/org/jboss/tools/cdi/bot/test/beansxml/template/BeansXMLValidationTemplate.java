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

package org.jboss.tools.cdi.bot.test.beansxml.template;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.condition.BeanXMLValidationProblemIsEmpty;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on beans validation in beans.xml 
 * 
 * @author Jaroslav Jankovic
 * 
 */
public abstract class BeansXMLValidationTemplate extends CDITestBase {
	
	public static final String CLEAR_BEANS_XML = "resources/beansXML/"
			+ "beans.xml.cdi";
	public static final String CLEAR_BEANS_XML_WITH_TAG = "resources/beansXML/"
			+ "beansXmlWithEmptyTag.xml.cdi";
	public static final String BEANS_XML_WITH_INTERCEPTOR = "resources/beansXML/"
			+ "beansXmlWithInterceptor.xml.cdi";
	public static final String BEANS_XML_WITH_DECORATOR = "resources/beansXML/"
			+ "beansXmlWithDecorator.xml.cdi";
	public static final String BEANS_XML_WITH_STEREOTYPE = "resources/beansXML/"
			+ "beansXmlWithStereotype.xml.cdi";
	public static final String BEANS_XML_WITH_ALTERNATIVE = "resources/beansXML/"
			+ "beansXmlWithAlternative.xml.cdi";
	
	protected IValidationProvider validationProvider = null;
	

	private static final String someBean = "Bean1";
	private static final String nonExistingPackage = "somePackage";
	
	@After
	public void cleanup(){
		deleteAllProjects();
	}
	
	@Test
	public void testEmptyBeansXMLValidation() {
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		new WaitUntil(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		
	}
	
	@Test
	public void testInterceptorsValidation() {
		
		String className = "I1";
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		Project p = pe.getProject(getProjectName());
		p.refresh();
		if (!p.containsItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC, getPackageName(),
				someBean + ".java")) {
			beansHelper.createClass(someBean, getPackageName());
		}
		beansHelper.createInterceptor(className, getPackageName(), null, false,true);
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		String beansText = new DefaultStyledText().getText();
		assertTrue(beansText.contains("<interceptors>"));
		assertTrue(beansText.contains("<class>"+getPackageName()+"."+className+"</class>"));
		
		new WaitUntil(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		
		bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor(getPackageName() + "."+ className, 
				nonExistingPackage + "."+ className);
		
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		
		ValidationProblem vProblem = validationProvider.getValidationProblem(ValidationType.NO_CLASS);
		if(vProblem == null){
			fail("unable to find any validation problem of type"+vProblem);
		}
		assertTrue(validationHelper.findProblems(vProblem).size() > 0);
		
		bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor(nonExistingPackage + "."+ className, 
				getPackageName() + "." + someBean);
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		vProblem = validationProvider.getValidationProblem(ValidationType.ISNT_INTERCEPTOR);
		if(vProblem == null){
			fail("unable to find any validation problem of type"+vProblem);
		}
		assertTrue(validationHelper.findProblems(vProblem).size() > 0);
		
	}
	
	@Test
	public void testDecoratorsValidation() {
		
		String className = "D1";
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		Project p = pe.getProject(getProjectName());
		p.refresh();
		if (!p.containsItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC, getPackageName(),
				someBean + ".java")) {
			beansHelper.createClass(someBean, getPackageName());
		}
		
		beansHelper.createDecorator(className, getPackageName(), "java.util.set", null, false,false,false,false,true);

		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
				
		String beansText = new DefaultStyledText().getText();
		assertTrue(beansText.contains("<decorators>"));
		assertTrue(beansText.contains("<class>"+getPackageName()+"."+className+"</class>"));
		new WaitUntil(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		
		
		bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
				
		editResourceUtil.replaceInEditor(getPackageName() + "."+ className, 
				nonExistingPackage + "."+ className);
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		ValidationProblem vProblem = validationProvider.getValidationProblem(ValidationType.NO_CLASS);
		if(vProblem == null){
			fail("unable to find any validation problem of type"+vProblem);
		}
		assertTrue(validationHelper.findProblems(vProblem).size() > 0);
		
		bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor(nonExistingPackage + "."+ className, 
				getPackageName() + "." + someBean);
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		vProblem = validationProvider.getValidationProblem(ValidationType.ISNT_DECORATOR);
		if(vProblem == null){
			fail("unable to find any validation problem of type"+vProblem);
		}
		assertTrue(validationHelper.findProblems(vProblem).size() > 0);
	}
	
	@Test
	public void testAlternativesValidation() {
		
		String className = "A1";
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		Project p =pe.getProject(getProjectName());
		p.refresh();
		if (!p.containsItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC, getPackageName(),
				someBean + ".java")) {
			beansHelper.createClass(someBean, getPackageName());
		}
		beansHelper.createBean(className, getPackageName(), false, 
				true, false, false, false, false, false, null, null);

		
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
				
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_ALTERNATIVE));
		editResourceUtil.replaceInEditor("Component", getPackageName()+"."+className);
		new WaitUntil(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		
		
		bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor(getPackageName() + "." + className, 
				nonExistingPackage + "."	+ className);
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		ValidationProblem vProblem = validationProvider.getValidationProblem(ValidationType.NO_CLASS);
		if(vProblem == null){
			fail("unable to find any validation problem of type"+vProblem);
		}
		assertTrue(validationHelper.findProblems(vProblem).size() > 0);
		
		bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		
		editResourceUtil.replaceInEditor(nonExistingPackage + "." + className, 
				getPackageName() + "." + someBean);
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()),TimePeriod.LONG);
		vProblem = validationProvider.getValidationProblem(ValidationType.ISNT_ALTERNATIVE);
		if(vProblem == null){
			fail("unable to find any validation problem of type"+vProblem);
		}
		assertTrue(validationHelper.findProblems(vProblem).size() > 0);
		
	}
	
}