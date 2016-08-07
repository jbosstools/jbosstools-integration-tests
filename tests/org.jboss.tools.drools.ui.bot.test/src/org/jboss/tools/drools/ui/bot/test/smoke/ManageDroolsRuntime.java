 /*******************************************************************************
  * Copyright (c) 2007-2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.drools.ui.bot.test.smoke;

import java.io.File;

import org.drools.eclipse.util.DroolsRuntime;
import org.drools.eclipse.util.DroolsRuntimeManager;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

/**
 * Test managing of Drools Runtime
 * @author Vladimir Pakan
 *
 */
public class ManageDroolsRuntime extends SWTTestExt {

  /**
   * Test manage Drools Runtime
   */
  @Test
  public void testManageDroolsRuntime() {
    addDroolsRuntime(DroolsAllBotTests.DROOLS_RUNTIME_NAME,DroolsAllBotTests.DROOLS_RUNTIME_LOCATION,true);
    editDroolsRuntime(DroolsAllBotTests.getTestDroolsRuntimeName(),
      DroolsAllBotTests.getTestDroolsRuntimeLocation(),
      "edited" , "testedit");
    removeDroolsRuntime(DroolsAllBotTests.getTestDroolsRuntimeName());
    createDroolsRuntime(DroolsAllBotTests.DROOLS_RUNTIME_NAME,DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION);
    if (DroolsAllBotTests.useExternalDroolsRuntime()) {
      removeDroolsRuntime(DroolsAllBotTests.DROOLS_RUNTIME_NAME);
      addDroolsRuntime(DroolsAllBotTests.DROOLS_RUNTIME_NAME,DroolsAllBotTests.DROOLS_RUNTIME_LOCATION,true);
    }
  }

 /**
  * Adds Drools Runtime
  * @param runtimeName
  * @param runtimeLocation
  * @param setAsDefault
  */
  private void addDroolsRuntime(String runtimeName, String runtimeLocation, boolean setAsDefault) {
    selectDroolsPreferences();
    bot.button(IDELabel.Button.ADD).click();
    bot.shell(IDELabel.Shell.DROOLS_RUNTIME).activate();
    bot.textWithLabel(IDELabel.DroolsRuntimeDialog.NAME).setText(runtimeName);
    bot.textWithLabel(IDELabel.DroolsRuntimeDialog.PATH).setText(runtimeLocation);
    bot.button(IDELabel.Button.OK).click();
    bot.shell(IDELabel.Shell.PREFERENCES).activate();
    SWTBotTable table = bot.table();
    boolean droolsRuntimeAdded = 
      SWTEclipseExt.isItemInTableColumn(table,runtimeName,IDELabel.DroolsRuntimeDialog.COLUMN_NAME_INDEX)
      && SWTEclipseExt.isItemInTableColumn(table,runtimeLocation,IDELabel.DroolsRuntimeDialog.COLUMN_LOCATION_INDEX);
    // Set new runtime as default
    if (setAsDefault) {
      table.getTableItem(0).check();
    }
    bot.button(IDELabel.Button.OK).click();
    assertTrue("Drools Runtime with name [" + runtimeName + 
      "] and location [" + runtimeLocation +
      "] was not added properly.",droolsRuntimeAdded);
    DroolsAllBotTests.setTestDroolsRuntimeName(runtimeName);
    DroolsAllBotTests.setTestDroolsRuntimeLocation(runtimeLocation);
    SWTEclipseExt.hideWarningIfDisplayed(bot);
  }

  /**
   * Selects Drools Preferences within Preferences Dialog  
   */
  private void selectDroolsPreferences() {
    jbt.delay();
    new SWTOpenExt(new SWTBotExt()).preferenceOpen(ActionItem.Preference.DroolsInstalledDroolsRuntimes.LABEL);
  }

