/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.app;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
import org.junit.Test;
/**
 * Checks displaying of Java Script errors and messages 
 * from CordovaSim to Console
 * @author Vlado Pakan
 *
 */
@Require(clearWorkspace = true)
public class DisplayJavaScriptErrors extends AerogearBotTest {
	@Test
	public void testDisplayingJSErrorsAndMessages() {
	  projectExplorer.openFile(CORDOVA_PROJECT_NAME, "www" , "js", "index.js");
	  SWTBotEclipseEditor jsEditor = bot.editorByTitle("index.js").toTextEditor();
	  String jsString = jsEditor.getText();
	  final String logMessage = "LOG_MESSAGE";
	  final String errorVariable = "ERROR_VARIABLE";
	  jsString = jsString.replaceFirst("app\\.receivedEvent\\('deviceready'\\);", 
	      "app.receivedEvent(\'deviceready\');"
	      + "\nconsole.log(\"" + logMessage + "\");\n"
	      + errorVariable
	      );
	  jsEditor.setText(jsString);
	  jsEditor.save();
	  console.clearConsole();
	  projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
    runTreeItemWithCordovaSim(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
    BrowserSimHandler.closeAllRunningInstances();
    String consoleText = console.getConsoleText();
    String textToContain = "LOG: " + logMessage;
    assertTrue ("Console text has to contain:\n" + textToContain
        + "\nbut is:\n" + consoleText,
      consoleText.contains(textToContain));
    textToContain = "ERROR: ReferenceError: Can't find variable: " + errorVariable;
    assertTrue ("Console text has to contain:\n" + textToContain
        + "\nbut is:\n" + consoleText,
      consoleText.contains(textToContain));
	}
}
