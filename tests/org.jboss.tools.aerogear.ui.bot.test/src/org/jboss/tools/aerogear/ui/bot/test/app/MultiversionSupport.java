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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.tools.aerogear.reddeer.cordovasim.CordovaSimLauncher;
import org.jboss.tools.aerogear.reddeer.thym.ui.config.ConfigEditor;
import org.jboss.tools.aerogear.reddeer.thym.ui.properties.EnginePropertyPage;
import org.jboss.tools.aerogear.reddeer.thym.ui.properties.EnginePropertyPage.Platform;
import org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project.CordovaPluginSelectionPage;
import org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project.CordovaPluginWizard;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.eclipse.swt.widgets.Label;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.condition.WidgetIsFound;
import org.eclipse.reddeer.core.matcher.ClassMatcher;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.label.DefaultLabel;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Before;
import org.junit.Test;


/**
 * Checks Multi versions Engine support
 * 
 * @author Vlado Pakan
 * @author Pavol Srna
 * 
 */
@CleanWorkspace
public class MultiversionSupport extends AerogearBotTest {
	private static final String VERSION_MESSSAGE_PREFIX = "INFO - Cordova Version Number:";

	@Before
	public void setUp() {
		createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME, AerogearBotTest.CORDOVA_APP_NAME,
				"org.jboss.example.cordova");

		assertTrue(new ProjectExplorer().containsProject(AerogearBotTest.CORDOVA_PROJECT_NAME));
	}

	/**
	 * Uses cordova device plugin to get real-time cordova.js version.
	 * 	- runs app, checks selected version
	 *  - changes engine, runs app, checks changed version
	 */
	@Test
	public void testMultiversionSupport() {
		// Update index.js to display cordova version to console
		getProjectExplorer().getProject(CORDOVA_PROJECT_NAME).getProjectItem("www", "js", "index.js").open();
		DefaultEditor jsEditor = new DefaultEditor("index.js");
		String jsString = new DefaultStyledText().getText();
		jsString = jsString.replaceFirst("app\\.receivedEvent\\('deviceready'\\);",
				"app.receivedEvent(\'deviceready\');" + "\nconsole.log(\"" + MultiversionSupport.VERSION_MESSSAGE_PREFIX
						+ "\" + device.cordova );");
		new DefaultStyledText().setText(jsString);
		jsEditor.save();
		jsEditor.close();
		getProjectExplorer().getProject(CORDOVA_PROJECT_NAME).getProjectItem("config.xml").open();
		// Add device plugin to project
		ConfigEditor configEditor = new ConfigEditor(CORDOVA_APP_NAME);
		CordovaPluginWizard pluginWizard = configEditor.getPlatformPropertiesPage().addPlugin();
		CordovaPluginSelectionPage pluginPage = new CordovaPluginSelectionPage(pluginWizard);
		pluginPage.selectPlugin("cordova-plugin-device");
		
		pluginWizard.next();
		new WaitUntil(new WidgetIsFound(Label.class, new WithTextMatcher("cordova-plugin-device")));
		new DefaultLabel("cordova-plugin-device");
		assertTrue("There is no version available for plugin " + "cordova-plugin-device",
			new DefaultCombo().getItems().size() > 0);
		pluginWizard.finish();
		
		runCordovaSim(CORDOVA_PROJECT_NAME);
		CordovaSimLauncher.stopCordovasim();
		ConsoleView console = new ConsoleView();
		console.open();
		String consoleEngineVersion = parseConsoleTextForVersion(console.getConsoleText());
		assertNotNull("Cordova Engine version was not displayed in console", consoleEngineVersion);
		// change mobile engine version for project
		PropertyDialog projectPropertiesDialog = new PropertyDialog(CORDOVA_PROJECT_NAME);
		projectPropertiesDialog.open();
		EnginePropertyPage enginePropertyPage = new EnginePropertyPage(projectPropertiesDialog);
		projectPropertiesDialog.select(enginePropertyPage);
		String propEngineVersion = enginePropertyPage.getVersion(Platform.android);
		assertEquals("Version displayed to console is not equal to version in project properties "
				+ consoleEngineVersion + "!=" + propEngineVersion, propEngineVersion, consoleEngineVersion);
		List<String> versions = enginePropertyPage.getAvailableVersions(Platform.android);
		// if just one version is downloaded download second one
		if (versions.size() == 1) {
			downloadMobileEngine("cordova-android@4.1.1");
		}
		// Check other version
		versions = enginePropertyPage.getAvailableVersions(Platform.android, true);
		versions.remove(propEngineVersion);
		String newVersion = versions.get(0);
		enginePropertyPage.checkVersion(newVersion, Platform.android);
		projectPropertiesDialog.ok();
		
		//Save config.xml
		new ConfigEditor(CORDOVA_APP_NAME).save();
		
		// Run project with new mobile engine version
		console = new ConsoleView();
		console.open();
		console.clearConsole();
		runCordovaSim(CORDOVA_PROJECT_NAME);
		CordovaSimLauncher.stopCordovasim();
		consoleEngineVersion = parseConsoleTextForVersion(new ConsoleView().getConsoleText());
		assertEquals("Expected mobile engine version was " + newVersion + " but actual is " + consoleEngineVersion,
				newVersion, consoleEngineVersion);
	}

	private String parseConsoleTextForVersion(String consoleText) {
		String result = null;
		if (consoleText != null) {
			String[] consoleLines = consoleText.split("[\r\n]+");
			int index = 0;
			while (result == null && index < consoleLines.length) {
				if (consoleLines[index].contains(MultiversionSupport.VERSION_MESSSAGE_PREFIX)) {
					result = consoleLines[index]
							.substring(consoleLines[index].indexOf(MultiversionSupport.VERSION_MESSSAGE_PREFIX)
									+ MultiversionSupport.VERSION_MESSSAGE_PREFIX.length());
				}
				index++;
			}
		}
		return result.trim();
	}
}
