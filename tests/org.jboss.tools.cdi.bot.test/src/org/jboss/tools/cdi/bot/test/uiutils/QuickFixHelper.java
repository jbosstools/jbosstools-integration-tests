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
package org.jboss.tools.cdi.bot.test.uiutils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.tools.cdi.bot.test.annotations.ProblemsType;
import org.jboss.tools.cdi.bot.test.annotations.ValidationType;
import org.jboss.tools.cdi.bot.test.quickfix.validators.IValidationProvider;
import org.jboss.tools.cdi.bot.test.uiutils.wizards.OpenOnOptionsDialog;
import org.jboss.tools.cdi.bot.test.uiutils.wizards.QuickFixDialogWizard;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class QuickFixHelper {
	
	private SWTUtilExt util = SWTBotFactory.getUtil();
	private SWTBotExt bot = SWTBotFactory.getBot();
	
	
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
		IValidationProvider validationErrorsProvider = validationProvider;
		List<String> validationProblems = null;
		List<TreeItem> problemsInProblemsView = null;
		if (validationErrorsProvider.getAllWarningsAnnotation().contains(validationType)) {
			validationProblems = validationErrorsProvider.getAllWarningForAnnotationType(validationType);
			problemsInProblemsView = getProblems(ProblemsType.WARNINGS, projectName);
		} else {
			validationProblems = validationErrorsProvider.getAllErrorsForAnnotationType(validationType);
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
		
		QuickFixDialogWizard qfWizard = new QuickFixDialogWizard();
		
		/**
		 * if text is not specified, choose the first CDI 
		 * quickfix available proposal. Otherwise choose
		 * the one contains entered text
		 */
		String firstFix = null;
		if (text == null) {
			 firstFix = qfWizard.getDefaultCDIQuickFix();
		} else {
			firstFix = qfWizard.getCDIQuickFix(text);
		}
		String firstResource = qfWizard.getResources().get(0);
		
		qfWizard.setFix(firstFix).setResource(firstResource).finish();
		
		/**
		 * when creating beans.xml, user has to define location
		 */
		if (text != null && text.equals("Create File beans.xml")) {
			bot.waitUntil(new ShellIsActiveCondition("New beans.xml File"));
			bot.button(IDELabel.Button.FINISH).click();
		}
		
		util.waitForNonIgnoredJobs();
	}
	
	/**
	 * Method select openOnString and then open proposal dialog which
	 * is returned as object
	 * @param openOnString
	 * @param titleName
	 * @return
	 */
	public OpenOnOptionsDialog openOnDialog(String openOnString, String titleName) {
		SWTJBTExt.selectTextInSourcePane(bot, titleName,
				openOnString, 0, openOnString.length());
		bot.menu(IDELabel.Menu.EDIT).menu(IDELabel.Menu.QUICK_FIX).click();	
		bot.sleep(Timing.time1S());
		
		return new OpenOnOptionsDialog(bot);
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
		AbstractWait.sleep(5000); //wait for problems view to refresh
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
