package org.jboss.tools.portlet.ui.bot.matcher.problems;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;

/**
 * Checks the number of Errors on the Problems view. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class NumberOfErrorsMatcher extends AbstractSWTMatcher<Integer> {

	private int expectedNumber;

	private int numberOfErrors;

	@Override
	public boolean matchesSafely(Integer expectedNumber) {
		SWTBotFactory.getOpen().perspective(ActionItem.Perspective.JAVA.LABEL);
		SWTBotTreeItem errorItem = ProblemsView.getErrorsNode(SWTBotFactory.getBot());

		if (errorItem == null){
			if (expectedNumber.equals(0)){
				return true;
			} else {
				return false;
			}
		}

		this.expectedNumber = expectedNumber;
		this.numberOfErrors = errorItem.getNodes().size();

		return expectedNumber.equals(numberOfErrors);
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("Different number of errors.\n");
		description.appendText("Number of expected errors: " + expectedNumber + "\n");
		description.appendText("Number of errors: " + numberOfErrors + "\n");
	}
}
