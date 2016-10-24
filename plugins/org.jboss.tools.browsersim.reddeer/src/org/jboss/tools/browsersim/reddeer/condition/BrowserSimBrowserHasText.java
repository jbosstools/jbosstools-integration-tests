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
package org.jboss.tools.browsersim.reddeer.condition;

import java.rmi.RemoteException;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;

public class BrowserSimBrowserHasText extends AbstractWaitCondition{
	
	private IBrowsersimHandler bsHandler;
	private String text;
	
	public BrowserSimBrowserHasText(IBrowsersimHandler bsHandler, String text) {
		this.bsHandler = bsHandler;
		this.text = text;
	}

	@Override
	public boolean test() {
		try {
			return bsHandler.getBrowserText().contains(text);
		} catch (RemoteException e) {
			throw new EclipseLayerException("Failed while getting browser text: ",e.getCause());
		}
	}

}
