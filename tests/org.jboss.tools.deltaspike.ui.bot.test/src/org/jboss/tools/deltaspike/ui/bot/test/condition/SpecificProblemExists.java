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

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.swt.regex.Regex;

/**
 * Returns true, if specific problem exists in problems view
 * 
 * @author Jaroslav Jankovic
 *
 */
public class SpecificProblemExists implements WaitCondition {

	private ProblemsView problemsView;
	
	private String pattern;
	
	public SpecificProblemExists(Regex regex) {
		pattern = regex.getPattern().pattern();
	}
	
	@Override
	public boolean test() {
		problemsView = new ProblemsView();
		problemsView.open();
		for (TreeItem error : problemsView.getAllErrors()) {
			RegexMatcher matcher = new RegexMatcher(pattern);
			String text = null;
			try{
				text = error.getText();
			} catch (SWTLayerException ex){
				continue;
			}
			if (matcher.matches(text)) {
				return true;
			}
		}
		for (TreeItem warning : problemsView.getAllWarnings()) {
			 RegexMatcher matcher = new  RegexMatcher(pattern);
			 String text = null;
			 try{
				 text = warning.getText();
			 } catch (SWTLayerException ex){
				 continue;
			 }
			if (matcher.matches(text)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String description() {
		StringBuilder msg = new StringBuilder("There is no problem" +
				" marker: '" + pattern + "' in Problems view \n");
		return msg.toString();
	}
	
}