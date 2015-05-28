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

import static org.junit.Assert.assertTrue;

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
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

import org.jboss.tools.aerogear.reddeer.ui.wizard.NewTHYMProjectWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Base class for SWTBot tests of Aerogear JBoss Tools plugin.
 * 
 * @author sbunciak
 * @author Pavol Srna
 * 
 */
@RunWith(RedDeerSuite.class)
public class AerogearBotTest {
  protected static final String CORDOVA_PROJECT_NAME = "CordovaTestProject";
  protected static final String CORDOVA_APP_NAME = "CordovaTestApp";

  /**
   * Creates a new hybrid mobile project in workspace.
   * 
   * @param projectName
   * @param appName
   * @param appId
   */
  public void createHTMLHybridMobileApplication(String projectName,
      String appName, String appId) {

	NewTHYMProjectWizard w = new NewTHYMProjectWizard();
	w.open();
	new WaitWhile(new JobIsRunning(), TimePeriod.LONG);	
	new LabeledText("Project name:").setText(projectName);
	new LabeledText("Name:").setText(appName);
	new LabeledText("ID:").setText(appId);
	w.next();
    
	DefaultTreeItem tiAndroid =  new DefaultTreeItem("Android");
	tiAndroid.expand();
	if(tiAndroid.getItems().isEmpty()){
		downloadMobileEngine("3.7.1");
		tiAndroid = new DefaultTreeItem("Android");
		tiAndroid.expand();
	}
	tiAndroid.getItems().get(0).setChecked(true);	
	w.finish();
	new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
  }

  public void runTreeItemInAndroidEmulator(String projectName) {
    
    // TODO: Order/content of context many may change
    // TODO: Need to check presence of Android SDK installation
    new DefaultTreeItem(projectName).select();
    new ContextMenu("Run As","2 Run on Android Emulator").select();
    new WaitWhile(new JobIsRunning());
  }

  public void runTreeItemOnAndroidDevice(String projectName) {
    
    // TODO: Order/content of context many may change
    // TODO: Need to check presence of Android SDK installation
    new DefaultTreeItem(projectName).select();
    new ContextMenu("Run As","1 Run on Android Device").select();
    new WaitWhile(new JobIsRunning());
  }

  public void runTreeItemWithCordovaSim(String projectName) {

    new DefaultTreeItem(projectName).select();
    new ContextMenu("Run As","3 Run w/CordovaSim").select();
    new WaitUntil(new ConsoleHasNoChange(TimePeriod.NORMAL));
    new WaitWhile(new JobIsRunning());
  }

  public void openInConfigEditor(String projectName) {
	new ProjectExplorer().selectProjects(projectName);
    new DefaultTreeItem(projectName,"www","config.xml").select();
    new ContextMenu("Open With","Cordova Configuration Editor").select();
    new WaitWhile(new JobIsRunning());
  }

  @Before
  public void setUp() {
    createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME,
        AerogearBotTest.CORDOVA_APP_NAME, "org.jboss.example.cordova");

    assertTrue(new ProjectExplorer().containsProject(AerogearBotTest.CORDOVA_PROJECT_NAME));
  }

  @After
  public void tearDown() {
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
  protected void downloadMobileEngine(String version){
      new PushButton("Download...").click();
      new DefaultShell("Download Hybrid Mobile Engine");
     
      DefaultTreeItem tiAndroid = new DefaultTreeItem("Android");
      tiAndroid.expand();
      tiAndroid.getItem(version).setChecked(true);
      new PushButton("OK").click();
      new WaitWhile(new ShellWithTextIsActive("Download Hybrid Mobile Engine"), TimePeriod.LONG);
      
  }
  
  /**
   * Gets list of running java processes via calling command jps
   * @return
   */
  public List<String> getRunningJavaProcesesNames(){
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
  public int countJavaProcess(String processName){
    List<String> runningJavaProcesses = getRunningJavaProcesesNames();
    List<String> processNameList = new LinkedList<String>();
    processNameList.add(processName);
    runningJavaProcesses.retainAll(processNameList);
    return runningJavaProcesses.size();  
  }
  
}
