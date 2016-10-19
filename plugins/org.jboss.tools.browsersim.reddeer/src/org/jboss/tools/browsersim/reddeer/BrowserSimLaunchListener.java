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

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener;
public class BrowserSimLaunchListener implements ILaunchesListener{
	
	private ILaunch launch;
	
	public ILaunch getBrowserSimLaunch(){
		return launch;
	}

	@Override
	public void launchesAdded(ILaunch[] arg0) {
		this.launch=arg0[0];
	}

	@Override
	public void launchesChanged(ILaunch[] arg0) {
		
	}

	@Override
	public void launchesRemoved(ILaunch[] arg0) {
		
	}

}
