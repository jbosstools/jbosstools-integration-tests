/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.matcher;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

@SuppressWarnings("restriction")
public class WidgetExistsInShellMatcher extends TypeSafeMatcher<SWTBotShell> {

	private Class<? extends Widget> widgetClass = null;
	
	public WidgetExistsInShellMatcher(Class<? extends Widget> clazz) {
		this.widgetClass = clazz;
	}
	
	public void describeTo(Description description) {
		description.appendText("Shell does not contain any widget of type" + widgetClass);
	}

	@Override
	public boolean matchesSafely(SWTBotShell shell) {
		try {
			shell.bot().widget(widgetOfType(widgetClass));
			return true;
		} catch (WidgetNotFoundException wnfe) {
			return false;
		}
	}

}
