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
package org.jboss.tools.cdi.reddeer.uiutils;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerDescriptionMatcher;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.eclipse.ui.views.markers.QuickFixPage;
import org.eclipse.reddeer.eclipse.ui.views.markers.QuickFixWizard;
import org.hamcrest.core.StringContains;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;

public class ValidationHelper {
	
	private static final Logger log = new Logger(ValidationHelper.class);
	
	public List<Problem> findProblems(ValidationProblem validationProblem){
		StringContains regexpMatcher = new StringContains(validationProblem.getMessage());
		MarkerDescriptionMatcher descriptionMatcher = new MarkerDescriptionMatcher(regexpMatcher);
		List<Problem> filteredProblems = new ArrayList<Problem>();
		
		try {
			/**
			 * Sometimes happens that marker is displayed and suddenly is hidden and displayed again,
			 * so it is better to wait and doublecheck.
			 */
			new WaitUntil(new ProblemExists(validationProblem.getProblemType(), descriptionMatcher),TimePeriod.LONG);
			new WaitWhile(new ProblemExists(validationProblem.getProblemType(), descriptionMatcher),TimePeriod.getCustom(5), false);
			new WaitUntil(new ProblemExists(validationProblem.getProblemType(), descriptionMatcher),TimePeriod.DEFAULT);
			
			ProblemsView pw = new ProblemsView();
			pw.open();
			new WaitUntil(new ProblemExists(ProblemType.ALL), TimePeriod.MEDIUM, false);
			filteredProblems = pw.getProblems(validationProblem.getProblemType(), descriptionMatcher);
		} catch (WaitTimeoutExpiredException ex) {
			log.warn(validationProblem.toString() + " not found.");
		}
		
		logProblemViewStatus(filteredProblems);
		
		return filteredProblems;
	}
	
	public void openQuickfix(ValidationProblem validationProblem){
		openQuickfix(validationProblem,0);
	}
	
	public void openQuickfix(ValidationProblem validationProblem, int quickfixIndex){
		List<Problem> foundProblems = findProblems(validationProblem);
		QuickFixWizard qf = foundProblems.get(0).openQuickFix();
		QuickFixPage qp = new QuickFixPage(qf);
		List<String> fixes = qp.getAvailableFixes();
		String chosenFix = null;
		String proposedFix = validationProblem.getQuickFixes().get(quickfixIndex);
		if(proposedFix == null || proposedFix.isEmpty()){
			fail("No quickfix is defined");
		}
		for(String fix: fixes){
			if(fix.contains(validationProblem.getQuickFixes().get(quickfixIndex))){
				chosenFix = fix;
				break;
			}
		}
		if(chosenFix == null){
			fail("Unable to find proper quickfix");
		}
		qp.selectFix(chosenFix);
		qf.finish();
	}
	
	private void logProblemViewStatus(List<Problem> filteredProblems) {
		ProblemsView pw = new ProblemsView();
		pw.open();
		List<Problem> allProblems = pw.getProblems(ProblemType.ALL);
		
		String filteredProblemsString = filteredProblems.toString();
		String allProblemsString = allProblems.toString();
		
		log.debug("Matching problems: " + filteredProblemsString);
		log.debug("All problems: " + allProblemsString);
	}	
}
