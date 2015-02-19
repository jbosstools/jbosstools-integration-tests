package org.jboss.ide.eclipse.as.ui.bot.test.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;

/**
 * Checks if the active console in console view contains specified text.
 * 
 * @author Lucia Jelinkova
 *
 */
public class ConsoleContainsTextMatcher extends TypeSafeMatcher<ConsoleView> {

	private String expectedText;
	
	private String actualText;
	
	public ConsoleContainsTextMatcher(String expectedText) {
		this.expectedText = expectedText;
	}
	
	@Override
	protected boolean matchesSafely(ConsoleView view) {
		actualText = view.getConsoleText();
		return actualText.contains(expectedText);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Console should contain text '");
		description.appendText(expectedText);
		description.appendText("', but contains text '");
		description.appendText(actualText);
		description.appendText("'.");
	}
}
