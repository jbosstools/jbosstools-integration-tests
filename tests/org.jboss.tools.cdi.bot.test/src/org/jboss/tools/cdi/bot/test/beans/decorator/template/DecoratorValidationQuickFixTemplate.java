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

package org.jboss.tools.cdi.bot.test.beans.decorator.template;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI Decorator component
 * 
 * @author Jaroslav Jankovic
 */
public class DecoratorValidationQuickFixTemplate extends CDITestBase {
	
	protected IValidationProvider validationProvider;

	public IValidationProvider validationProvider() {
		return validationProvider;
		
	}
	
	@After
	public void deleteProjects() {
		deleteAllProjects();
	}
	
	@Before
	public void addProducerClass(){
		beansHelper.createClass("Bean1", getPackageName());
		editResourceUtil.replaceClassContentByResource("Bean1.java", 
				readFile("resources/quickfix/decorator/Bean1.java.cdi"), false);
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7680
	@Test
	public void testSessionAnnotation() {
			
		String className = "Decorator1";
		
		createBean(className, "resources/quickfix/decorator/DecoratorWithStateless.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.STATELESS));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.STATELESS));
		TextEditor te = new TextEditor(className+".java");
		assertFalse(te.getText().contains("@Decorator"));
		assertFalse(te.getText().contains("javax.decorator.Decorator"));
		assertEquals(1,validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.DELEGATE)).size());
	}
	
	// https://issues.jboss.org/browse/JBIDE-7636
	@Test
	public void testNamedAnnotation() {
		
		String className = "Decorator2";
		
		createBean(className, "resources/quickfix/decorator/DecoratorWithNamed.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.NAMED));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.NAMED));
		new WaitWhile(new ProblemExists(ProblemType.ALL));
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7683
	@Test
	public void testProducer() {
		
		String className = "Decorator3";
		
		createBean(className, "resources/quickfix/decorator/DecoratorWithProducer.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.PRODUCES));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.PRODUCES));	
		new WaitWhile(new ProblemExists(ProblemType.ALL));
	}
	
	// https://issues.jboss.org/browse/JBIDE-7684
	@Test
	public void testDisposesAnnotation() {
		
		String className = "Decorator4";
		
		createBean(className, "resources/quickfix/decorator/DecoratorWithDisposes.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.DISPOSES));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.DISPOSES));
		new WaitWhile(new ProblemExists(ProblemType.ALL));
	}
	
	// https://issues.jboss.org/browse/JBIDE-7685
	@Test
	public void testObservesAnnotation() {
		
		String className = "Decorator5";
		
		createBean(className, "resources/quickfix/decorator/DecoratorWithDisposes.java.cdi");
		
		editResourceUtil.replaceInEditor(className+".java","import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		editResourceUtil.replaceInEditor(className+".java","@Disposes", "@Observes");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.OBSERVES));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.OBSERVES));
		new WaitWhile(new ProblemExists(ProblemType.ALL));
	}
	
	// https://issues.jboss.org/browse/JBIDE-7686
	@Test
	public void testSpecializesAnnotation() {
		
		String className = "Decorator6";
		
		createBean(className, "resources/quickfix/decorator/DecoratorWithSpecializes.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.SPECIALIZES));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.SPECIALIZES));
		new WaitWhile(new ProblemExists(ProblemType.ALL));
	}
	
	private void createBean(String className, String resource){
		beansHelper.createClass(className, getPackageName());
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile(resource), false);
		editResourceUtil.replaceInEditor(className+".java","DecoratorComponent", className);
	}
	
}