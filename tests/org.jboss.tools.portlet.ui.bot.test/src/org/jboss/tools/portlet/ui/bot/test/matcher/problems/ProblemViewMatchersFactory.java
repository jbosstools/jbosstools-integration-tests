package org.jboss.tools.portlet.ui.bot.test.matcher.problems;

import org.jboss.tools.portlet.ui.bot.test.matcher.SWTMatcher;

/**
 * Factory of matchers for Problems View. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ProblemViewMatchersFactory {

	public ProblemViewMatchersFactory() {
		// not to be instantiated
	}
	
	public static SWTMatcher<Integer> isNumberOfErrors(){
		return new NumberOfErrorsMatcher();
	}
}
