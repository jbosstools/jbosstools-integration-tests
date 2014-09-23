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
package org.jboss.tools.aerogear.reddeer.ui.preferences;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * RedDeer implementation of Hybrid Mobile > Android Preferences page 
 * @author Vlado Pakan
 *
 */
public class AndroidPreferencesPage extends WorkbenchPreferencePage{
  
  public static final String PAGE_NAME = "Android";

  private static final Logger log = Logger.getLogger(AndroidPreferencesPage.class);

  public AndroidPreferencesPage() {
    super("Hybrid Mobile", PAGE_NAME);
  }
  /**
   * Sets Android SDK Location to androidSDKLocation
   * @param androidSDKLocation
   */
  public void setAndroidSDKLocation(String androidSDKLocation){
    log.info("Setting Android SDK Location to: " + androidSDKLocation);
    findAdnroidSDKLocation().setText("");
    KeyboardFactory.getKeyboard().type(androidSDKLocation);
  }
  /**
   * Returns Android SDK Location
   * @return
   */
  public String getAndroidSDKLocation(){
    return findAdnroidSDKLocation().getText();
  }
  
  /**
   * Finds Output Directory Combo
   * @return
   */
  private Text findAdnroidSDKLocation (){
    return new LabeledText("Android SDK Directory:");
  }
}
