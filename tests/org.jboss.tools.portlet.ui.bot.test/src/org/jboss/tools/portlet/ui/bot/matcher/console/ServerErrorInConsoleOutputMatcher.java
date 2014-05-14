package org.jboss.tools.portlet.ui.bot.matcher.console;

/**
 * Matcher for the current state of workspace - that there is an server error in console output. 
 * 
 * @author Radoslav Rabara
 * 
 * @see ErrorInconsoleOutputMatcher
 */
public class ServerErrorInConsoleOutputMatcher extends StringInConsoleOutputMatcher {

	public ServerErrorInConsoleOutputMatcher() {
		super("ERROR");
	}
	
}
