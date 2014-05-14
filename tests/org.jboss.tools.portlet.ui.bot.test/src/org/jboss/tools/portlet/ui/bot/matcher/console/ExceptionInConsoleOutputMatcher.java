package org.jboss.tools.portlet.ui.bot.matcher.console;

/**
 * Matcher for the current state of workspace - that there is an exception in console output. 
 * 
 * @author Lucia Jelinkova
 * @author Radoslav Rabara
 *
 */
public class ExceptionInConsoleOutputMatcher extends StringInConsoleOutputMatcher {
	
	public ExceptionInConsoleOutputMatcher() {
		super("Exception: ");
	}
	
}
