package org.jboss.tools.portlet.ui.bot.matcher.console;

/**
 * Matcher for the current state of workspace - that there is an error in console output.
 * 
 * @author Radoslav Rabara
 * @author Lucia Jelinkova
 * 
 * @see ServerErrorInConsoleOutputMatcher
 */
public class ErrorInConsoleOutputMatcher extends StringInConsoleOutputMatcher {

	public ErrorInConsoleOutputMatcher() {
		super("Error: ");
	}
	
}
