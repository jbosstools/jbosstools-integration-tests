/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.reddeer.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Matcher for matching of no existing substring.
 * 
 * @author mlabuda@redhat.com
 */
public class StringNotContained extends BaseMatcher<String> {

	private String substring;
	
	public StringNotContained(String substring) {
		this.substring = substring;
	}
	
	@Override
	public boolean matches(Object o) {
		try {
			return matches((String) o);
		} catch (ClassCastException ex) {
			return false;
		}
	}
	
	public boolean matches(String text) {
		return !text.contains(substring);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("does not contain substring " + substring);
	}	
}