  /**
   * Edits Drools Runtime
   * @param runtimeName
   * @param runtimeLocation
   * @param nameSuffix
   * @param locationSuffix
   */
   private void editDroolsRuntime(String runtimeName, String runtimeLocation, String nameSuffix, String locationSuffix) {
     selectDroolsPreferences();
     SWTBotTable table = bot.table();
     table.getTableItem(runtimeName).select();
     bot.button(IDELabel.Button.EDIT).click();
     bot.shell(IDELabel.Shell.DROOLS_RUNTIME).activate();
     SWTBotText txName = bot.textWithLabel(IDELabel.DroolsRuntimeDialog.NAME);
     SWTBotText txPath = bot.textWithLabel(IDELabel.DroolsRuntimeDialog.PATH);
     String editedDroolsRuntimeName = txName.getText() + " " + nameSuffix;
     String editedDroolsRuntimeLocation = txPath.getText() + File.separator + nameSuffix;
     new File(editedDroolsRuntimeLocation).mkdir();
     txName.setText(editedDroolsRuntimeName);
     txPath.setText(editedDroolsRuntimeLocation);
     bot.button(IDELabel.Button.OK).click();
     bot.shell(IDELabel.Shell.PREFERENCES).activate();
     boolean droolsRuntimeEdited = 
       SWTEclipseExt.isItemInTableColumn(table,editedDroolsRuntimeName,IDELabel.DroolsRuntimeDialog.COLUMN_NAME_INDEX)
       && SWTEclipseExt.isItemInTableColumn(table,editedDroolsRuntimeLocation,IDELabel.DroolsRuntimeDialog.COLUMN_LOCATION_INDEX);
     bot.button(IDELabel.Button.OK).click();
     assertTrue("Drools Runtime with name [" + runtimeName + 
       "] and location [" + runtimeLocation +
       "] was not renamed properly.",droolsRuntimeEdited);
     DroolsAllBotTests.setTestDroolsRuntimeName(editedDroolsRuntimeName);
     DroolsAllBotTests.setTestDroolsRuntimeLocation(editedDroolsRuntimeLocation);
     SWTEclipseExt.hideWarningIfDisplayed(bot);
   }

  /**
   * Removes Drools Runtime
   * @param runtimeName
   * @param runtimeLocation
   */
   private void removeDroolsRuntime(String runtimeName) {
     selectDroolsPreferences();
     SWTBotTable table = bot.table();
     table.getTableItem(runtimeName).select();
     bot.button(IDELabel.Button.REMOVE).click();
     boolean droolsRuntimeRemoved = !SWTEclipseExt.isItemInTableColumn(table,runtimeName,
       IDELabel.DroolsRuntimeDialog.COLUMN_NAME_INDEX); 
     bot.button(IDELabel.Button.OK).click();
     assertTrue("Drools Runtime with name [" + runtimeName + 
       "] was not removed properly.",droolsRuntimeRemoved);
     // Remove temporary directory created within editDroolsRuntime() method
     if (!DroolsAllBotTests.getTestDroolsRuntimeName().equals(DroolsAllBotTests.DROOLS_RUNTIME_NAME)) {
       File tempDir = new File (DroolsAllBotTests.getTestDroolsRuntimeLocation());
       if (tempDir.isDirectory()) {
         tempDir.delete();  
       }
     }
     DroolsAllBotTests.setTestDroolsRuntimeName(null);
     DroolsAllBotTests.setTestDroolsRuntimeLocation(null);
     SWTEclipseExt.hideWarningIfDisplayed(bot);
   }

   /**
    * Creates Drools Runtime
    * @param runtimeName
    * @param runtimeLocation
    */
    private void createDroolsRuntime(String runtimeName, String runtimeLocation) {
      DroolsRuntimeManager.createDefaultRuntime(runtimeLocation);
      DroolsRuntime droolsRuntime = new DroolsRuntime();
      droolsRuntime.setName(runtimeName);
      droolsRuntime.setPath(runtimeLocation);
      droolsRuntime.setDefault(true);
      DroolsRuntimeManager.setDroolsRuntimes(new DroolsRuntime[]{droolsRuntime});
      // Test if Drools runtime is defined
      assertTrue("Drools Runtime was not properly created on location: " + runtimeLocation,
        new File (runtimeLocation + File.separator + "drools-core.jar").exists());
      selectDroolsPreferences();
      SWTBotTable table = bot.table();
      boolean droolsRuntimeCreated = 
        SWTEclipseExt.isItemInTableColumn(table,runtimeName,IDELabel.DroolsRuntimeDialog.COLUMN_NAME_INDEX)
        && SWTEclipseExt.isItemInTableColumn(table,runtimeLocation,IDELabel.DroolsRuntimeDialog.COLUMN_LOCATION_INDEX);
      bot.button(IDELabel.Button.OK).click();
      SWTEclipseExt.hideWarningIfDisplayed(bot);
      assertTrue("Drools Runtime with name [" + runtimeName + 
        "] and location [" + runtimeLocation +
        "] was not created properly.",droolsRuntimeCreated);
      DroolsAllBotTests.setTestDroolsRuntimeName(runtimeName);
      DroolsAllBotTests.setTestDroolsRuntimeLocation(runtimeLocation);

    }  
}
