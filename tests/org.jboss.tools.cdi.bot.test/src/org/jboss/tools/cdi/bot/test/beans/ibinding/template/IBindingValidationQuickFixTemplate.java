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

package org.jboss.tools.cdi.bot.test.beans.ibinding.template;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard;
import org.jboss.reddeer.eclipse.jdt.ui.wizards.NewAnnotationWizardPage;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI Interceptor Binding component
 * 
 * @author Jaroslav Jankovic
 */
public class IBindingValidationQuickFixTemplate extends CDITestBase {
	
	
	protected IValidationProvider validationProvider;
	
	// https://issues.jboss.org/browse/JBIDE-7641
	@Test
	public void testNonbindingAnnotation() {
		
		String className = "IBinding1";
			
		NewAnnotationCreationWizard aw = new NewAnnotationCreationWizard();
		aw.open();
		NewAnnotationWizardPage ap = new NewAnnotationWizardPage();
		ap.setPackage(getPackageName());
		ap.setName("AAnnotation");
		aw.finish();
		
		beansHelper.createIBinding(className,getPackageName(), null,false,false);
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/interceptorBinding/IBindingWithAnnotation.java.cdi"), false);

		editResourceUtil.replaceInEditor(className+".java","IBindingComponent", className);
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.ANNOTATION_NONBINDING));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.ANNOTATION_NONBINDING));
		
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/interceptorBinding/IBindingWithStringArray.java.cdi"), false);
				
		editResourceUtil.replaceInEditor(className+".java","IBindingComponent", className);
			
		problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.ARRAY_NONBINDING));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.ARRAY_NONBINDING));
	}
	
}
