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
package org.jboss.tools.browsersim.reddeer;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.swt.api.ToolItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.browsersim.eclipse.launcher.ExternalProcessLauncher;
import org.jboss.tools.browsersim.reddeer.condition.LaunchExists;
import org.jboss.tools.browsersim.reddeer.condition.SimIsRunning;
import org.jboss.tools.browsersim.rmi.BrowsersimUtil;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.osgi.framework.Bundle;

public class SimController {
	
	protected static Registry registry;
	private static final String HAMCREST_BUNDLE = "org.hamcrest.core";
	private static ILaunch simLaunch;
	
	protected void stopRMIRegistry() {
		try {
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void startRMIRegistry() {
		try {
			registry = LocateRegistry.createRegistry(1099);
		} catch (RemoteException ex) {
			try {
				registry = LocateRegistry.getRegistry(1099);
				IBrowsersimHandler bsHandler = (IBrowsersimHandler) registry.lookup(BrowsersimUtil.BS_HANDLER);
				UnicastRemoteObject.unexportObject(bsHandler, true);
				registry.unbind(BrowsersimUtil.BS_HANDLER);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected ILaunchConfigurationWorkingCopy getSimLaunchConfig(ContextMenu menu, ToolItem item) {
		BrowserSimLaunchListener launchListener = new BrowserSimLaunchListener();
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		manager.addLaunchListener(launchListener);
		if (menu != null) {
			menu.select();
		} else {
			item.click();
		}

		new WaitUntil(new LaunchExists(launchListener));
		IProcess[] processes = launchListener.getBrowserSimLaunch().getProcesses();
		try {
			processes[0].terminate();
		} catch (DebugException e1) {
			e1.printStackTrace();
		}
		launchListener.getBrowserSimLaunch().removeProcess(processes[0]);
		try {
			return launchListener.getBrowserSimLaunch().getLaunchConfiguration().getWorkingCopy();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected IBrowsersimHandler launchSimWithRMI(List<Bundle> additionalBundles, String mainClass,
			ContextMenu menu, ToolItem item, String handlerName) {
		startRMIRegistry();
		ILaunchConfigurationWorkingCopy wc = getSimLaunchConfig(menu, null);
		try {
			List<String> s = wc.getAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, new ArrayList<String>());
			Bundle hamcrest = Platform.getBundle(HAMCREST_BUNDLE);
			String hamcrestLocation = FileLocator.getBundleFile(hamcrest).getCanonicalPath();
			List<String> mem = ExternalProcessLauncher.getClassPathMementos(hamcrestLocation);
			for(Bundle additionalBundle: additionalBundles){
				//Bundle bsAPI = Platform.getBundle(BROWSERSIM_API_BUNDLE);
				String aBundleLocation = FileLocator.getBundleFile(additionalBundle).getCanonicalPath();
				if (aBundleLocation.contains("browsersim") && System.getProperty("bsLocal") != null) {
					aBundleLocation = aBundleLocation + System.getProperty("bsLocal");
					// "/target/org.jboss.tools.browsersim.rmi-4.4.2-SNAPSHOT.jar";
				}
				if (aBundleLocation.contains("cordovasim") && System.getProperty("csLocal") != null) {
					aBundleLocation = aBundleLocation + System.getProperty("csLocal");
					// "/target/org.jboss.tools.browsersim.rmi-4.4.2-SNAPSHOT.jar";
				}
				mem.addAll(ExternalProcessLauncher.getClassPathMementos(aBundleLocation));
			}
			s.addAll(mem);
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, s);
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
					mainClass);
			//wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
			//		"org.jboss.tools.browsersim.rmi.BrowsersimUtil");

			simLaunch = wc.launch(ILaunchManager.RUN_MODE, null);
		} catch (CoreException | IOException e) {
			e.printStackTrace();
		}
		try {
			SimIsRunning isRunning = new SimIsRunning(handlerName);
			new WaitUntil(isRunning, TimePeriod.LONG);
			return isRunning.getHandler();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void stopSim() {
		if (simLaunch != null) {
			try {
				simLaunch.getProcesses()[0].terminate();
				simLaunch.terminate();
				simLaunch = null;
			} catch (DebugException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
