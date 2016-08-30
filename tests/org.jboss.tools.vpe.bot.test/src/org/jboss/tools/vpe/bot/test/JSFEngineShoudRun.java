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
package org.jboss.tools.vpe.bot.test;

import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.junit.execution.TestMethodShouldRun;
import org.junit.runners.model.FrameworkMethod;

public class JSFEngineShoudRun implements TestMethodShouldRun{

	@Override
	public boolean shouldRun(FrameworkMethod method) {
		String gtkVersion = System.getProperty("org.eclipse.swt.internal.gtk.version");
		Boolean xulRunner = Boolean.parseBoolean(System.getProperty("org.jboss.tools.vpe.loadxulrunner"));
		Boolean isLinux = RunningPlatform.isLinux();
		
		return isLinux && !gtkVersion.startsWith("3") && !xulRunner;
	}

}
