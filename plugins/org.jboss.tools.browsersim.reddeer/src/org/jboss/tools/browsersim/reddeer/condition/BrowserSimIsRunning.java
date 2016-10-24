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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;

public class BrowserSimIsRunning extends AbstractWaitCondition{
	
	private Registry registry;
	private String handlerName;
	private IBrowsersimHandler bsHandler = null;
	
	public BrowserSimIsRunning(String handlerName) {
		try {
			this.handlerName = handlerName;
			this.registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public boolean test(){
		try {
			bsHandler = (IBrowsersimHandler) registry.lookup(handlerName);
		} catch (Exception e1) {
			return false;
		} 
		
		try {
			return bsHandler.isStarted();
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public IBrowsersimHandler getBSHandler(){
		return bsHandler;
	}
	
	@Override
	public String errorMessage() {
		ConsoleView cw = new ConsoleView();
		cw.open();
		String text = cw.getConsoleText();
		return "Error in console: "+text;
	}

}
