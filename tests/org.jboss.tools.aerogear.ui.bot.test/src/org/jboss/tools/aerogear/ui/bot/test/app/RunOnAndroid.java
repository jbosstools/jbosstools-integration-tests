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

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.aerogear.reddeer.ui.preferences.AndroidPreferencesPage;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.aerogear.ui.bot.test.utils.AndroidDevelopmentTools;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@CleanWorkspace
public class RunOnAndroid extends AerogearBotTest {
	@BeforeClass
	public static void setAndroidSDKPreference() {

		WorkbenchPreferenceDialog preferencesDialog = new WorkbenchPreferenceDialog();
		preferencesDialog.open();
		AndroidPreferencesPage androidPreferences = new AndroidPreferencesPage();
		preferencesDialog.select(androidPreferences);
		androidPreferences.setAndroidSDKLocation(AndroidDevelopmentTools.getAndoridSDKLocation());
		preferencesDialog.ok();

	}

	@Before
	public void setUp() {
		createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME, AerogearBotTest.CORDOVA_APP_NAME,
				"org.jboss.example.cordova", "cordova-android@4.1.0");

		assertTrue(new ProjectExplorer().containsProject(AerogearBotTest.CORDOVA_PROJECT_NAME));
	}

	@Test
	public void canRunOnAndroidEmulator() {
		new ProjectExplorer().selectProjects(CORDOVA_PROJECT_NAME);
		DefaultShell activeShell = new DefaultShell();
		runTreeItemInAndroidEmulator(CORDOVA_PROJECT_NAME);
		activeShell.setFocus();
		assertTrue("Not exactly one instance of Android Emulator is running",
				AndroidDevelopmentTools.getRunningEmulators().size() == 1);
		// bot.sleep(2*TIME_60S);
		// Second run should not start new Emulator instance
		runTreeItemInAndroidEmulator(CORDOVA_PROJECT_NAME);
		activeShell.setFocus();
		assertTrue("Not exactly one instance of Android Emulator is running",
				AndroidDevelopmentTools.getRunningEmulators().size() == 1);
	}

	@Test
	public void testChangesDeployment() {
		new ProjectExplorer().getProject(CORDOVA_PROJECT_NAME).getProjectItem("www", "js", "index.js").open();
		DefaultEditor jsEditor = new DefaultEditor("index.js");
		String editorText = new DefaultStyledText().getText();
		final String firstVersion = "FIRST_VERSION_OF_JS_XYZCVDF1";
		editorText = editorText.replace("app.report('deviceready');",
				"app.report('deviceready');\nconsole.log('" + firstVersion + "');");
		new DefaultStyledText().setText(editorText);
		jsEditor.save();
		new ProjectExplorer().selectProjects(CORDOVA_PROJECT_NAME);
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		consoleView.clearConsole();
		DefaultShell activeShell = new DefaultShell();
		setLogCatFilterPropsAndRun(AerogearBotTest.CORDOVA_PROJECT_NAME);
		activeShell.setFocus();
		// because Emulator instability try to run application one more time
		if (!consoleView.getConsoleText().contains(firstVersion)) {
			setLogCatFilterPropsAndRun(AerogearBotTest.CORDOVA_PROJECT_NAME);
			activeShell.setFocus();
			assertTrue("Cordova app was not successfully deployed to Android Emulator",
					consoleView.getConsoleText().contains(firstVersion));
		}
		final String secondVersion = "SECOND_VERSION_OF_JS_XYZCVDF1";
		new DefaultStyledText().setText(editorText.replace(firstVersion, secondVersion));
		jsEditor.save();
		consoleView.clearConsole();
		// Second run with updated js file
		setLogCatFilterPropsAndRun(AerogearBotTest.CORDOVA_PROJECT_NAME);
		activeShell.setFocus();
		assertTrue("Updated Cordova app was not successfully deployed to Android Emulator",
				consoleView.getConsoleText().contains(secondVersion));
	}

	@Test
	@Ignore
	public void canRunOnAndroidDevice() {
		new ProjectExplorer().selectProjects(CORDOVA_PROJECT_NAME);
		runTreeItemOnAndroidDevice(CORDOVA_PROJECT_NAME);
	}

	@Override
	public void tearDown() {
		try {
			new DefaultEditor(CORDOVA_APP_NAME).close();
		} catch (CoreLayerException e) {
			// do nothing
		}
		try {
			new DefaultEditor("index.js").close();
		} catch (CoreLayerException e) {
			// do nothing
		}
		try {
			new DefaultEditor("config.xml").close();
		} catch (CoreLayerException e) {
			// do nothing
		}
		AndroidDevelopmentTools.killRunningEmulators();
		super.tearDown();
	}
}
