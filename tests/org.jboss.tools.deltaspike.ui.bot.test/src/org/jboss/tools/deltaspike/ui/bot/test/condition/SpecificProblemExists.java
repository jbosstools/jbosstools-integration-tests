package org.jboss.tools.deltaspike.ui.bot.test.condition;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
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
			if (matcher.matches(error.getText())) {
				return true;
			}
		}
		for (TreeItem warning : problemsView.getAllWarnings()) {
			RegexMatcher matcher = new RegexMatcher(pattern);
			if (matcher.matches(warning.getText())) {
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