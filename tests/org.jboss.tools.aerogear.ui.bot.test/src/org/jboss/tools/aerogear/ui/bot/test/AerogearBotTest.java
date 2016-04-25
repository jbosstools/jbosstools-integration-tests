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

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

import org.jboss.tools.aerogear.reddeer.ui.wizard.NewTHYMProjectWizard;
import org.junit.After;


/**
 * Base class for SWTBot tests of Aerogear JBoss Tools plugin.
 * 
 * @author sbunciak
 * @author Pavol Srna
 * 
 */
public class AerogearBotTest {
  protected static final String CORDOVA_PROJECT_NAME = "CordovaTestProject";
  protected static final String CORDOVA_APP_NAME = "CordovaTestApp";
  protected static String WS_PATH = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
  
  
  /**
   * Creates a new hybrid mobile project in workspace.
   * 
   * @param projectName
   * @param appName
   * @param appId
   * @param engine - cordova-platform@version e.g. ("cordova-android@4.1.1", "cordova-ios@3.9.0" ..)
   */
  public void createHTMLHybridMobileApplication(String projectName,
      String appName, String appId, String engine) {

	NewTHYMProjectWizard w = new NewTHYMProjectWizard();
	w.open();
	new WaitWhile(new JobIsRunning(), TimePeriod.LONG);	
	new LabeledText("Project name:").setText(projectName);
	new LabeledText("Name:").setText(appName);
	new LabeledText("ID:").setText(appId);
	w.next();
    
	String platform = engine.split("@")[0];
	
	if(platform.contains("android")){
		DefaultTreeItem tiAndroid =  new DefaultTreeItem("Android");
		tiAndroid.expand();
		try{
			tiAndroid.getItem(engine).setChecked(true);
		}catch(CoreLayerException e){
			//no item, need to download
			downloadMobileEngine(engine); //download required version
			tiAndroid = new DefaultTreeItem("Android");
			tiAndroid.expand();
			//select downloaded
			tiAndroid.getItem(engine).setChecked(true);
		}
	}else if(engine.contains("ios")){
		DefaultTreeItem tiIOS =  new DefaultTreeItem("iOS (XCode)");
		tiIOS.expand();
		try{
			tiIOS.getItem(engine).setChecked(true);
		}catch(CoreLayerException e){
			//no item, need to download
			downloadMobileEngine(engine); //download required version
			tiIOS = new DefaultTreeItem("iOS (XCode)");
			tiIOS.expand();
			//select downloaded
			tiIOS.getItem(engine).setChecked(true);
		}
	}
	w.finish();
	new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
  }

