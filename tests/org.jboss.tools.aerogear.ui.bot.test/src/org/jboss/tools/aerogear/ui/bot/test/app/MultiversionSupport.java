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

import java.util.List;

import javax.print.attribute.standard.MediaSize.Engineering;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.aerogear.reddeer.ui.config.ConfigEditor;
import org.jboss.tools.aerogear.reddeer.ui.properties.EnginePropertyPage;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
import org.junit.Test;

/**
 * Checks Multi versions Engine support
 * 
 * @author Vlado Pakan
 * 
 */
@Require(clearWorkspace = true)
public class MultiversionSupport extends AerogearBotTest {
	private static final String VERSION_MESSSAGE_PREFIX = "INFO - Cordova Version Number:";
	@Test
	public void testMultiversionSupport() {
		// Update index.js to display cordova version to console
		projectExplorer.openFile(CORDOVA_PROJECT_NAME, "www", "js", "index.js");
		SWTBotEclipseEditor jsEditor = bot.editorByTitle("index.js")
				.toTextEditor();
		String jsString = jsEditor.getText();
		jsString = jsString.replaceFirst("app\\.receivedEvent\\('deviceready'\\);",
				"app.receivedEvent(\'deviceready\');" + "\nconsole.log(\""
						+ MultiversionSupport.VERSION_MESSSAGE_PREFIX + "\" + device.cordova );");
		jsEditor.setText(jsString);
		jsEditor.save();
		jsEditor.close();
		projectExplorer.openFile(CORDOVA_PROJECT_NAME, "www", "config.xml");
		// Add device plugin to project
		ConfigEditor configEditor = new ConfigEditor(CORDOVA_APP_NAME);
		configEditor.addPlugin("org.apache.cordova.device");
		console.clearConsole();
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
		runTreeItemWithCordovaSim(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
		BrowserSimHandler.closeAllRunningInstances();
		String consoleEngineVersion = parseConsoleTextForVersion(console.getConsoleText());
		assertNotNull("Cordova Engine version was not displayed in console", consoleEngineVersion);
		// change mobile engine version for project
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		EnginePropertyPage enginePropertyPage = new EnginePropertyPage(
				packageExplorer.getProject(CORDOVA_PROJECT_NAME));
		enginePropertyPage.open();
		String propEngineVersion = enginePropertyPage.getVersion();
		assertEquals("Version displayed to console is not equal to version in project properties "
				+ consoleEngineVersion + "!=" + propEngineVersion,
			propEngineVersion, consoleEngineVersion);
		List<String> versions = enginePropertyPage.getAvailableVersions();
		// if just one version is downloaded download second one 
		if (versions.size() == 1){
			downloadMobileEngine(2);
		}
		// Check other version
		versions = enginePropertyPage.getAvailableVersions();
		versions.remove(propEngineVersion);
		String newVersion = versions.get(0);
		enginePropertyPage.checkVersion(newVersion);
		new WorkbenchPreferenceDialog().ok();
		// Run project with new mobile engine version
		console.clearConsole();
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
		runTreeItemWithCordovaSim(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
		BrowserSimHandler.closeAllRunningInstances();
		consoleEngineVersion = parseConsoleTextForVersion(console.getConsoleText());
		assertEquals("Expected mobile engine version was " + newVersion 
				+ " but actual is " + consoleEngineVersion, 
			newVersion,consoleEngineVersion);
	}
	
	private String parseConsoleTextForVersion(String consoleText){
		String result = null;
		if (consoleText != null){
			String[] consoleLines = consoleText.split("[\r\n]+");
			int index = 0;
			while (result == null && index < consoleLines.length){
				if (consoleLines[index].contains(MultiversionSupport.VERSION_MESSSAGE_PREFIX)){
					result = consoleLines[index].substring(
						consoleLines[index].indexOf(MultiversionSupport.VERSION_MESSSAGE_PREFIX)
						+ MultiversionSupport.VERSION_MESSSAGE_PREFIX.length());
				}
				index++;
			}
		}
		return result.trim();
	}
}
