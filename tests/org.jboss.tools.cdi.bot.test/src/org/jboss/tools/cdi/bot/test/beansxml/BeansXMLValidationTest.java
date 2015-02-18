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

import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.bot.test.condition.BeanValidationErrorIsEmpty;
import org.jboss.tools.cdi.bot.test.quickfix.base.BeansXMLQuickFixTestBase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test operates on beans validation in beans.xml 
 * 
 * @author Jaroslav Jankovic
 * 
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class BeansXMLValidationTest extends BeansXMLQuickFixTestBase {
	
	public static final String CLEAR_BEANS_XML = "/resources/beansXML/"
			+ "beans.xml.cdi";
	public static final String CLEAR_BEANS_XML_WITH_TAG = "/resources/beansXML/"
			+ "beansXmlWithEmptyTag.xml.cdi";
	public static final String BEANS_XML_WITH_INTERCEPTOR = "/resources/beansXML/"
			+ "beansXmlWithInterceptor.xml.cdi";
	public static final String BEANS_XML_WITH_DECORATOR = "/resources/beansXML/"
			+ "beansXmlWithDecorator.xml.cdi";
	public static final String BEANS_XML_WITH_STEREOTYPE = "/resources/beansXML/"
			+ "beansXmlWithStereotype.xml.cdi";
	public static final String BEANS_XML_WITH_ALTERNATIVE = "/resources/beansXML/"
			+ "beansXmlWithAlternative.xml.cdi";
	

	private static final String someBean = "Bean1";
	private static final String nonExistingPackage = "somePackage";
	
	@BeforeClass
	public static void setup() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
	}
	
	@Test
	public void testEmptyBeansXMLValidation() {
		
		beansHelper.createBeansXMLWithContent(getProjectName(), 
				this.getClass().getResourceAsStream(CLEAR_BEANS_XML));
		
		new WaitUntil(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		
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
			wizard.createCDIComponent(CDIWizardType.BEAN, someBean, getPackageName(), null);
		}
		
		wizard.createCDIComponent(CDIWizardType.INTERCEPTOR, className, getPackageName(), null);

		beansHelper.createBeansXMLWithInterceptor(getProjectName(), getPackageName(), className,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_INTERCEPTOR));
		
		new WaitUntil(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		
		beansHelper.createBeansXMLWithInterceptor(getProjectName(), nonExistingPackage, className,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_INTERCEPTOR));
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_CLASS, 
				getProjectName(), getValidationProvider()));
		
		beansHelper.createBeansXMLWithInterceptor(getProjectName(), getPackageName(), someBean,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_INTERCEPTOR));
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_INTERCEPTOR, 
				getProjectName(), getValidationProvider()));
		
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
			wizard.createCDIComponent(CDIWizardType.BEAN, someBean, getPackageName(), null);
		}
		
		wizard.createCDIComponent(CDIWizardType.DECORATOR, className, getPackageName(), "java.util.Set");

		beansHelper.createBeansXMLWithDecorator(getProjectName(), getPackageName(), className,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_DECORATOR));
		new WaitUntil(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		
		
		beansHelper.createBeansXMLWithDecorator(getProjectName(), nonExistingPackage, className,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_DECORATOR));
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_CLASS, 
				getProjectName(), getValidationProvider()));
		
		beansHelper.createBeansXMLWithDecorator(getProjectName(), getPackageName(), someBean,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_DECORATOR));
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_DECORATOR, 
				getProjectName(), getValidationProvider()));
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
			wizard.createCDIComponent(CDIWizardType.BEAN, someBean, getPackageName(), null);
		}
		
		wizard.createCDIComponent(CDIWizardType.BEAN, className, getPackageName(), "alternative");

		
		beansHelper.createBeansXMLWithAlternative(getProjectName(), getPackageName(), className,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_ALTERNATIVE));
		new WaitUntil(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		
		
		beansHelper.createBeansXMLWithAlternative(getProjectName(), nonExistingPackage, className,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_ALTERNATIVE));
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_CLASS, 
				getProjectName(), getValidationProvider()));
		
		beansHelper.createBeansXMLWithAlternative(getProjectName(), getPackageName(), someBean,
				this.getClass().getResourceAsStream(BEANS_XML_WITH_ALTERNATIVE));
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_ALTERNATIVE, 
				getProjectName(), getValidationProvider()));
		
	}
	
}