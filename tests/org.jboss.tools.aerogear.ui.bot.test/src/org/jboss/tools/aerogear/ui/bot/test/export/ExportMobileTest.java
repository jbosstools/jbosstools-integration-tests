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
package org.jboss.tools.aerogear.ui.bot.test.export;

import java.io.File;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.tools.aerogear.reddeer.ui.preferences.AndroidPreferencesPage;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
/**
 * Common ancestor for Aerogear export test 
 * @author Vlado Pakan
 *
 */
@Require(clearWorkspace = true)
public class ExportMobileTest extends AerogearBotTest {
  
  @Override
  public void setUp() {
    
    String androidSDKLocation = System.getProperty("android.sdk.location","");
    if (androidSDKLocation == null || androidSDKLocation.length() == 0){
      androidSDKLocation = System.getProperty("user.home","") + File.separator + "android-sdks";
    }
    if (!new File(androidSDKLocation).exists()){
      throw new RuntimeException("Android SDK Location is not set properly: " + androidSDKLocation);
    }
    else{
      WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
      AndroidPreferencesPage androidPreferencesPage = new AndroidPreferencesPage();
      preferenceDialog.open();
      preferenceDialog.select(androidPreferencesPage);
      androidPreferencesPage.setAndroidSDKLocation(androidSDKLocation);
      preferenceDialog.ok();
    }

    super.setUp();
    
  }
  /**
   * Returns output directory and cleans output directory from files which are going
   * to be exported and already exists in output directory
   * @param exportedFileName
   * @param isDirectory
   * @return
   */
  protected String prepareOutputDirectory(String exportedFileName , boolean isDirectory){
    String outputDirectory;
    
    outputDirectory = System.getProperty("java.io.tmpdir","");
    if (outputDirectory == null || outputDirectory.length() == 0){
      outputDirectory = System.getProperty("user.home","");
      if (outputDirectory == null || outputDirectory.length() == 0){
        outputDirectory = new File(".").getAbsolutePath();
      }
    }
    // delete possible leftovers from previous test runs
    cleanOutputDirectory(exportedFileName, outputDirectory , isDirectory);
    return outputDirectory;
    
  }
  /**
   * Cleans outputDirectory from file fileName
   * Proceed recursively whe isDirectory is true
   * @param fileName
   * @param outputDirectory
   * @param isDirectory
   */
  protected void cleanOutputDirectory (String fileName, String outputDirectory , boolean isDirectory){
    File output = new File (outputDirectory + File.separator + fileName);
    if (output.exists()){
      if (isDirectory && output.isDirectory()){
        deleteDirectory(output);
      }
      else if (!isDirectory && !output.isDirectory()){
        output.delete();
      }
    }
    
  }
  
  protected void assertExportedFileExists (String fileName , String path , boolean isDirectory){
    final String fullExportFilePath = path + File.separator + fileName;
    File checkFile = new File (fullExportFilePath);
    assertTrue("Expected " + (isDirectory ? "directory " : "file ") + fullExportFilePath + " was not exported properly",
      (checkFile.exists()) && (checkFile.isDirectory() == isDirectory)); 
    
  }

  private boolean deleteDirectory(File file){
    if (file.isDirectory()){ 
      String[] children = file.list(); 
      for (int i=0; i<children.length; i++){ 
        boolean success = deleteDirectory(new File(file, children[i])); 
        if (!success){  
          return false; 
        } 
      } 
    }
    return file.delete(); 
    
  }
  
}
