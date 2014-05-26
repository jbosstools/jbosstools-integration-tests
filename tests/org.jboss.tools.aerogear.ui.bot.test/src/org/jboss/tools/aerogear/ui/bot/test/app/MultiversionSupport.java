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
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.aerogear.reddeer.ui.config.ConfigEditor;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

/**
 * Checks Multi versions Engine support
 * 
 * @author Vlado Pakan
 * 
 */
@Require(clearWorkspace = true)
public class MultiversionSupport extends AerogearBotTest {
	@Test
	public void testMultiversionSupport() {
		// Update index.js to display cordova version to console
		projectExplorer.openFile(CORDOVA_PROJECT_NAME, "www", "js", "index.js");
		SWTBotEclipseEditor jsEditor = bot.editorByTitle("index.js")
				.toTextEditor();
		String jsString = jsEditor.getText();
		final String version_message_prefix = "INFO - Cordova Version Number:";
		jsString = jsString.replaceFirst("app\\.report\\('deviceready'\\);",
				"app.report(\'deviceready\');" + "\nconsole.log(\""
						+ version_message_prefix + "\" + device.cordova );");
		jsEditor.setText(jsString);
		jsEditor.save();
		jsEditor.close();
		projectExplorer.openFile(CORDOVA_PROJECT_NAME, "www", "config.xml");
		// Add device plugin to project
		ConfigEditor configEditor = new ConfigEditor(CORDOVA_APP_NAME);
		configEditor.addPlugin("org.apache.cordova.device");
		console.clearConsole();
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
		Object[] beforeListeners = SWTEclipseExt.getWorkbenchListeners()
				.getListeners();
		runTreeItemWithCordovaSim(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
		SWTEclipseExt.retainFromCurrentWorkbenchListeners(beforeListeners)
				.get(0).postShutdown(PlatformUI.getWorkbench());
		String consoleText = console.getConsoleText();
	}
}
