package org.jboss.tools.portlet.ui.bot.matcher.console;

import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.inConsoleOutput;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.SWTMatcher;

/**
 * Matcher for the current state of workspace - that there is an exception in console output. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExceptionInConsoleOutputMatcher extends AbstractSWTMatcher<Void> {

	private SWTMatcher<String> consoleOutputMatcher;

	public ExceptionInConsoleOutputMatcher() {
		super();
		consoleOutputMatcher = inConsoleOutput();
	}
	
	@Override
	public boolean matchesSafely(Void item) {
		return consoleOutputMatcher.matches("Exception:");
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("exception is in console output");
	}
}
