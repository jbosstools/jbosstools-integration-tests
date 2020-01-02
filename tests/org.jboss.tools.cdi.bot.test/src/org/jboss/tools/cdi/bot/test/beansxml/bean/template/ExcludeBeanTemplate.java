/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.bean.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;
import org.junit.Before;
import org.junit.Test;

//based on JBIDE-14765
public class ExcludeBeanTemplate extends CDITestBase{
	
	protected IValidationProvider validationProvider= null;
	
	@Before
	public void prepareWorkspace1(){
		excludeInBeansXml();
		createBeans();
		
	}
	
	private void createBeans(){
		beansHelper.createBean("Bean1",  "exclude.p1", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean2",  "exclude.p1", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean3",  "exclude.p2", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean4",  "exclude.p2.p3", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean5",  "exclude.p4", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean6",  "exclude.p4.p5", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean7",  "exclude.p6", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
	}
	
	private void excludeInBeansXml() {
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", readFile("resources/excluded/beans.xm_"),true);
	}
	
	@Test
	public void testExcludedBeans(){
		NewClassCreationWizard jd = new NewClassCreationWizard();
		jd.open();
		NewClassWizardPage jp = new NewClassWizardPage(jd);
		jp.setName("TestExcluded");
		jp.setPackage("test");
		jd.finish();
		
		TextEditor ed = new TextEditor("TestExcluded.java");
		editResourceUtil.replaceClassContentByResource("TestExcluded.java",
				readFile("resources/excluded/TestExcluded.jav_"), false);
		
		String warning = "org.eclipse.ui.workbench.texteditor.warning";
		
		new WaitUntil(new EditorHasValidationMarkers(ed));
		
		List<Marker> markers = ed.getMarkers();
		assertEquals(3, markers.size());
		
		ValidationProblem expected = validationProvider.getValidationProblem(ValidationType.NO_BEAN_ELIGIBLE);
		
		for(Marker m: ed.getMarkers()){
			
			// JBIDE-26664
			String expectedJSRVersion = expected.getJSR();
			if (CDIVersion.equals("1.2")) {
				expectedJSRVersion = "JSR-365";
			}
			
			assertTrue((m.getLineNumber() == 16 && m.getType().equals(warning) && m.getText().contains(expected.getMessage())
					&& m.getText().contains(expectedJSRVersion)) ||
					(m.getLineNumber() == 18 && m.getType().equals(warning) && m.getText().contains(expected.getMessage())
					&& m.getText().contains(expectedJSRVersion)) ||
					(m.getLineNumber() == 19 && m.getType().equals(warning) && m.getText().contains(expected.getMessage())
					&& m.getText().contains(expectedJSRVersion)) );
		}
		
		ProblemsView pw = new ProblemsView();
		pw.open();
		assertEquals(3,pw.getProblems(ProblemType.ALL).size());
		
		List<Problem> foundProblems = validationHelper.findProblems(expected);
		assertEquals(3,foundProblems.size());
	}
	

}
