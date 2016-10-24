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
package org.jboss.tools.browsersim.wait;

import org.jboss.tools.browsersim.rmi.BrowsersimHandler;

public class BrowsersimStarted extends AbstractWaitCondition{
	
	private BrowsersimHandler handler;
	
	public BrowsersimStarted(BrowsersimHandler handler) {
		this.handler = handler;
	}

	@Override
	public boolean test() {
		try{
			handler.getBrowser();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
