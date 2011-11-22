package org.jboss.tools.portlet.ui.bot.test.matcher.factory;

import org.jboss.tools.portlet.ui.bot.test.matcher.SWTMatcher;
import org.jboss.tools.portlet.ui.bot.test.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.portlet.ui.bot.test.matcher.problems.NumberOfErrorsMatcher;

/**
 * Factory of matchers for Problems View. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class DefaultMatchersFactory {

	public DefaultMatchersFactory() {
		// not to be instantiated
	}
	
	public static SWTMatcher<Integer> isNumberOfErrors(){
		return new NumberOfErrorsMatcher();
	}
	
	public static SWTMatcher<String> inConsoleOutput(){
		return new ConsoleOutputMatcher();
	}
}
