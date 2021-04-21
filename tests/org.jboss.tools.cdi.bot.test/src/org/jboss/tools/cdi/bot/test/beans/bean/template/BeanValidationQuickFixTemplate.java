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

package org.jboss.tools.cdi.bot.test.beans.bean.template;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI bean component
 * 
 * @author Jaroslav Jankovic
 */
public class BeanValidationQuickFixTemplate extends CDITestBase {
	
	
	protected IValidationProvider validationProvider;
	
	
	// https://issues.jboss.org/browse/JBIDE-8550
	@Test
	public void testSerializableManagedBean() {
		
		String className = "ManagedBean";
		
		createBean(className,"resources/quickfix/bean/SerializableBean.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.SERIALIZABLE));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.SERIALIZABLE));
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7664
	@Test
	public void testConstructor() {
		
		String className = "Bean1";
		
		createBean(className,"resources/quickfix/bean/ConstructorWithParam.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.CONSTRUCTOR_DISPOSES));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.CONSTRUCTOR_DISPOSES));
		
		editResourceUtil.replaceClassContentByResource(className+".java", 
				readFile("resources/quickfix/bean/ConstructorWithParam.java.cdi"), false);
		
		editResourceUtil.replaceInEditor(className+".java", "@Disposes", "@Observes");
		editResourceUtil.replaceInEditor(className+".java", "import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		editResourceUtil.replaceInEditor(className+".java", "BeanComponent", className);		
		
		problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.CONSTRUCTOR_OBSERVES));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.CONSTRUCTOR_OBSERVES));
	}
	
	// https://issues.jboss.org/browse/JBIDE-7665
	@Test
	public void testProducer() {
		
		String className = "Bean2";
		
		createBean(className,"resources/quickfix/bean/ProducerWithParam.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.PRODUCER_DISPOSES));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.PRODUCER_DISPOSES),1);
		
		editResourceUtil.replaceClassContentByResource(className+".java",
				readFile("resources/quickfix/bean/ProducerWithParam.java.cdi"), false);
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);
	
		editResourceUtil.replaceInEditor(className+".java","@Disposes", "@Observes");
		editResourceUtil.replaceInEditor(className+".java","import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		
		problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.PRODUCER_OBSERVES));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.PRODUCER_OBSERVES),1);
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7667
	@Test
	public void testInjectDisposer() {
			
		String className = "Bean3";
		
		createBean(className,"resources/quickfix/bean/BeanInjectDisposes.java.cdi");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.DISPOSER_INJECT));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.DISPOSER_INJECT));
				
	}
	
	// https://issues.jboss.org/browse/JBIDE-7667
	@Test
	public void testInjectObserver() {
		
		String className = "Bean4";
		
		createBean(className,"resources/quickfix/bean/BeanInjectDisposes.java.cdi");
	
		editResourceUtil.replaceInEditor(className+".java","import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		editResourceUtil.replaceInEditor(className+".java","@Disposes", "@Observes");
		
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.OBSERVER_INJECT));
		
		assertEquals(2,problems.size());

		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.OBSERVER_INJECT), 1);

	}
	
	// https://issues.jboss.org/browse/JBIDE-7667
	@Test
	public void testInjectProducer() {
		
		String className = "Bean5";
		
		createBean(className,"resources/quickfix/bean/BeanInjectProducer.java.cdi");

			
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.PRODUCER_INJECT));
		
		assertEquals(1,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.PRODUCER_INJECT));
			
	}
	
	// https://issues.jboss.org/browse/JBIDE-7668
	@Test
	public void testObserverWithDisposer() {
			
		String className = "Bean6";
		
		createBean(className,"resources/quickfix/bean/ObserverWithDisposer.java.cdi");
			
		List<Problem> problems= validationHelper.findProblems(validationProvider.getValidationProblem(ValidationType.OBSERVER_DISPOSES));
		
		assertEquals(2,problems.size());
		
		validationHelper.openQuickfix(validationProvider.getValidationProblem(ValidationType.OBSERVER_DISPOSES));
			
	}
	
	private void createBean(String className, String content){
		beansHelper.createBean(className, getPackageName(), false, false, false, false, false, false, false, null, null);
		ProjectExplorer pe = new ProjectExplorer();
		pe.activate();
		pe.selectAllProjects();
		new ContextMenuItem("Refresh").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.MEDIUM);
		editResourceUtil.replaceClassContentByResource(className+".java", readFile(content), false);
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);		
	}
	
	/**
	 * Need to delete all created beans because some of the tests may
	 * fail before applying quick fix and then it will affect all the next tests
	 * (There will be more problems than expected).
	 */
	@After
	public void cleanProject() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC, "main", "java", getPackageName())
				.delete();
	}
	
}
