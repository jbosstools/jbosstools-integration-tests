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

package org.jboss.tools.cdi.bot.test.beansxml.validation.template;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.eclipse.ui.views.markers.QuickFixPage;
import org.eclipse.reddeer.eclipse.ui.views.markers.QuickFixWizard;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.beansxml.template.BeansXMLValidationTemplate;
import org.jboss.tools.cdi.bot.test.condition.BeanXMLValidationProblemIsEmpty;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeanCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeansXMLCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewDecoratorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewStereotypeCreationWizard;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of beans.xml
 * 
 * @author Jaroslav Jankovic
 */
public class BeansXMLValidationQuickFixTemplate extends CDITestBase {
	
	protected IValidationProvider validationProvider = null;
	protected boolean requireBeansXML = true;

	@After
	public void cleanup() {
		cleanUp();
	}
	
	@Test
	public void testNoBeanComponent() {
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_ALTERNATIVE));
		editResourceUtil.replaceInEditor("Component", getPackageName()+".A1");
		
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()));
		openQuickfix(ValidationType.NO_CLASS);
		NewBeanCreationWizard bw = new NewBeanCreationWizard();
		assertEquals("A1",bw.getName());
		assertEquals(getPackageName(),bw.getPackage());
		assertTrue(bw.isAlternative());
		bw.finish();
		new WaitWhile(new ProblemExists(ProblemType.ALL));
	}
	
	
	@Test
	public void testNoStereotypeAnnotation() {
		
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_STEREOTYPE));
		editResourceUtil.replaceInEditor("Component", getPackageName()+".S1");
		
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()));
		openQuickfix(ValidationType.NO_ANNOTATION);
		NewStereotypeCreationWizard sw = new NewStereotypeCreationWizard();
		assertEquals("S1",sw.getName());
		assertEquals(getPackageName(),sw.getPackage());
		assertTrue(sw.isAlternative());
		sw.finish();
		new WaitWhile(new ProblemExists(ProblemType.ALL));	
	}
	
	@Test
	public void testNoAlternativeBeanComponent() {
		
		beansHelper.createBean("S1", getPackageName(), false, false, false, false, 
				false, false,false,null,null);
		
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_ALTERNATIVE));
		editResourceUtil.replaceInEditor("Component", getPackageName()+".S1");
		
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()));
		openQuickfix(ValidationType.ISNT_ALTERNATIVE);
		new TextEditor("S1.java").getText().contains("@Alternative");
		new WaitWhile(new ProblemExists(ProblemType.ALL));
	}
	
	
	
	@Test
	public void testNoAlternativeStereotypeAnnotation() {
		
		beansHelper.createStereotype("S2", getPackageName(), false, false, false, false, false);
		
		
		
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_STEREOTYPE));
		editResourceUtil.replaceInEditor("Component", getPackageName()+".S2");
		
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()));
		
		openQuickfix(ValidationType.ISNT_ALTERNATIVE_STEREOTYPE);
		
		new TextEditor("S2.java").getText().contains("@Alternative");
		new WaitWhile(new ProblemExists(ProblemType.ALL));
		
	}
	
	@Test
	public void testNoInterceptor() {
		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_INTERCEPTOR));
		editResourceUtil.replaceInEditor("Component", getPackageName()+".I1");
			
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()));
		
		openQuickfix(ValidationType.NO_INTERCEPTOR);
		NewInterceptorCreationWizard iw = new NewInterceptorCreationWizard();
		assertEquals("I1",iw.getName());
		assertEquals(getPackageName(),iw.getPackage());
		iw.finish();
		new WaitUntil(new BeanXMLValidationProblemIsEmpty(getProjectName()));
		
	}
	
	
	@Test
	public void testNoDecorator() {

		EditorPartWrapper bEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		bEditor.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", readFile(BeansXMLValidationTemplate.BEANS_XML_WITH_DECORATOR));
		editResourceUtil.replaceInEditor("Component", getPackageName()+".D1");
		
		new WaitWhile(new BeanXMLValidationProblemIsEmpty(getProjectName()));
		
		openQuickfix(ValidationType.NO_DECORATOR);
		NewDecoratorCreationWizard dw = new NewDecoratorCreationWizard();
		assertEquals("D1",dw.getName());
		assertEquals(getPackageName(),dw.getPackage());
		dw.addDecoratedTypeInterfaces("java.util.Set");
		dw.finish();
		new WaitUntil(new BeanXMLValidationProblemIsEmpty(PROJECT_NAME));
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-14384
	@Test
	public void testNoBeansXmlPresent() {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("src","main","webapp","WEB-INF","beans.xml").delete();
		if(requireBeansXML){
			new WaitUntil(new ProblemExists(ProblemType.WARNING));
			pe.getProject(PROJECT_NAME).select();
			//https://issues.jboss.org/browse/JBIDE-19421
			new ContextMenu().getItem("Validate").select();
			new WaitWhile(new JobIsRunning());
			try{
				new WaitUntil(new ProblemExists(ProblemType.WARNING));
			} catch (WaitTimeoutExpiredException ex){
				fail("this is a known issue JBIDE-19421");
			}
			openQuickfix(ValidationType.NO_BEANS_XML);
			NewBeansXMLCreationWizard bw = new NewBeansXMLCreationWizard();
			bw.finish();
			EditorPartWrapper bEditor = new EditorPartWrapper();
			bEditor.activateTreePage();
			//TODO check version
			new WaitWhile(new ProblemExists(ProblemType.ALL));
		} else {
			new WaitUntil(new ProblemExists(ProblemType.WARNING),TimePeriod.DEFAULT,false);
			pe.getProject(PROJECT_NAME).select();
			new ContextMenu().getItem("Validate").select();
			new WaitWhile(new JobIsRunning());
			new WaitUntil(new ProblemExists(ProblemType.WARNING),TimePeriod.DEFAULT,false);
		}
		
	}
	
	private void openQuickfix(ValidationType validationProblemType){
		ValidationProblem vProblem = validationProvider.getValidationProblem(validationProblemType);
		if(vProblem == null){
			fail("unable to find any validation problem of type"+validationProblemType);
		}
		List<Problem> foundProblems = validationHelper.findProblems(vProblem);
		assertEquals(1, foundProblems.size());
		QuickFixWizard qf = foundProblems.get(0).openQuickFix();
		QuickFixPage qp = new QuickFixPage(qf);
		List<String> fixes = qp.getAvailableFixes();
		String chosenFix = null;
		for(String fix: fixes){
			for(String quickfix: vProblem.getQuickFixes()){
				if(fix.contains(quickfix)){
					chosenFix = fix;
					break;
				}
			}
		}
		if(chosenFix == null){
			fail("Unable to find proper quickfix");
		}
		qp.selectFix(chosenFix);
		qf.finish();
	}
	
}
