/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.browsersim.matcher;

import org.hamcrest.Matcher;

/**
 * Builder for building more complex matchers.
 *
 * @author Jiri Peterka
 *
 */
public class MatcherBuilder {

	private static MatcherBuilder instance;

	/**
	 * Gets singleton instance of MatcherBuilder.
	 *
	 * @return instance of MatcherBuilder
	 */
	public static MatcherBuilder getInstance() {
		if (instance == null) {
			instance = new MatcherBuilder();
		}
		return instance;
	}

	private MatcherBuilder() {
	}

	/**
	 * Adds matcher into array of matchers.
	 * 
	 * @param matchers array of matchers
	 * @param matcher matcher to add to array of matchers
	 * @return new array containing old array of matchers and new matcher 
	 */
	@SuppressWarnings("rawtypes")
	public Matcher[] addMatcher(Matcher[] matchers, Matcher matcher) {
		Matcher[] finalMatchers = new Matcher[matchers.length + 1];
		for (int i = 0; i < matchers.length; i++) {
			finalMatchers[i] = matchers[i];
		}
		finalMatchers[finalMatchers.length - 1] = matcher;

		return finalMatchers;
	}
}
