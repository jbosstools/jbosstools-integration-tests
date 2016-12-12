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
package org.jboss.tools.livereload.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;

public class BrowserContainsText extends AbstractWaitCondition{
	
	private InternalBrowser ib;
	private String text;
	
	public BrowserContainsText(InternalBrowser ib, String text) {
		this.ib = ib;
		this.text = text;
	}

	@Override
	public boolean test() {
		WidgetHandler.getInstance().setFocus(ib.getSWTWidget());
		return ib.getText().contains(text);
	}
	
	@Override
	public String description() {
		return "Internal browser contains text '"+text+"'";
	}

	/* (non-Javadoc)
	 * @see org.jboss.reddeer.common.condition.WaitCondition#errorMessage()
	 */
	@Override
	public String errorMessage() {
		return "Expected text '"+text+"' but was '"+ib.getText()+"'";
	}

}
