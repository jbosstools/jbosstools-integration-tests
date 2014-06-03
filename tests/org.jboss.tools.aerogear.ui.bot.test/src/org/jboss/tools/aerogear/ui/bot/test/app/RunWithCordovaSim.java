/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.app;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.vpe.ui.bot.test.tools.BrowserSimHandler;
import org.junit.Test;

@Require(clearWorkspace = true, perspective = "JBoss")
public class RunWithCordovaSim extends AerogearBotTest {
	@Test
	public void canRunWithCordovaSim() {
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
		final String cordovaSimProcessName = "CordovaSimRunner";
		int countBrowserSimmProcesses = SWTUtilExt
				.countJavaProcess(cordovaSimProcessName);
		// this also asserts that CordovaSim runs without error within JBT
		runTreeItemWithCordovaSim(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
		assertTrue("No new CordovaSimm process was started",
				countBrowserSimmProcesses + 1 == SWTUtilExt
						.countJavaProcess(cordovaSimProcessName));
		BrowserSimHandler.closeAllRunningInstances();
	}
}
