package org.jboss.tools.portlet.ui.bot.matcher.problems;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;

/**
 * Checks the number of Errors on the Problems view. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class NumberOfErrorsMatcher extends JavaPerspectiveAbstractSWTMatcher<Integer> {

	private int numberOfErrors;

	@Override
	protected boolean matchesSafelyInJavaPerspective(Integer expectedNumber) {
		numberOfErrors = new ProblemsView().getAllErrors().size();
		return numberOfErrors == expectedNumber;
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("is number of errors in workspace but there are " + numberOfErrors + " errors");
	}
	
}
