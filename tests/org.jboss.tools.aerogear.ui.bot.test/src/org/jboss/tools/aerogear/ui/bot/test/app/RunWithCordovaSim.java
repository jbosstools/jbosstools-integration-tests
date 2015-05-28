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

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
import org.junit.Test;

@CleanWorkspace
public class RunWithCordovaSim extends AerogearBotTest {
	@Test
	public void canRunWithCordovaSim() {
		new ProjectExplorer().selectProjects(CORDOVA_PROJECT_NAME);
		final String cordovaSimProcessName = "CordovaSimRunner";
		int countBrowserSimmProcesses = countJavaProcess(cordovaSimProcessName);
		// this also asserts that CordovaSim runs without error within JBT
		runTreeItemWithCordovaSim(CORDOVA_PROJECT_NAME);
		assertTrue("No new CordovaSimm process was started",
				countBrowserSimmProcesses + 1 == countJavaProcess(cordovaSimProcessName));
		BrowserSimHandler.closeAllRunningInstances();
	}
}
