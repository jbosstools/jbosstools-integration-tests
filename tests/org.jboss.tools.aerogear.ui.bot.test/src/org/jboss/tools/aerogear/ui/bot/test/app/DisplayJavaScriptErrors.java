/******************************************************************************* 
 * Copyright (c) 2014-2017 Red Hat, Inc.
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
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.aerogear.reddeer.cordovasim.CordovaSimLauncher;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Checks displaying of Java Script errors and messages from CordovaSim to
 * Console
 * 
 * @author Vlado Pakan
 * @author Pavol Srna
 *
 */
@CleanWorkspace
public class DisplayJavaScriptErrors extends AerogearBotTest {

	@Before
	public void setUp() {
		createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME, AerogearBotTest.CORDOVA_APP_NAME,
				"org.jboss.example.cordova");

		assertTrue(new ProjectExplorer().containsProject(AerogearBotTest.CORDOVA_PROJECT_NAME));
	}

	@Test
	public void testDisplayingJSErrorsAndMessages() {
		getProjectExplorer().getProject(CORDOVA_PROJECT_NAME).getProjectItem("www", "js", "index.js").open();

		DefaultEditor jsEditor = new DefaultEditor("index.js");
		String jsString = new DefaultStyledText().getText();
		final String logMessage = "LOG_MESSAGE";
		final String errorVariable = "ERROR_VARIABLE";
		jsString = jsString.replaceFirst("app\\.receivedEvent\\('deviceready'\\);",
				"app.receivedEvent(\'deviceready\');" + "\nconsole.log(\"" + logMessage + "\");\n" + errorVariable);
		new DefaultStyledText().setText(jsString);
		jsEditor.save();
		runCordovaSim(CORDOVA_PROJECT_NAME);
		CordovaSimLauncher.stopCordovasim();
		String consoleText = new ConsoleView().getConsoleText();
		String textToContain = "LOG: " + logMessage;
		assertTrue("Console text has to contain:\n" + textToContain + "\nbut is:\n" + consoleText,
				consoleText.contains(textToContain));
		textToContain = "ERROR: ReferenceError: Can't find variable: " + errorVariable;
		assertTrue("Console text has to contain:\n" + textToContain + "\nbut is:\n" + consoleText,
				consoleText.contains(textToContain));
	}
}
