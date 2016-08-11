/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;
import org.jboss.reddeer.core.handler.TextHandler;
import org.eclipse.swt.widgets.Text;

public class TextTooltipMatcher extends TypeSafeMatcher<Text>{
	
	private Matcher<String> messageTextMatcher;

	public TextTooltipMatcher(String messageText) {
		this(new IsEqual<String>(messageText));
	}
	
	public TextTooltipMatcher(Matcher<String> messageTextMatcher) {
		this.messageTextMatcher = messageTextMatcher;
	}
	
	@Override
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean matchesSafely(Text text) {
		String textMessage = TextHandler.getInstance().getMessage(text);
		return messageTextMatcher.matches(textMessage);
	}

}
