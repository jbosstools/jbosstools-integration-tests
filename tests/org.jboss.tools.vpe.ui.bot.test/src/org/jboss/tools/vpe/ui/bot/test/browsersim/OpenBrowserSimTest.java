/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.browsersim;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;
/**
 * Test opening of Browsersim within JBoss Perspective
 * @author Vladimir Pakan
 *
 */
public class OpenBrowserSimTest extends JBTSWTBotTestCase{
  /**
   * Opens and closes BrowserSim
   */
	public void testOpenBrowserSim(){
	  if (!bot.activePerspective().getLabel().equals(IDELabel.SelectPerspectiveDialog.JBOSS)){
	    bot.perspectiveByLabel(IDELabel.SelectPerspectiveDialog.JBOSS).activate();
	  }
	  final String browserSimmProcessName = "BrowserSimRunner";
	  int countBrowserSimmProcesses = OpenBrowserSimTest.countJavaProcess(browserSimmProcessName);
	  // this also asserts that BrowserSim runs without error within JBT
		bot.toolbarButtonWithTooltip(IDELabel.ToolbarButton.RUN_BROWSER_SIM).click();
		assertTrue("No new BrowserSim process was started",countBrowserSimmProcesses + 1 == OpenBrowserSimTest.countJavaProcess(browserSimmProcessName));
		// currently there is no way how to close BrowserSim within running JBT
		// BrowserSim is automatically closed when JBT are
	}
  @Override
  protected void activePerspective() {
    // do nothing here it's not working 
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
    List<String> runningJavaProcesses = OpenBrowserSimTest.getRunningJavaProcesesNames();
    List<String> processNameList = new LinkedList<String>();
    processNameList.add(processName);
    runningJavaProcesses.retainAll(processNameList);
    return runningJavaProcesses.size();  
  }
	
}