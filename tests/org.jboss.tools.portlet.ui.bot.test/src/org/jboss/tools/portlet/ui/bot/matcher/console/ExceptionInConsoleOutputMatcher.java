package org.jboss.tools.portlet.ui.bot.matcher.console;

import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.inConsoleOutput;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Matcher for the current state of workspace - that there is an exception in console output. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExceptionInConsoleOutputMatcher extends BaseMatcher<Void> {

	private ConsoleOutputMatcher consoleOutputMatcher;

	public ExceptionInConsoleOutputMatcher() {
		super();
		consoleOutputMatcher = (ConsoleOutputMatcher) inConsoleOutput();
	}
	
	@Override
	public boolean matches(Object item) {
		return consoleOutputMatcher.matchesSafely("Exception: ");
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("exception is in console output");
	}

}
