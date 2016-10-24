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
package org.jboss.tools.browsersim.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.hamcrest.Matcher;
import org.jboss.tools.browsersim.matcher.WithMnemonicTextMatcher;

/**
 * PushButton is simple button implementation that can be pushed
 * @author Jiri Peterka
 *
 */
public class PushButton {
	
	Button button;
	
	/**
	 * Push button with given text inside given composite.
	 *
	 * @param referencedComposite the referenced composite
	 * @param text the text
	 */
	public PushButton(String text) {
		this(new WithMnemonicTextMatcher(text));
	}

	public PushButton(Matcher<?>... matchers) {
		button = WidgetLookup.activeWidget(null, org.eclipse.swt.widgets.Button.class, 
				0, createMatchers( matchers));
	}
	
	private static Matcher<?>[] createMatchers(Matcher<?>... matchers) {
		List<Matcher<?>> list= new ArrayList<Matcher<?>>();
		
		list.addAll(Arrays.asList(matchers));
		return list.toArray(new Matcher[list.size()]);
	}
	
	public void click(){
		WidgetHandler.notify(SWT.Selection, button);
	}
}
