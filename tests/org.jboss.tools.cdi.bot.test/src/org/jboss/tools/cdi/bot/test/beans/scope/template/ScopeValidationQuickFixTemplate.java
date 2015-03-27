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

package org.jboss.tools.cdi.bot.test.beans.scope.template;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI Scope component
 * 
 * @author Jaroslav Jankovic
 */
public class ScopeValidationQuickFixTemplate extends CDITestBase {
	
	protected IValidationProvider validationProvider;
	protected String CDIVersion;

	public IValidationProvider validationProvider() {
		return validationProvider;
	}
	
	// https://issues.jboss.org/browse/JBIDE-7633
	//cdi1.0
	//cdi1.1+ should not fail
	@Test
	public void testTargetAnnotation() {
		
		String className = "Scope1";
		
		beansHelper.createScope(className, getPackageName(), false, false, false,false);
		
		editResourceUtil.replaceInEditor(className+".java","@Target({ TYPE, METHOD, FIELD })", 
				"@Target({ TYPE, FIELD })");
		
		ValidationProblem problem = validationProvider.getValidationProblem(ValidationType.TARGET);
		List<Problem> problems= validationHelper.findProblems(problem);
		if(CDIVersion.equals("1.0")){
			assertEquals(1,problems.size());
			validationHelper.openQuickfix(problem);
			editResourceUtil.replaceInEditor(className+".java","@Target({TYPE, METHOD, FIELD})", "");
			
			problem = validationProvider.getValidationProblem(ValidationType.NO_TARGET);
			problems= validationHelper.findProblems(problem);
			assertEquals(1,problems.size());
			validationHelper.openQuickfix(problem);
		} else {
			assertEquals(0,problems.size());
			editResourceUtil.replaceInEditor(className+".java","@Target({ TYPE, FIELD })", "");
			
			problem = validationProvider.getValidationProblem(ValidationType.NO_TARGET);
			problems= validationHelper.findProblems(problem);
			assertEquals(0,problems.size());
		}
	}
	
	// https://issues.jboss.org/browse/JBIDE-7631
	// https://issues.jboss.org/browse/JBIDE-19636
	@Test
	public void testRetentionAnnotation() {
		
		String className = "Scope2";

		beansHelper.createScope(className, getPackageName(), false, false, false,false);
				
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "@Retention(CLASS)");
		
		ValidationProblem problem = validationProvider.getValidationProblem(ValidationType.RETENTION);
		List<Problem> problems= validationHelper.findProblems(problem);
		assertEquals(1,problems.size());
		validationHelper.openQuickfix(problem);
		
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "");
		
		problem = validationProvider.getValidationProblem(ValidationType.NO_RETENTION);
		problems= validationHelper.findProblems(problem);
		assertEquals(1,problems.size());
		validationHelper.openQuickfix(problem);
		
	}
	
}
