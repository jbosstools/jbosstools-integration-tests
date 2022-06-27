/******************************************************************************* 
 * Copyright (c) 2020 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.testng.ui.bot.test;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.direct.preferences.Preferences;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasText;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Oleksii Korniienko olkornii@redhat.com
 *
 */
@RunWith(RedDeerSuite.class)
public class TestNGTest extends AbstractTestNGTest {

	private static String testNG_file_name = "NewTest.java";
	private static String PROJECT_NAME = "testng_project";

	@BeforeClass
	public static void createNewProject() {
		Preferences.set("org.eclipse.debug.ui", "Console.limitConsoleOutput", "false");
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WorkbenchShell().setFocus();

		createProject(PROJECT_NAME);
		checkProblemsView();
	}

	@Test
	public void runAndCheckTestNG() {

		new WorkbenchShell().setFocus();
		new ProjectExplorer().selectProjects(PROJECT_NAME);

		new ContextMenuItem("TestNG", "Create TestNG class").select();
		new FinishButton().click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		addLibraries(PROJECT_NAME);

		new TextEditor(testNG_file_name);
		new ShellMenuItem("Run", "Run As", "1 TestNG Test").select();

		ConsoleView consoleView = new ConsoleView();
		new WaitUntil(new ConsoleHasText(consoleView, "Total tests run: 1, Passes: 1, Failures: 0, Skips: 0"),
				TimePeriod.LONG);
	}
}
