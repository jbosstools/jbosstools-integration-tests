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
package org.jboss.tools.browsersim.rmi;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.jboss.tools.browsersim.wait.BrowsersimStarted;
import org.jboss.tools.browsersim.wait.TimePeriod;
import org.jboss.tools.browsersim.wait.WaitUntil;

public class SimUtil {
	
	private static IBrowsersimHandler handlerStub;
	private static boolean started = false;
	
	protected static void startRMI(IBrowsersimHandler handler, String handlerName, String mainClass, String[] args){
		try {
			try {
				UnicastRemoteObject.unexportObject(handler, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handlerStub = (IBrowsersimHandler) UnicastRemoteObject.exportObject(handler, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(handlerName, handlerStub);

			System.out.println("Server is ready.");
		} catch (Exception e) {
			System.out.println("Server failed: " + e);
		}
		
		waitForSim();
		openSim(mainClass, args);
	}
	
	protected static void openSim(String className, final String[] args) {
		try {
			Class br = Class.forName(className);
			Method mm = br.getMethod("main", String[].class);
			mm.invoke(null, (Object) args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void waitForSim(){
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					new WaitUntil(new BrowsersimStarted(new BrowsersimHandler()), TimePeriod.LONG);
					notifyStarted();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t1.start();
	}

	protected static void notifyStarted() {
		started = true;
	}

	public static boolean isStarted() {
		return started;
	}

}
