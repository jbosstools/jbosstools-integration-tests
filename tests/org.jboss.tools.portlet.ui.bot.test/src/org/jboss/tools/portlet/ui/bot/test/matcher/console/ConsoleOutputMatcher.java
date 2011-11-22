package org.jboss.tools.portlet.ui.bot.test.matcher.console;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.test.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * Checks if the console contains specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ConsoleOutputMatcher extends AbstractSWTMatcher<String> {

	private String consoleText;
	
	@Override
	public boolean matchesSafely(String item) {
		consoleText = SWTBotFactory.getConsole().getConsoleText();
		return consoleText.contains(item);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("console contains specified text [" + consoleText + "]");
	}
}
