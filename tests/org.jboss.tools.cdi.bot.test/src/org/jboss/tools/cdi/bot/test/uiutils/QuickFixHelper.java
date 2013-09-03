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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.cdi.bot.test.annotations.ProblemsType;
import org.jboss.tools.cdi.bot.test.annotations.ValidationType;
import org.jboss.tools.cdi.bot.test.quickfix.validators.IValidationProvider;
import org.jboss.tools.cdi.bot.test.uiutils.wizards.OpenOnOptionsDialog;
import org.jboss.tools.cdi.bot.test.uiutils.wizards.QuickFixDialogWizard;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;

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
		SWTBotTreeItem validationProblem = getProblem(
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
	public SWTBotTreeItem getProblem(ValidationType validationType, String projectName,
			IValidationProvider validationProvider) {		
		IValidationProvider validationErrorsProvider = validationProvider;
		List<String> validationProblems = null;
		SWTBotTreeItem[] problemsInProblemsView = null;
		if (validationErrorsProvider.getAllWarningsAnnotation().contains(validationType)) {
			validationProblems = validationErrorsProvider.getAllWarningForAnnotationType(validationType);
			problemsInProblemsView = getProblems(ProblemsType.WARNINGS, projectName);
		} else {
			validationProblems = validationErrorsProvider.getAllErrorsForAnnotationType(validationType);
			problemsInProblemsView = getProblems(ProblemsType.ERRORS, projectName);
		}
		for (SWTBotTreeItem ti: problemsInProblemsView) {
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
	private void resolveQuickFix(SWTBotTreeItem ti, String text) {
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
	public void openQuickFix(SWTBotTreeItem item) {
		SWTBotTree problemsTree = bot.viewByTitle(
				ViewType.PROBLEMS.getViewLabel()).bot().tree();
		ContextMenuHelper.prepareTreeItemForContextMenu(
				problemsTree, item);
		new SWTBotMenu(ContextMenuHelper.getContextMenu(problemsTree, 
				IDELabel.Menu.QUICK_FIX, false)).click();
	}
	
	/**
	 * Methods gets all problems of given type
	 * @param problemType
	 * @return array of problems of given type
	 */
	public SWTBotTreeItem[] getProblems(ProblemsType problemType, String projectName) {
		SWTEclipseExt.showView(bot,ViewType.PROBLEMS);
		if (problemType == ProblemsType.WARNINGS) {
			SWTBotTreeItem[] projectRelatedProblemItems = ProblemsView.getFilteredWarningsTreeItems(
					bot, null, null, projectName, "CDI Problem");
			SWTBotTreeItem[] resourceRelatedProblemItems = ProblemsView.getFilteredWarningsTreeItems(
					bot, null, "/" + projectName, null, null);
			SWTBotTreeItem[] result = concatArrays(projectRelatedProblemItems, 
					resourceRelatedProblemItems);
			return result;
		} 
		if (problemType == ProblemsType.ERRORS) {
			SWTBotTreeItem[] projectRelatedErrors = ProblemsView.getFilteredErrorsTreeItems(
					bot, null, null, projectName, "CDI Problem");;
			SWTBotTreeItem[] resourceRelatedErrors = ProblemsView.getFilteredErrorsTreeItems(
					bot, null, "/" + projectName, null, null);
			SWTBotTreeItem[] result = concatArrays(projectRelatedErrors, resourceRelatedErrors);
			return result;
		}
		return null;
	}
	
	/**
	 * Array concatenation
	 * @param aArray
	 * @param bArray
	 * @return aArray `concat` bArray
	 */
	private static <T> T[] concatArrays(T[] aArray, T[] bArray) {
		Collection<T> sum = new ArrayList<T>();
		sum.addAll(Arrays.asList(aArray));
		sum.addAll(Arrays.asList(bArray));
		T[] result =  sum.toArray(aArray);
		return result;
	}
	
	/**
	 * Method gets allProblems in problemsView as array of SWTBotTreeItem
	 * @return
	 */
	public SWTBotTreeItem[] getAllProblems(String projectName) {
		
		SWTBotTreeItem[] warningProblemsTree = getProblems(ProblemsType.WARNINGS, projectName);
		
		SWTBotTreeItem[] errorProblemsTree = getProblems(ProblemsType.ERRORS, projectName);
		
		return concatArrays(warningProblemsTree, errorProblemsTree);
	}
}
