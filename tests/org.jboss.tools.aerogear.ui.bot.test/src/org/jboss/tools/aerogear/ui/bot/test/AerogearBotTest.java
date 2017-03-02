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
package org.jboss.tools.aerogear.ui.bot.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.aerogear.reddeer.cordovasim.CordovaSimLauncher;
import org.jboss.tools.aerogear.reddeer.thym.ui.config.ConfigEditor;
import org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project.EngineConfigurationPage;
import org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project.NewHybridProjectWizard;
import org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project.ThymPlatform;
import org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project.WizardNewHybridProjectCreationPage;
import org.jboss.tools.cordovasim.rmi.ICordovasimHandler;
import org.junit.After;
import org.junit.BeforeClass;

/**
 * Base class for SWTBot tests of Aerogear JBoss Tools plugin.
 * 
 * @author sbunciak
 * @author Pavol Srna
 * 
 */
@CleanWorkspace
public class AerogearBotTest {
	protected static final String CORDOVA_PROJECT_NAME = "CordovaTestProject";
	protected static final String CORDOVA_APP_NAME = "CordovaTestApp";
	protected static String WS_PATH = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
	
	@BeforeClass
	public static void prepare(){
		WorkbenchShell ws = new WorkbenchShell();
		if(!ws.isMaximized()){
			ws.maximize();
		}
	}
	
	@After
	public void tearDown() {
		new CleanWorkspaceRequirement().fulfill();
	}
	
	public static void createHTMLHybridMobileApplication(String projectName, String appName, String appId) {
		createHTMLHybridMobileApplication(projectName, appName, appId, ThymPlatform.ANDROID, getLatestCordovaAndroid());
	}

	/**
	 * Creates a new hybrid mobile project in workspace.
	 * 
	 * @param projectName
	 * @param appName
	 * @param appId
	 * @param engine
	 *            - cordova-platform@version e.g. ("cordova-android@4.1.1",
	 *            "cordova-ios@3.9.0" ..)
	 */
	public static void createHTMLHybridMobileApplication(String projectName, String appName, String appId, ThymPlatform engine, 
			String engineVersion) {

		NewHybridProjectWizard w = new NewHybridProjectWizard();
		w.open();
		
		WizardNewHybridProjectCreationPage hpFirstPage = new WizardNewHybridProjectCreationPage();
		hpFirstPage.setProjectName(projectName);
		hpFirstPage.setAppName(appName);
		hpFirstPage.setAppID(appId);
		w.next();
		
		EngineConfigurationPage confPage = new EngineConfigurationPage();
		List<ThymPlatform> availableEngines = confPage.getAvailableEngines();
		assertTrue("Engine '"+engine.getText()+"' is not available",availableEngines.contains(engine));
		List<String> availableVersions = confPage.getAvailableVersions(engine);
		
		if(!availableVersions.contains(engineVersion)){
			//download requested version if not available
			confPage.downloadEngineVersion(engine, engineVersion);
		}
		
		confPage.selectEngine(engine, engineVersion);
		w.finish();
		//check if config editor was opened & close
		new ConfigEditor(appName).close();
	}

	public void runTreeItemInAndroidEmulator(String projectName) {

		// TODO: Order/content of context many may change
		// TODO: Need to check presence of Android SDK installation
		getProjectExplorer().selectProjects(projectName);
		new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Run on Android Emulator)")).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public void runTreeItemOnAndroidDevice(String projectName) {

