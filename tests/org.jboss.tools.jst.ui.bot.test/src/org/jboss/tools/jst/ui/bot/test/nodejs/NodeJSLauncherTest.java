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
package org.jboss.tools.jst.ui.bot.test.nodejs;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.ExplorerItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for NodeJS Launchers
 * 
 * @author Pavol Srna
 *
 */
@OpenPerspective(DebugPerspective.class)
public class NodeJSLauncherTest extends JSTTestBase {

	private static String TEST_APP_NAME = "testApp";
	private static String IMPORT_PATH = "resources/" + TEST_APP_NAME;

	@BeforeClass
	public static void prepare() {
		/* PE is closed in Debug perspective, open it */
		new ProjectExplorer().open();
		importExistingProject(IMPORT_PATH);
		assertTrue("testApp has not been imported!", new ProjectExplorer().containsProject(TEST_APP_NAME));
		bowerInstall(TEST_APP_NAME, "client");
		npmInstall(TEST_APP_NAME, "client");
		npmInstall(TEST_APP_NAME, "server");
	}

	@AfterClass
	public static void cleanup() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	public void testNodeJSRunAsLauncherAvailable() {
		ExplorerItem serverJS = new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("server", "server.js");
		serverJS.select();
		assertTrue("'Run As -> Node.js Application' not available!", runAsNodeJSAppMenu().isEnabled());
	}

	@Test
	public void testNodeJSAppIsRunning() {
		ExplorerItem serverJS = new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("server", "server.js");
		serverJS.select();
		runAsNodeJSAppMenu().select();

		ConsoleView console = new ConsoleView();
		console.activate();
		new WaitUntil(new ConsoleHasText("Hello From Node.js App"));
		new WaitUntil(new ConsoleHasText("Express server listening on port 3000"));

		assertTrue("Node.js App is not running!", console.getConsoleText().contains("Hello From Node.js App"));
		assertTrue("Node.js App is not running!",
				console.getConsoleText().contains("Express server listening on port 3000"));

		console.terminateConsole();

	}

	@Test
	public void testNodeJSDebugAsLauncherAvailable() {
		assertTrue("'Debug As -> Node.js Application' not available!",
				debugAsNodeJSAppMenu(
						new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("server", "server.js"))
								.isEnabled());
	}

	@Test
	public void testNodeJSAppIsDebugging() {
		debugAsNodeJSAppMenu(new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("server", "server.js"))
				.select();

		ConsoleView console = new ConsoleView();
		console.activate();
		new WaitUntil(new ConsoleHasText("Debugger listening"), TimePeriod.LONG);

		assertTrue("Node.js App is not debugging!", console.getConsoleText().contains("Debugger listening"));

		console.terminateConsole();
	}

}
