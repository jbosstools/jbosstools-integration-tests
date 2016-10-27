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
package org.jboss.tools.aerogear.reddeer.cordovasim;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.browsersim.reddeer.BrowserSimController;
import org.jboss.tools.browsersim.reddeer.SimController;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.jboss.tools.cordovasim.rmi.CordovasimUtil;
import org.jboss.tools.cordovasim.rmi.ICordovasimHandler;
import org.osgi.framework.Bundle;

public class CordovaSimController extends SimController{
	
	public static final String CORDOVASIM_API_BUNDLE = "org.jboss.tools.cordovasim.rmi";
	private static final String CORDOVASIM_MAIN_CLASS="org.jboss.tools.cordovasim.rmi.CordovasimUtil";

	public ICordovasimHandler launchCordovaSim(ContextMenu menu) {
		IBrowsersimHandler handler =launchSimWithRMI(getBundles(), CORDOVASIM_MAIN_CLASS, menu, null, CordovasimUtil.CS_HANDLER);
		return handler == null ? null : (ICordovasimHandler) handler;
	}
	
	private List<Bundle> getBundles() {
		Bundle csAPI = Platform.getBundle(CORDOVASIM_API_BUNDLE);
		Bundle bsAPI = Platform.getBundle(BrowserSimController.BROWSERSIM_API_BUNDLE);
		List<Bundle> bundles = new ArrayList<>();
		bundles.add(csAPI);
		bundles.add(bsAPI);
		return bundles;
	}

	public void stopCordovasim() {
		stopSim();
	}

}
