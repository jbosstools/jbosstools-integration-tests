/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.uiutils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.cdi.reddeer.annotation.ProblemsType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.QuickFixWizard;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.common.reddeer.label.IDELabel;

public class QuickFixHelper {
	
	
	public void checkQuickFix(ValidationType validationType, String projectName,
			IValidationProvider validationProvider) {
		checkQuickFix(validationType, null, projectName, validationProvider);
	}
	
	/**
	 * checkQuickFix is the most important method in this class. It
	 * gets validation error prior to component type and annotation type,
	 * then it resolve validation error through quick fix
	 * wizard and finally check if validation errors was fixed through
	 * this wizard
	 * @param validationType
	 * @param compType
	 */
	
	public void checkQuickFix(ValidationType validationType, String text, String projectName,
			IValidationProvider validationProvider) {
		TreeItem validationProblem = getProblem(
				validationType, projectName, validationProvider);		
		assertNotNull("Expected validation was not found in Problems View", validationProblem);
		resolveQuickFix(validationProblem, text);
		validationProblem = getProblem(
				validationType, projectName, validationProvider);		
		assertNull("Validation problem should be fixed.", validationProblem);
	}
	
	/**
	 * Methods gets the particular validation problem located in Problems View by
	 * using specific ValidationErrorsProvider
	 * @param validationType
	 * @param compType
	 * @return
	 */
	public TreeItem getProblem(ValidationType validationType, String projectName, IValidationProvider validationProvider) {		
		List<String> validationProblems = null;
		List<TreeItem> problemsInProblemsView = null;
		if (validationProvider.getAllWarningsAnnotation().contains(validationType)) {
			validationProblems = validationProvider.getAllWarningForAnnotationType(validationType);
			problemsInProblemsView = getProblems(ProblemsType.WARNINGS, projectName);
		} else {
			validationProblems = validationProvider.getAllErrorsForAnnotationType(validationType);
			problemsInProblemsView = getProblems(ProblemsType.ERRORS, projectName);
		}
		for (TreeItem ti: problemsInProblemsView) {
			for (String validationProblem: validationProblems) {					
				if (ti.getText().contains(validationProblem)) {										
					return ti;
				}
			}
		}
		return null;
	}
	
	/**
	 * Method resolves particular validation problem (parameter ti).
	 * It simply open context menu for param "ti", open menu "Quick Fix" and
	 * chooses first option and confirms it (resolve it)
	 * @param ti
	 */
	
	private void resolveQuickFix(TreeItem ti, String text) {
		openQuickFix(ti);
		
		QuickFixWizard qfWizard = new QuickFixWizard();
		
		String firstFix = null;
		if (text == null) {
			 firstFix = qfWizard.getDefaultCDIQuickFix();
		} else {
			firstFix = qfWizard.getCDIQuickFix(text);
		}
		String firstResource = qfWizard.getResources().get(0);
		
		qfWizard.setFix(firstFix);
		qfWizard.setResource(firstResource);
		qfWizard.finish();
		
		if (text != null && text.equals("Create File beans.xml")) {
			new DefaultShell("New beans.xml File");
			new PushButton(IDELabel.Button.FINISH).click();
		}
	}
	
	/**
	 * Method select openOnString and then open proposal dialog which
	 * is returned as object
	 * @param openOnString
	 * @param titleName
	 * @return
	 */
	
	public void openOnDialog() {
		new ShellMenu(IDELabel.Menu.EDIT,IDELabel.Menu.QUICK_FIX).select();	
		new DefaultShell();
	}
	
	/**
	 * Method open context menu for given tree item and opens Quick Fix option
	 * @param item
	 */
	public void openQuickFix(TreeItem item) {
		item.select();
		new ContextMenu(IDELabel.Menu.QUICK_FIX).select();
	}
	
	/**
	 * Methods gets all problems of given type
	 * @param problemType
	 * @return array of problems of given type
	 */
	public List<TreeItem> getProblems(ProblemsType problemType, String projectName) {
		ProblemsView pv = new ProblemsView();
		pv.open();
		AbstractWait.sleep(TimePeriod.NORMAL); //wait for problems view to refresh
		List<TreeItem> result = new ArrayList<TreeItem>();
		if (problemType == ProblemsType.WARNINGS) {
			for(TreeItem warning: pv.getAllWarnings()){
				if(warning.getCell(1).contains(projectName) && warning.getCell(4).equals("CDI Problem")){
					result.add(warning);
				} else if (warning.getCell(2).contains(projectName)){
					result.add(warning);
				}
			}
			return result;
		} 
		if (problemType == ProblemsType.ERRORS) {
			for(TreeItem error: pv.getAllErrors()){
				if(error.getCell(1).contains(projectName) && error.getCell(4).equals("CDI Problem")){
					result.add(error);
				} else if (error.getCell(2).contains(projectName)){
					result.add(error);
				}
			}
			return result;
		}
		return null;
	}
	
	/**
	 * Method gets allProblems in problemsView as array of SWTBotTreeItem
	 * @return
	 */
	public  List<TreeItem> getAllProblems(String projectName) {
		
		 final List<TreeItem> warningProblemsTree = getProblems(ProblemsType.WARNINGS, projectName);
		
		 final List<TreeItem> errorProblemsTree = getProblems(ProblemsType.ERRORS, projectName);
		
		 return new ArrayList<TreeItem>() { { addAll(warningProblemsTree); addAll(errorProblemsTree); } };
	}
}
