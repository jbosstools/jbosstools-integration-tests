package org.jboss.tools.portlet.ui.bot.matcher.console;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;

/**
 * Checks if the console contains specified text. 
 * 
 * If used with static method assertThatInWorkspace("test", new ConsoleOutputMatcher())
 * it produces following failure trace's message:
 * 'Expected that "text" is in console output.
 * The console output was:'
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 * @author Radoslav Rabara
 *
 */
public class ConsoleOutputMatcher extends AbstractSWTMatcher<String> {

	private ConsoleView console;
	private String consoleText;

	public ConsoleOutputMatcher() {
		super();
		console = new ConsoleView();
		console.open();
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is in console output."
				+ "\nThe console output was:\n"+consoleText);
	}

	@Override
	protected boolean matchesSafely(String text) {
		if(text instanceof String){
			consoleText = console.getConsoleText();
			return consoleText.contains(text);
		}
		return false;
	}
}
