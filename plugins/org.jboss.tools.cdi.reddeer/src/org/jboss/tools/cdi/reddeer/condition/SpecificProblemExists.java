/*******************************************************************************
 * Copyright (c) 2010-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.reddeer.condition;

import java.util.List;

import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerDescriptionMatcher;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.hamcrest.Matcher;

/**
 * Returns true, if specific problem exists in problems view
 * 
 * @author Jaroslav Jankovic
 *
 */
public class SpecificProblemExists extends ProblemExists {
		
	private Matcher<String> matcher; 
	private ProblemType problemType;
	
	public SpecificProblemExists(Matcher<String> matcher) {
		this(matcher, ProblemType.ALL);
	}
	
	public SpecificProblemExists(Matcher<String> matcher, ProblemType problemType) {
		super(problemType, new MarkerDescriptionMatcher(matcher));
		this.matcher = matcher;
		this.problemType = problemType;
	}
	
	@Override
	public String errorMessageUntil() {
		StringBuilder msg = new StringBuilder("There is no problem matching: '");
		msg.append(matcher);
		msg.append("' with severity '");
		msg.append(problemType);
		msg.append("' in Problems view. \n");
		
		ProblemsView pw = new ProblemsView();
		pw.open();
		List<Problem> allProblems = pw.getProblems(ProblemType.ALL);
		msg.append("There are these problems:\n");
		msg.append(allProblems);
		
		return msg.toString();
	}
	
	@Override
	public String errorMessageWhile() {
		StringBuilder msg = new StringBuilder("There is still problem matching: '");
		msg.append(matcher);
		msg.append("' with severity '");
		msg.append(problemType);
		msg.append("' in Problems view \n");
		return msg.toString();
	}
	
}