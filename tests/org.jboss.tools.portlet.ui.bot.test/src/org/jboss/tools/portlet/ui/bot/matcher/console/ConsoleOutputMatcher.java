package org.jboss.tools.portlet.ui.bot.matcher.console;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;

/**
 * Checks if the console contains specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ConsoleOutputMatcher extends AbstractSWTMatcher<String> {

	private ConsoleView console;
	private String text;
	
	public ConsoleOutputMatcher() {
		super();
		console = new ConsoleView();
		console.open();
	}
	
	@Override
	public void describeTo(Description description) {
		// automatically generated
	}

	@Override
	protected boolean matchesSafely(String arg0) {
		if(arg0 instanceof String){
			text = (String) arg0;
			return console.getConsoleText().contains(text);
		}
		return false;
	}
}
