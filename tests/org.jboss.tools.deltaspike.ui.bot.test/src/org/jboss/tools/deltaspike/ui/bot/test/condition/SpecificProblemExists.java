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

import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.common.matcher.RegexMatcher;

/**
 * Returns true, if specific problem exists in problems view
 * 
 * @author Jaroslav Jankovic
 *
 */
public class SpecificProblemExists extends AbstractWaitCondition {

	private ProblemsView problemsView;
	
	private RegexMatcher regexMatcher;
		
	public SpecificProblemExists(RegexMatcher regexMatcher) {
		this.regexMatcher = regexMatcher;
	}
	
	@Override
	public boolean test() {
		problemsView = new ProblemsView();
		problemsView.open();
		for (Problem error : problemsView.getProblems(ProblemType.ERROR)) {
			String text = null;
			try{
				text = error.getDescription();
			} catch (SWTLayerException ex){
				continue;
			}
			if (regexMatcher.matches(text)) {
				return true;
			}
		}
		for (Problem warning : problemsView.getProblems(ProblemType.WARNING)) {
			 String text = null;
			 try{
				 text = warning.getDescription();
			 } catch (SWTLayerException ex){
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
				" matching: '" + regexMatcher + "' in Problems view \n");
		return msg.toString();
	}
	
}