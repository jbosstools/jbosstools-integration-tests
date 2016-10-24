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
package org.jboss.tools.browsersim.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.jboss.tools.browsersim.wait.BrowsersimStarted;
import org.jboss.tools.browsersim.wait.TimePeriod;
import org.jboss.tools.browsersim.wait.WaitUntil;

public class BrowsersimUtil {

	private static IBrowsersimHandler handler;
	private static boolean started = false;
	public static final String BS_HANDLER = "bsHandler";

	public static void main(final String[] args) {
		try {
			IBrowsersimHandler bsHandler = new BrowsersimHandler();
			try {
				UnicastRemoteObject.unexportObject(bsHandler, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler = (IBrowsersimHandler) UnicastRemoteObject.exportObject(bsHandler, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(BS_HANDLER, handler);

			System.out.println("Server is ready.");
		} catch (Exception e) {
			System.out.println("Server failed: " + e);
		}
		
		waitForBrowsersim();
		openBrowsersim(args);
	}

	private static void openBrowsersim(final String[] args) {
		try {
			Class br = Class.forName("org.jboss.tools.browsersim.ui.launch.BrowserSimRunner");
			Method mm = br.getMethod("main", String[].class);
			mm.invoke(null, (Object) args);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void waitForBrowsersim(){
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

	private static void notifyStarted() {
		started = true;
	}

	public static boolean isStarted() {
		return started;
	}

}
