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

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.JobName;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.junit.Test;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
/**
 * Test managing of Drools Project
 * @author Vladimir Pakan
 *
 */
public class ManageDroolsProject extends SWTTestExt{
  /**
   * Test manage Drools project
   */
  @Test
  public void testManageDroolsProject() {
    createDroolsProject (DroolsAllBotTests.DROOLS_PROJECT_NAME);
  }
  
  private void createDroolsProject(String droolsProjectName){
    eclipse.showView(ViewType.PACKAGE_EXPLORER);
    eclipse.createNew(EntityType.DROOLS_PROJECT);
    bot.textWithLabel(IDELabel.NewDroolsProjectDialog.NAME).setText(droolsProjectName);
    bot.button(IDELabel.Button.NEXT).click();
    // check all buttons
    int index = 0;
    boolean checkBoxExists = true;
    while (checkBoxExists){
      try{
        SWTBotCheckBox checkBox = bot.checkBox(index);
        if (!checkBox.isChecked()){
          checkBox.click();
        }
        index++;
      }catch (WidgetNotFoundException wnfe){
        checkBoxExists = false;
      }catch (IndexOutOfBoundsException ioobe){
        checkBoxExists = false;
      }
    }
    bot.button(IDELabel.Button.NEXT).click();
    bot.button(IDELabel.Button.FINISH).click();
    SWTTestExt.util.waitForAllExcept(100*1000L, new String[]{JobName.USAGE_DATA_EVENT_CONSUMER});
    SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
    SWTBotTree tree = innerBot.tree();
    boolean isOk = false;
    try {
      tree.getTreeItem(droolsProjectName);
      isOk = true;
    } catch (WidgetNotFoundException e) {
    }
    assertTrue("Project "
      + droolsProjectName 
      + " was not created properly.",isOk);
  }
  
}

