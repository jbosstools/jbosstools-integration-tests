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
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.annotations.CDIWizardType;
import org.jboss.tools.cdi.bot.test.annotations.ValidationType;
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

	private static final String someBean = "Bean1";
	private static final String nonExistingPackage = "somePackage";
	
	@BeforeClass
	public static void setup() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
	}
	
	@Test
	public void testEmptyBeansXMLValidation() {
		
		beansHelper.createEmptyBeansXML(getProjectName());	
		
		new WaitUntil(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		
	}
	
	@Test
	public void testInterceptorsValidation() {
		
		String className = "I1";
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		Project p = pe.getProject(getProjectName());
		if (!p.containsItem((CDIConstants.SRC +"/"+ getPackageName() + 
				"/" + someBean + ".java").split("/"))) {
			wizard.createCDIComponent(CDIWizardType.BEAN, someBean, getPackageName(), null);
		}
		
		wizard.createCDIComponent(CDIWizardType.INTERCEPTOR, className, getPackageName(), null);

		beansHelper.createBeansXMLWithInterceptor(getProjectName(), getPackageName(), className);
		new WaitUntil(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		
		beansHelper.createBeansXMLWithInterceptor(getProjectName(), nonExistingPackage, className);
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_CLASS, 
				getProjectName(), getValidationProvider()));
		
		beansHelper.createBeansXMLWithInterceptor(getProjectName(), getPackageName(), someBean);
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_INTERCEPTOR, 
				getProjectName(), getValidationProvider()));
		
	}
	
	@Test
	public void testDecoratorsValidation() {
		
		String className = "D1";
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		Project p = pe.getProject(getProjectName());
		if (!p.containsItem((CDIConstants.SRC +"/"+ getPackageName() + 
				"/" + someBean + ".java").split("/"))) {
			wizard.createCDIComponent(CDIWizardType.BEAN, someBean, getPackageName(), null);
		}
		
		wizard.createCDIComponent(CDIWizardType.DECORATOR, className, getPackageName(), "java.util.Set");

		beansHelper.createBeansXMLWithDecorator(getProjectName(), getPackageName(), className);
		new WaitUntil(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		
		beansHelper.createBeansXMLWithDecorator(getProjectName(), nonExistingPackage, className);
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_CLASS, 
				getProjectName(), getValidationProvider()));
		
		beansHelper.createBeansXMLWithDecorator(getProjectName(), getPackageName(), someBean);
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_DECORATOR, 
				getProjectName(), getValidationProvider()));
	}
	
	@Test
	public void testAlternativesValidation() {
		
		String className = "A1";
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		Project p =pe.getProject(getProjectName());
		if (!p.containsItem((CDIConstants.SRC +"/"+ getPackageName() + 
				"/" + someBean + ".java").split("/"))) {
			wizard.createCDIComponent(CDIWizardType.BEAN, someBean, getPackageName(), null);
		}
		
		wizard.createCDIComponent(CDIWizardType.BEAN, className, getPackageName(), "alternative");

		beansHelper.createBeansXMLWithAlternative(getProjectName(), getPackageName(), className);
		new WaitUntil(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		
		beansHelper.createBeansXMLWithAlternative(getProjectName(), nonExistingPackage, className);
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_CLASS, 
				getProjectName(), getValidationProvider()));
		
		beansHelper.createBeansXMLWithAlternative(getProjectName(), getPackageName(), someBean);
		new WaitWhile(new BeanValidationErrorIsEmpty(getProjectName()),TimePeriod.LONG);
		assertNotNull(quickFixHelper.getProblem(ValidationType.NO_ALTERNATIVE, 
				getProjectName(), getValidationProvider()));
		
	}
	
}