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

package org.jboss.tools.cdi.bot.test.quickfix.injection;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.views.markers.QuickFixWizard;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewQualifierCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.SpecifyBeanWizard;
import org.jboss.tools.cdi.reddeer.condition.QualifierIsFound;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;
import org.junit.After;
import org.junit.Test;

/**
 * Test checks if Quick Fix provides useful operations when 
 * ambiguous injection points
 * @author jjankovi
 *
 */
public class ProblemEligibleInjectionTest extends CDITestBase {
	/*
	protected static IValidationProvider validationProvider;
	
	private static final String ANIMAL = "Animal";
	private static final String DOG = "Dog";
	private static final String BROKEN_FARM = "BrokenFarm";
	private static final String QUALIFIER = "Q1";
	
	@After
	public void waitForJobs() {
		deleteAllProjects();	
	}
	
	@Test
	public void testMultipleBeansAddingExistingQualifier() {
		
		beansHelper.createQualifier(QUALIFIER, getPackageName(), false,false);

		createBean(ANIMAL, false);
		createBean(DOG, true);
		createBean(BROKEN_FARM, true);

		refreshProject();
		resolveMultipleBeans(validationProvider.getValidationProblem(ValidationType.MULTIPLE_BEAN_ELIGIBLE)
				, DOG, QUALIFIER, QualifierOperation.ADD);

		TextEditor textEditor = new TextEditor(BROKEN_FARM + ".java");
		assertTrue(textEditor.getText().contains("@Inject @" + QUALIFIER));
		textEditor = new TextEditor(DOG + ".java");
		assertTrue(textEditor.getText().contains("@" + QUALIFIER));
	}
	
	@Test
	public void testMultipleBeansAddingNonExistingQualifier() {

		createBean(ANIMAL, false);
		createBean(DOG, true);
		createBean(BROKEN_FARM, true);
		
		refreshProject();
		resolveMultipleBeans(ValidationType.MULTIPLE_BEAN_ELIGIBLE, DOG, QUALIFIER, 
				QualifierOperation.ADD);

		TextEditor textEditor = new TextEditor(BROKEN_FARM + ".java");
		assertTrue(textEditor.getText().contains("@Inject @" + QUALIFIER));
		textEditor = new TextEditor(DOG + ".java");
		assertTrue(textEditor.getText().contains("@" + QUALIFIER));
	}
	
	@Test
	public void testMultipleBeansRemovingExistingQualifier() {

		beansHelper.createQualifier(QUALIFIER, getPackageName(), false,false);

		createBean(ANIMAL, false);
		createBean(DOG, true);
		createBean(BROKEN_FARM, true);
		
		refreshProject();
		resolveMultipleBeans(validationProvider.getValidationProblem(ValidationType.MULTIPLE_BEAN_ELIGIBLE),
				DOG, QUALIFIER, QualifierOperation.REMOVE);
		
		TextEditor textEditor = new TextEditor(BROKEN_FARM + ".java");
		assertTrue(textEditor.getText().contains("@Inject private") ||textEditor.getText().contains("@Inject  private"));
		textEditor = new TextEditor(DOG + ".java");
		assertTrue(!textEditor.getText().contains("@" + QUALIFIER));
	}
	
	@Test
	public void testNoBeanEligibleAddingExistingQualifier() {

		beansHelper.createQualifier(QUALIFIER, getPackageName(), false,false);

		createBean(ANIMAL, false);
		createBean(DOG, true);
		createBean(BROKEN_FARM, true);
		
		refreshProject();
		resolveMultipleBeans(validationProvider.getValidationProblem(ValidationType.MULTIPLE_BEAN_ELIGIBLE),
				DOG, QUALIFIER, QualifierOperation.ADD);

		TextEditor textEditor = new TextEditor(BROKEN_FARM + ".java");
		textEditor = new TextEditor(DOG + ".java");
		assertTrue(textEditor.getText().contains("@" + QUALIFIER));
		
	}
	
	@Test
	public void testNoBeanEligibleRemovingExistingQualifier() {
		
		beansHelper.createQualifier(QUALIFIER, getPackageName(), false,false);

		createBean(ANIMAL, false);
		createBean(DOG, true);
		createBean(BROKEN_FARM, true);
		
		refreshProject();
		resolveMultipleBeans(validationProvider.getValidationProblem(ValidationType.MULTIPLE_BEAN_ELIGIBLE),
				QualifierOperation.REMOVE);

		TextEditor textEditor = new TextEditor(BROKEN_FARM + ".java");
		assertTrue(textEditor.getText().contains("@Inject private") || textEditor.getText().contains("@Inject  private"));
		textEditor = new TextEditor(DOG + ".java");
		assertFalse(textEditor.getText().contains("@" + QUALIFIER));
		
	}
	
	private void createBean(String name, boolean content){
		beansHelper.createBean(name, getPackageName(), false, false, false, false, false,false,false, null,null);
		if(content){
			editResourceUtil.replaceClassContentByResource(name+".java", 
					readFile("resources/quickfix/injection/"+name+".java.cdi"),false);
		}
	}
	

	private void resolveMultipleBeans(ValidationProblem validationProblem, String classToQualify, 
			String qualifier, QualifierOperation operation) {
		
		List<Problem> p = validationHelper.findProblems(validationProblem);	
		assertTrue(p.size()>0);
		
		quickFixHelper.openQuickFix(validationProblem);
		QuickFixWizard quickFixWizard = new QuickFixWizard();
		for (String availableFix : quickFixWizard.getAvailableFixes()) {
			if (availableFix.contains(classToQualify)) {
				quickFixWizard.setFix(availableFix);
				quickFixWizard.setResource(quickFixWizard.getResources().get(0));
				quickFixWizard.finish();
				break;
			}
		}
	
		SpecifyBeanWizard spBeanDialogWizard = new SpecifyBeanWizard();
		if (operation == QualifierOperation.ADD) {
			new WaitUntil(new SpecifyBeanWizardHasQualifier(spBeanDialogWizard, qualifier + " - " + getPackageName()),TimePeriod.getCustom(TimePeriod.DEFAULT.getSeconds()*2),false);
			boolean qualifFound = false;
			for (String availQualifer : spBeanDialogWizard.getAvailableQualifiers()) {
				if (availQualifer.equals(qualifier + " - " + getPackageName())) {
					qualifFound = true;
					spBeanDialogWizard.addQualifier(availQualifer);
				}
			}
			// there was no such qualifer, it has to be created, after creation it 
			// has to be added to in Bean qualifiers
			if (!qualifFound) {
				NewQualifierCreationWizard qw = spBeanDialogWizard.createNewQualifier(qualifier, getPackageName());
				qw.setName(qualifier);
				qw.finish();
				new DefaultShell("Specify CDI Bean for the Injection Point");
				new WaitUntil(new SpecifyBeanWizardHasQualifier(spBeanDialogWizard, qualifier + " - " + getPackageName()),TimePeriod.getCustom(TimePeriod.DEFAULT.getSeconds()*2),false);
				for (String availQualifer : spBeanDialogWizard.getAvailableQualifiers()) {
					if (availQualifer.equals(qualifier + " - " + getPackageName())) {						
						spBeanDialogWizard.addQualifier(availQualifer);
					}
				}
			}
			
		} else {
			new WaitUntil(new QualifierIsFound(spBeanDialogWizard,qualifier + " - " + getPackageName()));
			for (String inBeanQualifer : spBeanDialogWizard.getInBeanQualifiers()) {
				if (inBeanQualifer.equals(qualifier + " - " + getPackageName())) {
					spBeanDialogWizard.removeQualifier(inBeanQualifer);
				}
			}
		}
		
		spBeanDialogWizard.finish();
	}
*/
}