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

import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.jst.reddeer.common.CursorPositionIsOnLine;
import org.jboss.tools.jst.reddeer.common.TreeContainsItem;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for NodeJS Debugger
 * 
 * @author Pavol Srna
 *
 */
@OpenPerspective(DebugPerspective.class)
public class NodeJSDebuggerTest extends JSTTestBase {

	private static String TEST_APP_NAME = "testApp";
	private static String IMPORT_PATH = "resources/" + TEST_APP_NAME;

	private static String CONSOLE_LOG = "console.log";

	private static String DEBUG_VARIABLE_EXPRESS = "express";
	private static String DEBUG_VARIABLE_PATH = "path";
	private static String DEBUG_VARIABLE_PORT = "port";

	@BeforeClass
	public static void prepare() {
		try {
			new DefaultEditor("Red Hat Central").close();
		} catch (CoreLayerException e) {
			// if not open, ignore
		}
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

	@Before
	public void debugAs() {

		debugAsNodeJSAppMenu(new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("server", "server.js"))
				.select();

		new WorkbenchView("Debug").open();
		DefaultTree debugTree = new DefaultTree();
		new WaitUntil(
				new TreeContainsItem(debugTree, new RegexMatcher("\\(anonymous function\\)(.*)(server\\.js)(.*)")),
				TimePeriod.LONG);

		RegexMatcher matcher = new RegexMatcher("\\(anonymous function\\)(.*)(server\\.js)(.*)");
		doubleClickTreeItem(debugTree, matcher);

		ConsoleView console = new ConsoleView();
		console.open();
		new WaitUntil(new ConsoleHasText("Debugger listening"), TimePeriod.LONG);
	}

	@After
	public void terminate() {
		ConsoleView console = new ConsoleView();
		console.open();
		console.terminateConsole();
	}

	@Test
	public void testJSVariablesAvailableInView() {

		TreeItem varExpress = getVariable(DEBUG_VARIABLE_EXPRESS);
		TreeItem varPath = getVariable(DEBUG_VARIABLE_PATH);
		TreeItem varPort = getVariable(DEBUG_VARIABLE_PORT);

		assertTrue("Variable '" + DEBUG_VARIABLE_EXPRESS + "' not found in view!", varExpress != null);
		assertTrue(varExpress.getCell(0).equals(DEBUG_VARIABLE_EXPRESS));
		assertTrue(varExpress.getCell(1).equals("undefined"));

		assertTrue("Variable '" + DEBUG_VARIABLE_PATH + "' not found in view!", varPath != null);
		assertTrue(varPath.getCell(0).equals(DEBUG_VARIABLE_PATH));
		assertTrue(varPath.getCell(1).equals("undefined"));

		assertTrue("Variable '" + DEBUG_VARIABLE_PORT + "' not found in view!", varPort != null);
		assertTrue(varPort.getCell(0).equals(DEBUG_VARIABLE_PORT));
		assertTrue(varPort.getCell(1).equals("undefined"));

	}

	@Test
	public void testJSVariablesInitialized() throws CoreException {

		TextEditor editor = new TextEditor("server.js");
		int line = editor.getLineOfText(CONSOLE_LOG) + 1;
		setLineBreakpoint(editor, line);

		// resume
		new WorkbenchView("Debug").open();
		DefaultTree debugTree = new DefaultTree();
		resume(debugTree, new RegexMatcher("\\(anonymous function\\)(.*)(server\\.js)(.*)"));
		new WaitUntil(new CursorPositionIsOnLine(editor, line));

		TreeItem varExpress = getVariable(DEBUG_VARIABLE_EXPRESS);
		TreeItem varPath = getVariable(DEBUG_VARIABLE_PATH);
		TreeItem varPort = getVariable(DEBUG_VARIABLE_PORT);

		assertTrue("Variable '" + DEBUG_VARIABLE_EXPRESS + "' not found in view!", varExpress != null);
		assertTrue(varExpress.getCell(0).equals(DEBUG_VARIABLE_EXPRESS));
		assertTrue(varExpress.getCell(1).contains("[Function]"));

		assertTrue("Variable '" + DEBUG_VARIABLE_PATH + "' not found in view!", varPath != null);
		assertTrue(varPath.getCell(0).equals(DEBUG_VARIABLE_PATH));
		assertTrue(varPath.getCell(1).contains("[Object]"));

		assertTrue("Variable '" + DEBUG_VARIABLE_PORT + "' not found in view!", varPort != null);
		assertTrue(varPort.getCell(0).equals(DEBUG_VARIABLE_PORT));
		assertTrue(varPort.getCell(1).equals("3000"));

	}

	@Test
	public void testDebuggerStopsAtBreakpoint() throws CoreException {

		TextEditor editor = new TextEditor("server.js");
		int line = editor.getLineOfText(CONSOLE_LOG) + 1;
		setLineBreakpoint(editor, line);

		// resume
		new WorkbenchView("Debug").open();
		DefaultTree debugTree = new DefaultTree();
		resume(debugTree, new RegexMatcher("\\(anonymous function\\)(.*)(server\\.js)(.*)"));

		try {
			new WaitUntil(new CursorPositionIsOnLine(editor, line));
		} catch (WaitTimeoutExpiredException e) {
			Assert.fail("Debugger hasn't stopped on breakpoint");
		}
	}

}
