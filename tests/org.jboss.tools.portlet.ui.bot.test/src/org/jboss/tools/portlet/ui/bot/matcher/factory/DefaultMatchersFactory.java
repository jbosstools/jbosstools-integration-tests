package org.jboss.tools.portlet.ui.bot.matcher.factory;

import org.hamcrest.BaseMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.SWTMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.console.ExceptionInConsoleOutputMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.problems.NumberOfErrorsMatcher;

/**
 * Factory of matchers for Problems View. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class DefaultMatchersFactory {

	private DefaultMatchersFactory() {
		// not to be instantiated
	}
	
	public static SWTMatcher<Integer> isNumberOfErrors(){
		return new NumberOfErrorsMatcher();
	}
	
	public static AbstractSWTMatcher<String> inConsoleOutput(){
		return new ConsoleOutputMatcher();
	}
	
	public static BaseMatcher<Void> exceptionInConsoleOutput(){
		return new ExceptionInConsoleOutputMatcher();
	}
}
