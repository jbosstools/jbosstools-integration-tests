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
package org.jboss.tools.browsersim.reddeer;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Platform;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.swt.api.ToolItem;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.browsersim.rmi.BrowsersimUtil;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.osgi.framework.Bundle;

public class BrowserSimLauncher extends SimLauncher{

	public static final String BROWSERSIM_API_BUNDLE = "org.jboss.tools.browsersim.rmi";
	private static final String BROWSERSIM_MAIN_CLASS="org.jboss.tools.browsersim.rmi.BrowsersimUtil";

	public IBrowsersimHandler launchBrowserSim(ContextMenuItem menu) {
		ToolItem item = null;
		if(menu == null){
			item = new DefaultToolItem(new WorkbenchShell(), "Run BrowserSim");
			launchSimWithRMI(getBundles(), BROWSERSIM_MAIN_CLASS, null, item);
		} else {
			launchSimWithRMI(getBundles(), BROWSERSIM_MAIN_CLASS, menu, null);
		}
		BrowserSimIsRunning bsIsRunning = new BrowserSimIsRunning(BrowsersimUtil.BS_HANDLER);
		new WaitUntil(bsIsRunning, TimePeriod.LONG);
		return bsIsRunning.getHandler();
	}

	private List<Bundle> getBundles() {
		Bundle bsAPI = Platform.getBundle(BROWSERSIM_API_BUNDLE);
		Bundle reddeerCommon = Platform.getBundle("org.eclipse.reddeer.common");
		Bundle reddeerCore = Platform.getBundle("org.eclipse.reddeer.core");
		Bundle reddeerSwt = Platform.getBundle("org.eclipse.reddeer.swt");
		List<Bundle> bundles = new ArrayList<>();
		bundles.add(bsAPI);
		bundles.add(reddeerCommon);
		bundles.add(reddeerCore);
		bundles.add(reddeerSwt);
		return bundles;
	}

	public void stopBrowsersim() {
		stopSim();
	}
	
	public class BrowserSimIsRunning extends AbstractWaitCondition {

		private Registry registry;
		private String handlerName;
		private IBrowsersimHandler handler = null;

		public BrowserSimIsRunning(String handlerName) {
			try {
				this.handlerName = handlerName;
				this.registry = LocateRegistry.getRegistry();
			} catch (RemoteException e) {
				throw new BrowserSimException("Unable to get registry", e);
			}
		}

		public boolean test() {
			try {
				handler = (IBrowsersimHandler) registry.lookup(handlerName);
			} catch (AccessException e1) {
				throw new BrowserSimException("access violated", e1);
			} catch (RemoteException e1) {
				throw new BrowserSimException("remote exception", e1);
			} catch (NotBoundException e1) {
				return false;
			}

			try {
				return handler.isStarted();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		public IBrowsersimHandler getHandler() {
			return handler;
		}

		@Override
		public String errorMessageWhile() {
			ConsoleView cw = new ConsoleView();
			cw.open();
			String text = cw.getConsoleText();
			return "Error in console: " + text;
		}
		
		@Override
		public String errorMessageUntil() {
			ConsoleView cw = new ConsoleView();
			cw.open();
			String text = cw.getConsoleText();
			return "Error in console: " + text;
		}

	}

}