		// TODO: Order/content of context many may change
		// TODO: Need to check presence of Android SDK installation
		getProjectExplorer().selectProjects(projectName);
		new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Run on Android Device)")).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public ICordovasimHandler runCordovaSim(String projectName) {
		getProjectExplorer().selectProjects(projectName);
		CordovaSimLauncher csLauncher = new CordovaSimLauncher();
		return csLauncher.launchCordovaSim(
				new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Run w/CordovaSim)")));
	}

	/**
	 * Opens config.xml in Cordova Configuration Editor
	 * 
	 * @param workspace
	 *            related path to config.xml file
	 */
	public void openInConfigEditor(String projectName, String appName, String... path) {
		getProjectExplorer().getProject(projectName).getProjectItem(path).open();
		// check if correct editor was opened
		new ConfigEditor(appName);
		new WaitWhile(new JobIsRunning());
	}

	/**
	 * Sets LogCat Filter properties for projoectName via Run Configurations
	 * Currently just adds displaying debug messages to console and runs project
	 * on Android Emulator
	 * 
	 * @param projectName
	 */
	public void setLogCatFilterPropsAndRun(String projectName) {
		new ShellMenu("Run", "run Configurations...").select();
		new DefaultShell("Run Configurations");

		DefaultTreeItem tiAndroidEmulator = new DefaultTreeItem("Android Emulator");

		tiAndroidEmulator.select();
		tiAndroidEmulator.expand();
		try {
			tiAndroidEmulator.getItem(projectName).select();
		} catch (CoreLayerException e) {
			new DefaultToolItem("New launch configuration").click();
			new DefaultText("Name:").setText(projectName);
			new DefaultText("Project:").setText(projectName);
		}

		new DefaultCTabItem("Emulator").activate();

		DefaultText txFilter = new DefaultText("Log Filter:");
		String filter = txFilter.getText();
		if (!filter.contains("chromium:V")) {
			txFilter.setText("chromium:V " + filter);
			new PushButton("Apply").click();
		}
		new PushButton("Run").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	/**
	 * Downloads Mobile Engine
	 */
	protected void downloadMobileEngine(String engine) {
		new PushButton("Download...").click();
		Shell downloadShell = new DefaultShell("Download Hybrid Mobile Engine");

		String platform = engine.split("@")[0];
		String version = engine.split("@")[1];

		if (platform.contains("android")) {
			DefaultTreeItem tiAndroid = new DefaultTreeItem("Android");
			tiAndroid.expand();
			tiAndroid.getItem(version).setChecked(true);
		} else if (platform.contains("ios")) {
			DefaultTreeItem tiIOS = new DefaultTreeItem("iOS (XCode)");
			tiIOS.expand();
			tiIOS.getItem(version).setChecked(true);
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable(downloadShell), TimePeriod.LONG);

	}

	/**
	 * Gets list of running java processes via calling command jps
	 * 
	 * @return
	 */
	public static List<String> getRunningJavaProcesesNames() {
		List<String> result = new LinkedList<String>();
		String javaHome = System.getProperty("java.home", "");
		// search for sdk location instead of jre location
		if (javaHome.endsWith(File.separator + "jre")) {
			javaHome = javaHome.substring(0, javaHome.length() - 4);
		}
		String jpsCommand = "jps";
		if (javaHome.length() > 0) {
			File javaLocation = new File(javaHome);
			if (javaLocation.exists() && javaLocation.isDirectory()) {
				File javaBinLocation = new File(javaLocation, "bin" + File.separator + "jps");
				if (javaBinLocation.exists()) {
					jpsCommand = javaBinLocation.getAbsolutePath();
				}
			}
		}
		String line;
		Process p;
		try {
			p = Runtime.getRuntime().exec(jpsCommand);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (line.length() > 0) {
					String[] lineSplit = line.split(" ");
					if (lineSplit.length > 1) {
						result.add(lineSplit[1]);
					} else {
						result.add("[PID]:" + lineSplit[0]);
					}
				}
			}
			input.close();

		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		return result;
	}

	/**
	 * Counts running java processes with name processName
	 * 
	 * @param processName
	 * @return
	 */
	public static int countJavaProcess(String processName) {
		List<String> runningJavaProcesses = getRunningJavaProcesesNames();
		List<String> processNameList = new LinkedList<String>();
		processNameList.add(processName);
		runningJavaProcesses.retainAll(processNameList);
		return runningJavaProcesses.size();
	}

	public ProjectExplorer getProjectExplorer() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		return pe;
	}
	
	protected static String getLatestCordovaAndroid(){
		return (String)System.getProperty("cordova.android","cordova-android@5.2.2");
	}

}
