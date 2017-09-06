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

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.core.handler.ControlHandler;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.swt.impl.browser.InternalBrowser;

public class BrowserContainsText extends AbstractWaitCondition{
	
	private InternalBrowser ib;
	private String text;
	
	public BrowserContainsText(InternalBrowser ib, String text) {
		this.ib = ib;
		this.text = text;
	}

	@Override
	public boolean test() {
		ControlHandler.getInstance().setFocus(ib.getSWTWidget());
		
		Display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				ib.getSWTWidget().forceFocus();
				
			}
		});
		return ib.getText().contains(text);
	}
	
	@Override
	public String description() {
		return "Internal browser contains text '"+text+"'";
	}

}