  public void runTreeItemInAndroidEmulator(String projectName) {
    
    // TODO: Order/content of context many may change
    // TODO: Need to check presence of Android SDK installation
    new DefaultTreeItem(projectName).select();
    new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Run on Android Emulator)")).select();
    new WaitWhile(new JobIsRunning());
  }

  public void runTreeItemOnAndroidDevice(String projectName) {
    
    // TODO: Order/content of context many may change
    // TODO: Need to check presence of Android SDK installation
    new DefaultTreeItem(projectName).select();
    new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Run on Android Device)")).select();
    new WaitWhile(new JobIsRunning());
  }

  public void runTreeItemWithCordovaSim(String projectName) {

    new DefaultTreeItem(projectName).select();
    new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Run w/CordovaSim)")).select();
    new WaitWhile(new JobIsRunning());
    new WaitUntil(new ConsoleHasNoChange(TimePeriod.LONG), TimePeriod.VERY_LONG);
  }

  /**
   * Opens config.xml in Cordova Configuration Editor
   * @param workspace related path to config.xml file
   */
  public void openInConfigEditor(String... path) {
	new ProjectExplorer().selectProjects(path[0]);
    new DefaultTreeItem(path).select();
    new ContextMenu("Open With","Cordova Configuration Editor").select();
    new WaitWhile(new JobIsRunning());
  }

  /**
  @Before
  public void setUp() {
    createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME,
        AerogearBotTest.CORDOVA_APP_NAME, "org.jboss.example.cordova", "cordova-android@4.1.0");

    assertTrue(new ProjectExplorer().containsProject(AerogearBotTest.CORDOVA_PROJECT_NAME));
  }
  */
  @After
  public void tearDown() {
	new WorkbenchShell().setFocus();
	new ProjectExplorer().deleteAllProjects();
	ShellHandler.getInstance().closeAllNonWorbenchShells();
  }
  /**
   * Sets LogCat Filter properties for projoectName via Run Configurations
   * Currently just adds displaying debug messages to console
   * and runs project on Android Emulator  
   * @param projectName
   */
  public void setLogCatFilterPropsAndRun(String projectName){
    new ShellMenu("Run", "run Configurations...").select();
    new DefaultShell("Run Configurations");
    
    DefaultTreeItem tiAndroidEmulator = new DefaultTreeItem("Android Emulator");
    
    tiAndroidEmulator.select();
    tiAndroidEmulator.expand();
    try{
    	tiAndroidEmulator.getItem(projectName).select();
    }catch(CoreLayerException e){
    	new DefaultToolItem("New launch configuration").click();
    	new DefaultText("Name:").setText(projectName);
        new DefaultText("Project:").setText(projectName);
    }

    new DefaultCTabItem("Emulator").activate();
    
    DefaultText txFilter = new DefaultText("Log Filter:");
    String filter = txFilter.getText();
    if (!filter.contains("chromium:V")){
      txFilter.setText("chromium:V " + filter);
      new PushButton("Apply").click();
    }
    new PushButton("Run").click();
    new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
  }
  /**
   * Downloads Mobile Engine
   */
  protected void downloadMobileEngine(String engine){
      new PushButton("Download...").click();
      new DefaultShell("Download Hybrid Mobile Engine");
     
      String platform = engine.split("@")[0];
      String version = engine.split("@")[1];
      
      if(platform.contains("android")){
    	  DefaultTreeItem tiAndroid = new DefaultTreeItem("Android");
          tiAndroid.expand();
          tiAndroid.getItem(version).setChecked(true);
      }else if(platform.contains("ios")){
    	  DefaultTreeItem tiIOS = new DefaultTreeItem("iOS (XCode)");
          tiIOS.expand();
          tiIOS.getItem(version).setChecked(true);
      }
      new PushButton("OK").click();
      new WaitWhile(new ShellWithTextIsActive("Download Hybrid Mobile Engine"), TimePeriod.LONG);
      
  }
  
  /**
   * Gets list of running java processes via calling command jps
   * @return
   */
  public static List<String> getRunningJavaProcesesNames(){
    List<String> result = new LinkedList<String>();
    String javaHome = System.getProperty("java.home", "");
    // search for sdk location instead of jre location
    if (javaHome.endsWith(File.separator + "jre")){
      javaHome = javaHome.substring(0,javaHome.length() -4);
    }
    String jpsCommand = "jps";
    if (javaHome.length() > 0) {
      File javaLocation = new File(javaHome);
      if (javaLocation.exists() && javaLocation.isDirectory()) {
        File javaBinLocation = new File(javaLocation, "bin" + File.separator
            + "jps");
        if (javaBinLocation.exists()) {
          jpsCommand = javaBinLocation.getAbsolutePath();
        }
      }
    }
    String line;
    Process p;
    try {
      p = Runtime.getRuntime().exec(jpsCommand);
      BufferedReader input = new BufferedReader(new InputStreamReader(
          p.getInputStream()));
      while ((line = input.readLine()) != null) {
        if(line.length() > 0){
          String[] lineSplit = line.split(" ");
          if (lineSplit.length > 1){
            result.add(lineSplit[1]);  
          }
          else {
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
   * @param processName
   * @return
   */
  public static int countJavaProcess(String processName){
    List<String> runningJavaProcesses = getRunningJavaProcesesNames();
    List<String> processNameList = new LinkedList<String>();
    processNameList.add(processName);
    runningJavaProcesses.retainAll(processNameList);
    return runningJavaProcesses.size();  
  }
  
}
