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

import static org.jboss.tools.ui.bot.ext.SWTTestExt.bot;

import java.awt.event.KeyEvent;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.config.requirement.RequirementNotFulfilledException;
import org.jboss.tools.ui.bot.ext.config.requirement.StartServer;
import org.jboss.tools.ui.bot.ext.config.requirement.StopServer;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.GeneralInternalWebBrowser;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.ui.bot.ext.view.GuvnorRepositories;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.junit.Test;
/**
 * Tests Guvnor Repositories
 * @author Vladimir Pakan
 *
 */
public class GuvnorRepositoriesTest extends SWTTestExt{
  private GuvnorRepositories guvnorRepositories = new GuvnorRepositories(); 
  /**
   * Tests Guvnor Repositories
   */
  @Test
  public void testGuvnorRepositories() {
    //startGuvnor();
    addGuvnorRepository();
    deleteGuvnorRepository();
    addGuvnorRepository();
    openGuvnorConsole();
    browseGuvnorRepository();
    //stopGuvnor();
  }

  /**
   * Adds Guvnor Repository
   */
  private void addGuvnorRepository(){
    eclipse.openPerspective(PerspectiveType.GUVNOR_REPOSITORY_EXPLORING);
    SWTBot guvnorRepositoriesBot = guvnorRepositories.show().bot();
    SWTUtilExt.getViewToolbarButtonWithTooltip(
      guvnorRepositories.show(),
      IDELabel.GuvnorRepositories.ADD_GUVNOR_REPOSITORY_TOOLTIP)
        .click();
    eclipse.waitForShell("");
    guvnorRepositoriesBot.activeShell().bot().button(IDELabel.Button.FINISH).click();
    assertTrue("Guvnor repository was not created properly",
      guvnorRepositoriesBot.tree().rowCount() == 1);
  }
  /**
   * Deletes Guvnor Repostiry
   */
  private void deleteGuvnorRepository(){
    SWTBot guvnorRepositoriesBot = guvnorRepositories.show().bot();;
    SWTBotTree guvnorRepositoryTree = guvnorRepositoriesBot.tree();
    guvnorRepositoryTree.select(0);
    SWTUtilExt.getViewToolbarButtonWithTooltip(
      guvnorRepositories.show(),
      IDELabel.GuvnorRepositories.REMOVE_GUVNOR_REPOSITORY_TOOLTIP)
        .click();
    guvnorRepositoriesBot.shell(IDELabel.GuvnorRepositories.REMOVE_GUVNOR_REPOSITORY_DIALOG_TITLE)
      .activate();
    bot.button(IDELabel.Button.OK).click();
    assertTrue("Guvnor repository was not deleted properly",
      guvnorRepositoriesBot.tree().rowCount() == 0);
  }
  /**
   * Opens Guvnor Console
   */
  private void openGuvnorConsole(){
    SWTBot guvnorRepositoriesBot = guvnorRepositories.show().bot();
    SWTBotTree guvnorRepositoryTree = guvnorRepositoriesBot.tree();
    SWTBotTreeItem tiGuvnorRepository = guvnorRepositoryTree.getAllItems()[0];
    ContextMenuHelper.prepareTreeItemForContextMenu(guvnorRepositoryTree, tiGuvnorRepository);
    new SWTBotMenu(ContextMenuHelper.getContextMenu(guvnorRepositoryTree, 
      IDELabel.Menu.OPEN_GUVNOR_CONSOLE, false)).click();
    bot.sleep(Timing.time5S());
    SWTBotBrowserExt browser = bot.browserByTitle(IDELabel.GuvnorConsole.GUVNOR_CONSOLE_TITLE);
    browser.clickOnButtonViaJavaScript(0, bot);
    browser.clickOnButtonViaJavaScript(IDELabel.GuvnorConsole.BUTTON_YES_INSTALL_SAMPLES, bot);
    bot.sleep(Timing.time1S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
    bot.sleep(Timing.time1S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    bot.sleep(Timing.time10S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_ENTER);
  }
  /**
   * Browse Guvnor Repository
   */
  private void browseGuvnorRepository(){
    
    guvnorRepositories.show();

    String fileToOpen = "Underage.brl";
    guvnorRepositories.openFile(Timing.time1S(),IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
      IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM,
      IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM,
      fileToOpen);
    
    assertTrue("File from Guvnor Repository was not opened properly. File " + fileToOpen + " is not opened in editor",
      SWTEclipseExt.existEditorWithLabel(bot,fileToOpen + " (Read only)"));
    
  }
  /**
   * Starts Guvnor AS
   */
  private void startGuvnor(){
    try {
      new StartServer().fulfill();
    } catch (RequirementNotFulfilledException e) {
      throw new RuntimeException(e);
    } 
  }
  /**
   * Stops Guvnor AS
   */
  private void stopGuvnor(){
    try {
      new StopServer().fulfill();
    } catch (RequirementNotFulfilledException e) {
      throw new RuntimeException(e);
    } 
  }
}