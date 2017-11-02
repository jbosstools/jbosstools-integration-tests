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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.eclipse.reddeer.common.condition.WaitCondition;
import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.jboss.tools.browsersim.wait.BrowsersimStarted;

public class SimUtil {

	private static IBrowsersimHandler handlerStub;
	private static boolean started = false;
	private static final Logger log = Logger.getLogger(SimUtil.class);

	protected static void startRMI(IBrowsersimHandler handler, String handlerName, String mainClass, String[] args) {
		try {
			try {
				UnicastRemoteObject.unexportObject(handler, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("RMI: export handler object");
			handlerStub = (IBrowsersimHandler) UnicastRemoteObject.exportObject(handler, 0);
			System.out.println("RMI: get regitry");
			Registry registry = LocateRegistry.getRegistry();
			System.out.println("RMI: registry rebind");
			registry.rebind(handlerName, handlerStub);
			System.out.println("Server is ready.");
		} catch (Exception e) {
			System.out.println("Server failed: " + e);
		}

		waitForSim(handler);
		openSim(mainClass, args);
	}

	protected static void openSim(final String className, final String[] args) {
		try {
			Class br = Class.forName(className);
			Method mm = br.getMethod("main", String[].class);
			mm.invoke(null, (Object) args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void waitForSim(final IBrowsersimHandler handler) {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				SimUtil.wait(new BrowsersimStarted(handler), 500, TimePeriod.LONG);
				notifyStarted();
			}
		});
		t1.start();
	}

	protected static void notifyStarted() {
		System.out.println("BS started.");
		started = true;
	}

	public static boolean isStarted() {
		return started;
	}
	
	private static void wait(WaitCondition condition, long testPeriod, TimePeriod timeout) {
		log.debug("Wait Until " + condition.description() + "...");

		long limit;
		if ((Long.MAX_VALUE - System.currentTimeMillis()) / 1000 > timeout.getSeconds()) {
			limit = System.currentTimeMillis() + timeout.getSeconds() * 1000;
		} else {
			limit = Long.MAX_VALUE;
		}

		while (true) {
			if (condition.test()) {
				break;
			}

			if (timeoutExceeded(condition, limit, timeout)) {
				return;
			}

			sleep(testPeriod);
		}

		log.debug("Wait Until " + condition.description() + " finished successfully");
	}
	
	private static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException("Sleep interrupted", e);
		}
	}
	
	private static boolean timeoutExceeded(WaitCondition condition, long limit, TimePeriod timeout) {
		if (System.currentTimeMillis() > limit) {
			log.debug("Wait Until " + condition.description() + " failed, an exception will be thrown");
			throw new WaitTimeoutExpiredException(
					"Timeout after: " + timeout.getSeconds() + " s.: " + condition.description());
		}
		return false;
	}

}