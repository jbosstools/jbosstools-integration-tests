/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltaspike.ui.bot.test.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.jboss.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;

/**
 * Returns true, if specific problem exists in problems view
 * 
 * @author Jaroslav Jankovic
 *
 */
public class SpecificProblemExists extends AbstractWaitCondition {

	private ProblemsView problemsView;
	
	private RegexMatcher regexMatcher;
	
	private ProblemType problemType;
		
	public SpecificProblemExists(RegexMatcher regexMatcher) {
		this(regexMatcher, ProblemType.ALL);
	}
	
	public SpecificProblemExists(RegexMatcher regexMatcher, ProblemType problemType) {
		this.regexMatcher = regexMatcher;
		this.problemType = problemType;
	}
	
	@Override
	public boolean test() {
		problemsView = new ProblemsView();
		problemsView.open();
		for (Problem error : problemsView.getProblems(problemType)) {
			String text = null;
			try{
				text = error.getDescription();
			} catch (CoreLayerException ex){
				continue;
			}
			if (regexMatcher.matches(text)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String description() {
		StringBuilder msg = new StringBuilder("There is no problem" +
				" matching: '" + regexMatcher + "' with severity '" + problemType + "' in Problems view \n");
		return msg.toString();
	}
	
}