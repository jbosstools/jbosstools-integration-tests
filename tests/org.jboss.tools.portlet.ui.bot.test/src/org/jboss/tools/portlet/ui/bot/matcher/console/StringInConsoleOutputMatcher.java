package org.jboss.tools.portlet.ui.bot.matcher.console;

import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.inConsoleOutput;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.SWTMatcher;

/**
 * Matcher for the current state of workspace - that there is given text in console output. 
 * 
 * @author Radoslav Rabara
 *
 */
public class StringInConsoleOutputMatcher extends BaseMatcher<Void> implements SWTMatcher<Void> {

	private ConsoleOutputMatcher consoleOutputMatcher;
	
	private String text;
	
	public StringInConsoleOutputMatcher(String text) {
		consoleOutputMatcher = (ConsoleOutputMatcher) inConsoleOutput();
		this.text = text;
	}
	
	@Override
	public boolean matches(Object item) {
		return consoleOutputMatcher.matchesSafely(text);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("\""+text+"\" is in console output\n");
		description.appendDescriptionOf(consoleOutputMatcher);		
	}

}
