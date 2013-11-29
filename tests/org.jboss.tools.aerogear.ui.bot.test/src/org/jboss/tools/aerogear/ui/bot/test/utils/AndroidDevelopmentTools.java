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
package org.jboss.tools.aerogear.ui.bot.test.utils;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
/**
 * Provides methods to magage ADK Tools
 * @author Vlado Pakan
 *
 */
public class AndroidDevelopmentTools {
  /**
   * Kills all running emulators
   * @return
   */
  public static void killRunningEmulators() {
    for(String emulatorName : AndroidDevelopmentTools.getRunningEmulators()){
      AndroidDevelopmentTools.killRunningEmulator(emulatorName);
    }
  }
  /**
   * Kills running emulator specified by name
   * @param name
   * @return
   */
  public static void killRunningEmulator(String name) {
    try {
      Process process = new ProcessBuilder(
        AndroidDevelopmentTools.getADBCommandLocation(), "-s" ,name,"emu","kill").start();
      process.waitFor();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } catch (InterruptedException ie){
      // do nothing
    }
  }
  /**
   * Returns list of running emulators
   * @return
   */
  public static List<String> getRunningEmulators() {
    LinkedList<String> result = new LinkedList<String>();
    try {
      Process process = new ProcessBuilder(
          AndroidDevelopmentTools.getADBCommandLocation(), "devices").start();
      BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      boolean listOfDevicesIsFollowing = false;
      while ((line = br.readLine()) != null) {
        if (line.startsWith("List of devices attached")){
          listOfDevicesIsFollowing = true;
        } else if(listOfDevicesIsFollowing && line.startsWith("emulator-")){
          String[] lineSplit = line.split("\t");
          if (lineSplit[1].equals("device")){
            result.add(lineSplit[0]);
          }
        }
      }
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    return result;
  }
  /**
   * Returns Android location read from preferences
   * @return
   */
  public static String getAndoridSDKLocation(){
    IEclipsePreferences preferences = InstanceScope.INSTANCE
        .getNode("com.android.ide.eclipse.adt");
    
    return preferences.get("com.android.ide.eclipse.adt.sdk",null);
  }
  /**
   * Returns full adb command location
   * @return
   */
  public static String getADBCommandLocation(){
    String androidSDKLocation = AndroidDevelopmentTools.getAndoridSDKLocation();
    assertNotNull("Android SDK Location is not specified",androidSDKLocation);
    return androidSDKLocation + File.separator
           + "platform-tools" + File.separator
           + "adb";
  }

}
