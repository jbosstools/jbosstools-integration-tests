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

package org.jboss.tools.cdi.bot.test.beans.stereotype.template;


import static org.junit.Assert.assertEquals;

import java.util.List;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI Stereotype component
 * 
 * @author Jaroslav Jankovic
 */
public class StereotypeValidationQuickFixTemplate extends CDITestBase {
	
	public static IValidationProvider validationProvider;
	protected String CDIVersion;

	public IValidationProvider validationProvider() {
		return validationProvider;
	}
	
	@After
	public void deleteProjects(){
		deleteAllProjects();
	}
	
	// https://issues.jboss.org/browse/JBIDE-7630
	// cdi 1.0, not for 1.1+
	@Test
	public void testTargetAnnotation() {
		
		String className = "Stereotype1";
		
		beansHelper.createStereotype(className, getPackageName(), false, false, false, false, false);

		editResourceUtil.replaceInEditor(className+".java","@Target({ TYPE, METHOD, FIELD })", 
				"@Target({ TYPE, FIELD })");
		
		ValidationProblem problem = validationProvider.getValidationProblem(ValidationType.TARGET);
		List<Problem> problems= validationHelper.findProblems(problem);
		if(CDIVersion.equals("1.0")){
			assertEquals(1,problems.size());
			validationHelper.openQuickfix(problem,3);
			TextEditor te =new TextEditor();
			te.getText().contentEquals("@Target({TYPE, METHOD, FIELD})");
			editResourceUtil.replaceInEditor(className+".java","@Target({TYPE, METHOD, FIELD})", "");
			problems= validationHelper.findProblems(problem);
			assertEquals(1,problems.size());
		} else {
			assertEquals(0,problems.size());
			editResourceUtil.replaceInEditor(className+".java","@Target({ TYPE, FIELD })", "");
			problems= validationHelper.findProblems(problem);
			assertEquals(0,problems.size());
		}
	}
	
	
	// https://issues.jboss.org/browse/JBIDE-7631
	@Test
	public void testRetentionAnnotation() {
		
		String className = "Stereotype2";
		
		beansHelper.createStereotype(className, getPackageName(), false, false, false, false, false);
		
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "@Retention(CLASS)");
		
		ValidationProblem problem = validationProvider.getValidationProblem(ValidationType.RETENTION);
		List<Problem> problems= validationHelper.findProblems(problem);
		assertEquals(1,problems.size());
		validationHelper.openQuickfix(problem);
		//quickFixHelper.checkQuickFix(ValidationType.RETENTION, getProjectName(), validationProvider());
				
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "");
		
		problem = validationProvider.getValidationProblem(ValidationType.NO_RETENTION);
		problems= validationHelper.findProblems(problem);
		assertEquals(1,problems.size());
		validationHelper.openQuickfix(problem);
		new WaitWhile(new ProblemExists(ProblemType.ANY));
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7634
	@Test
	public void testNamedAnnotation() {
		
		String className = "Stereotype3";
		
		beansHelper.createStereotype(className, getPackageName(), false, false, false, false, false);
		
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/stereotype/StereotypeWithNamed.java.cdi"), false);
	
		editResourceUtil.replaceInEditor(className+".java","StereotypeComponent", className);
		
		ValidationProblem problem = validationProvider.getValidationProblem(ValidationType.NAMED);
		List<Problem> problems= validationHelper.findProblems(problem);
		assertEquals(1,problems.size());
		validationHelper.openQuickfix(problem);
		new WaitWhile(new ProblemExists(ProblemType.ANY));
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7640
	@Test	
	public void testTypedAnnotation() {
		
		String className = "Stereotype4";
		
		beansHelper.createStereotype(className, getPackageName(), false, false, false, false, false);
		
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/stereotype/StereotypeWithTyped.java.cdi"), false);
	
		editResourceUtil.replaceInEditor(className+".java","StereotypeComponent", className);
		
		ValidationProblem problem = validationProvider.getValidationProblem(ValidationType.TYPED);
		List<Problem> problems= validationHelper.findProblems(problem);
		assertEquals(1,problems.size());
		validationHelper.openQuickfix(problem);
		new WaitWhile(new ProblemExists(ProblemType.ANY));
		
	}	

}
