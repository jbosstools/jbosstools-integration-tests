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

package org.jboss.tools.cdi.bot.test.beans.qualifier.template;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewAnnotationWizardPage;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI Qualifier component
 * 
 * @author Jaroslav Jankovic
 */
public class QualifierValidationQuickFixTemplate extends CDITestBase {

	protected IValidationProvider validationProvider;

	// https://issues.jboss.org/browse/JBIDE-7630
	//depends on CDI Version
	@Test
	public void testTargetAnnotation() {
		
		String className = "Qualifier1";
		
		beansHelper.createQualifier(className, getPackageName(), false, false);
		
		editResourceUtil.replaceInEditor(className+".java","@Target({ TYPE, METHOD, PARAMETER, FIELD })", 
				"@Target({ TYPE, FIELD })");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.TARGET));
		
		//depends on CDI Version
		if(CDIVersion.equals("1.0")){
			assertEquals(1,problems.size());
			validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.TARGET),1);
			editResourceUtil.replaceInEditor(className+".java","@Target({TYPE, METHOD, FIELD, PARAMETER})", "");
			problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.TARGET));
			
			assertEquals(1,problems.size());
			
			validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.NO_TARGET));
		} else {
			assertEquals(0,problems.size());
			editResourceUtil.replaceInEditor(className+".java","@Target({ TYPE, FIELD })", "");
			assertEquals(0,problems.size());
		}
	}
	
	// https://issues.jboss.org/browse/JBIDE-7631
	@Test
	public void testRetentionAnnotation() {
		
		String className = "Qualifier2";

		beansHelper.createQualifier(className, getPackageName(), false, false);
				
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "@Retention(CLASS)");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.RETENTION));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.RETENTION));
		
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "");
		
		problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.NO_RETENTION));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.NO_RETENTION));
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7641
	@Test
	public void testNonbindingAnnotation() {
	
		String className = "Qualifier3";
		
		NewAnnotationCreationWizard aw = new NewAnnotationCreationWizard();
		aw.open();
		NewAnnotationWizardPage ap = new NewAnnotationWizardPage(aw);
		ap.setPackage(getPackageName());
		ap.setName("AAnnotation");
		aw.finish();
		
		beansHelper.createQualifier(className, getPackageName(), false, false);
		editResourceUtil.replaceClassContentByResource(className+".java",
				readFile("resources/quickfix/qualifier/QualifierWithAnnotation.java.cdi"),false);
	
		editResourceUtil.replaceInEditor(className+".java","QualifierComponent", className);
	
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.ANNOTATION_NONBINDING));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.ANNOTATION_NONBINDING));
		
		editResourceUtil.replaceClassContentByResource(className+".java",
				readFile("resources/quickfix/qualifier/QualifierWithStringArray.java.cdi"),false);
		editResourceUtil.replaceInEditor(className+".java","QualifierComponent", className);
		
		problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.ARRAY_NONBINDING));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.ARRAY_NONBINDING));
	}

}

