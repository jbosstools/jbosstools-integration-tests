package org.jboss.tools.portlet.ui.bot.matcher.problems;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;

/**
 * Checks the number of Errors on the Problems view. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class NumberOfErrorsMatcher extends JavaPerspectiveAbstractSWTMatcher<Integer> {

	private int numberOfErrors;

	@Override
	protected boolean matchesSafelyInJavaPerspective(Integer expectedNumber) {
		SWTBotTreeItem errorItem = ProblemsView.getErrorsNode(SWTBotFactory.getBot());

		if (errorItem == null){
			if (expectedNumber.equals(0)){
				return true;
			} else {
				return false;
			}
		}

		this.numberOfErrors = errorItem.getNodes().size();

		return expectedNumber.equals(numberOfErrors);
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("is number of errors in workspace but there are " + numberOfErrors + " errors");
	}
}
