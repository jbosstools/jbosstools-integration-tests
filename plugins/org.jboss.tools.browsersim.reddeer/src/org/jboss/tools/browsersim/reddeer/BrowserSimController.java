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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.swt.api.ToolItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.browsersim.rmi.BrowsersimUtil;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.osgi.framework.Bundle;

public class BrowserSimController extends SimController{

	public static final String BROWSERSIM_API_BUNDLE = "org.jboss.tools.browsersim.rmi";
	private static final String BROWSERSIM_MAIN_CLASS="org.jboss.tools.browsersim.rmi.BrowsersimUtil";

	public IBrowsersimHandler launchBrowserSim(ContextMenu menu) {
		IBrowsersimHandler handler = null;
		ToolItem item = null;
		if(menu == null){
			item = new DefaultToolItem(new WorkbenchShell(), "Run BrowserSim");
			handler =launchSimWithRMI(getBundles(), BROWSERSIM_MAIN_CLASS, null, item, BrowsersimUtil.BS_HANDLER);
		} else {
			handler =launchSimWithRMI(getBundles(), BROWSERSIM_MAIN_CLASS, menu, null, BrowsersimUtil.BS_HANDLER);
		}
		return handler;
	}

	private List<Bundle> getBundles() {
		Bundle bsAPI = Platform.getBundle(BROWSERSIM_API_BUNDLE);
		List<Bundle> bundles = new ArrayList<>();
		bundles.add(bsAPI);
		return bundles;
	}

	public void stopBrowsersim() {
		stopSim();
	}

}
