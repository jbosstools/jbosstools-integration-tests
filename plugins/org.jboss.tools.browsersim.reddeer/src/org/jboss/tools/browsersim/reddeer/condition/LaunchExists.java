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
package org.jboss.tools.browsersim.reddeer.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.browsersim.reddeer.BrowserSimLaunchListener;

public class LaunchExists extends AbstractWaitCondition{
	
	BrowserSimLaunchListener launchListener;
	
	public LaunchExists(BrowserSimLaunchListener launchListener) {
		this.launchListener = launchListener;
	}

	@Override
	public boolean test() {
		return launchListener.getBrowserSimLaunch() != null && launchListener.getBrowserSimLaunch().getProcesses() != null &&
				launchListener.getBrowserSimLaunch().getProcesses().length > 0;
	}

}
