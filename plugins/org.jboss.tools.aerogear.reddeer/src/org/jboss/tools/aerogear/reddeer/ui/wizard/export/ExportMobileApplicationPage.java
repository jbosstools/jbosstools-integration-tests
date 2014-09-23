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
package org.jboss.tools.aerogear.reddeer.ui.wizard.export;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.tools.aerogear.reddeer.ui.preferences.AndroidPreferencesPage;

/**
 * RedDeer implementation of Export Mobile Application Wizard page
 * @author Vlado Pakan
 *
 */
public class ExportMobileApplicationPage extends WizardPage{
  
  private static final Logger log = Logger.getLogger(AndroidPreferencesPage.class);
  /**
   * Checks/Unchecks platform with platformName
   * @param platformName
   * @param check
   */
  public void setPlatform(String platformName, boolean check){
    log.info("Setting check of platform " + platformName + " to " + check);
    findPlatform(platformName).setChecked(check);
  }
  /**
   * Checks/Unchecks project with projectName
   * @param projectName
   * @param check
   */
  public void setProject(String projectName, boolean check){
    log.info("Setting check of project " + projectName + " to " + check);
    findProject(projectName).setChecked(check);
  }
  /**
   * Set output directory to specified path
   * @param path
   */
  public void setOutputDirectory(String path){
    log.info("Setting output directory to " + path);
    findOutputDirectory().setText(path);
  }
  /**
   * Returns true when platform with platformName is checked
   * @param platformName
   * @return
   */
  public boolean isPlatform(String platformName){
    return findPlatform(platformName).isChecked();
  }
  /**
   * Returns true when project with projectName is checked
   * @param projectName
   * @return
   */
  public boolean isProject(String projectName){
    return findProject(projectName).isChecked();
  }
  /**
   * Returns value of Output Directory combo
   * @return
   */
  public String getOutputDirectory(){
    return findOutputDirectory().getText();
  }
  /**
   * Finds platform tableItem with platformName
   * @param platformName
   * @return
   */
  private TableItem findPlatform (String platformName){
    return new DefaultTableItem(1,platformName);    
  }
  /**
   * Finds project tableItem with projectName
   * @param projectName
   * @return
   */
  private TableItem findProject (String projectName){
    return new DefaultTableItem(0,projectName);    
  }
  /**
   * Finds Output Directory Combo
   * @return
   */
  private Combo findOutputDirectory (){
    return new LabeledCombo("Directory:");
  }
}
