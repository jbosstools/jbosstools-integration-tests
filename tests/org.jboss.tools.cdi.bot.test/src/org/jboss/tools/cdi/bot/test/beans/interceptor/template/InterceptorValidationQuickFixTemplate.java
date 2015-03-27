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

package org.jboss.tools.cdi.bot.test.beans.interceptor.template;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI Interceptor component
 * 
 * @author Jaroslav Jankovic
 */
public class InterceptorValidationQuickFixTemplate extends CDITestBase {
	
	protected static IValidationProvider validationProvider;
	
	// https://issues.jboss.org/browse/JBIDE-7680
	@Test
	public void testSessionAnnotation() {
			
		String className = "Interceptor1";
		
		beansHelper.createInterceptor(className, getPackageName(), null, false, false);
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/interceptor/InterceptorWithStateless.java.cdi"),false);

		editResourceUtil.replaceInEditor(className+".java","InterceptorComponent", className);
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.STATELESS));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.STATELESS));
			
	}
	
	// https://issues.jboss.org/browse/JBIDE-7636
	@Test
	public void testNamedAnnotation() {
		
		String className = "Interceptor2";
		
		beansHelper.createInterceptor(className, getPackageName(), null, false, false);
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/interceptor/InterceptorWithNamed.java.cdi"),false);

		editResourceUtil.replaceInEditor(className+".java","InterceptorComponent", className);
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.NAMED));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.NAMED));
		
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7683
	@Test
	public void testProducer() {
		
		String className = "Interceptor3";
		
		beansHelper.createInterceptor(className, getPackageName(), null, false, false);
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/interceptor/InterceptorWithProducer.java.cdi"),false);

		editResourceUtil.replaceInEditor(className+".java","InterceptorComponent", className);
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.PRODUCES));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.PRODUCES));
		
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7684
	@Test
	public void testDisposesAnnotation() {
		
		String className = "Interceptor4";
		
		beansHelper.createInterceptor(className, getPackageName(), null, false, false);
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/interceptor/InterceptorWithDisposes.java.cdi"),false);

		editResourceUtil.replaceInEditor(className+".java","InterceptorComponent", className);
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.DISPOSES));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.DISPOSES));
		
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7685
	@Test
	public void testObservesAnnotation() {
		
		String className = "Interceptor5";
		
		beansHelper.createInterceptor(className, getPackageName(), null, false, false);
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/interceptor/InterceptorWithDisposes.java.cdi"),false);
		
		editResourceUtil.replaceInEditor(className+".java","import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		editResourceUtil.replaceInEditor(className+".java","@Disposes", "@Observes");
		editResourceUtil.replaceInEditor(className+".java","InterceptorComponent", className);
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.OBSERVES));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.OBSERVES));
		
			
	}
	
	// https://issues.jboss.org/browse/JBIDE-7686
	@Test
	public void testSpecializesAnnotation() {
		
		String className = "Interceptor6";
		
		beansHelper.createInterceptor(className, getPackageName(), null, false, false);
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/interceptor/InterceptorWithSpecializes.java.cdi"),false);

		editResourceUtil.replaceInEditor(className+".java","InterceptorComponent", className);
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.SPECIALIZES));
		
		assertEquals(1,problems.size());		
			
	}
	
}
