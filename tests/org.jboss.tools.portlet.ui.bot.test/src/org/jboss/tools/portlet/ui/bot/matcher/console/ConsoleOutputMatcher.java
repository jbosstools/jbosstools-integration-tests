package org.jboss.tools.portlet.ui.bot.matcher.console;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;

/**
 * Checks if the console contains specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ConsoleOutputMatcher extends AbstractSWTMatcher<String> {

	private org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher wrappedMatcher;
	
	public ConsoleOutputMatcher() {
		wrappedMatcher = new org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher();
	}
	
	public ConsoleOutputMatcher(TaskDuration duration) {
		wrappedMatcher = new org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher(duration);
	}
	
	@Override
	public boolean matchesSafely(String item) {
		return wrappedMatcher.matchesSafely(item);
	}

	@Override
	public void describeTo(Description description) {
		wrappedMatcher.describeTo(description);
	}
}
