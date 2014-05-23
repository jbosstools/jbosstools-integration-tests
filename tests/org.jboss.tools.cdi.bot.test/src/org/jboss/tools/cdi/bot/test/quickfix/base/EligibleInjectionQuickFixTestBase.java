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

package org.jboss.tools.cdi.bot.test.quickfix.base;

import static org.junit.Assert.assertNotNull;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.annotations.ValidationType;
import org.jboss.tools.cdi.bot.test.condition.SpecifyBeanWizardHasQualifier;
import org.jboss.tools.cdi.bot.test.quickfix.injection.QualifierOperation;
import org.jboss.tools.cdi.bot.test.quickfix.validators.BeanValidationProvider;
import org.jboss.tools.cdi.bot.test.quickfix.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewQualifierCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.QuickFixWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.SpecifyBeanWizard;
import org.jboss.tools.cdi.reddeer.condition.QualifierIsFound;

public class EligibleInjectionQuickFixTestBase extends CDITestBase {
	
	private static IValidationProvider validationProvider = new BeanValidationProvider();
	
	public IValidationProvider validationProvider() {
		return validationProvider;
	}
	
	/**
	 * Method resolves multiple bean injection problem. By setting class which
	 * should be more qualified and qualifier name it resolves this problem.
	 * If qualifier doesn't exist, by using qualifier wizard it creates the new
	 * one and uses it to resolve problem
	 * @param classToQualify
	 * @param qualifier
	 */
	public void resolveMultipleBeans(ValidationType validationType, String classToQualify, 
			String qualifier, QualifierOperation operation) {
		
		TreeItem validationProblem = quickFixHelper.getProblem(
				validationType, getProjectName(), validationProvider());		
		assertNotNull(validationProblem);
		
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
			new WaitUntil(new SpecifyBeanWizardHasQualifier(spBeanDialogWizard, qualifier + " - " + getPackageName()),TimePeriod.getCustom(TimePeriod.NORMAL.getSeconds()*2),false);
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
				new WaitUntil(new SpecifyBeanWizardHasQualifier(spBeanDialogWizard, qualifier + " - " + getPackageName()),TimePeriod.getCustom(TimePeriod.NORMAL.getSeconds()*2),false);
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

}
