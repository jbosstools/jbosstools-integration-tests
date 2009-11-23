/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.SWTJBTBot;
import org.jboss.tools.ui.bot.test.WidgetVariables;

/**
 * Check Renaming Functionality within WebProjects View
 * Tests if file war properly renamed in WebProjects View
 * and Title of file in Editor was renamed also. 
 * @author Vladimir Pakan
 *
 */
public class CheckRenaming {
  private static final int sleepTime = 1000;
  /**
   * Check File Renaming
   * @param bot
   * @param oldFileName
   * @param newFileName
   * @param treePathItems
   * @return
   */
  public static String checkRenameJSPFile(SWTJBTBot bot , String oldFileName, String newFileName, String... treePathItems){
    
    bot.sleep(sleepTime);    
    SWTBot webProjects = bot.viewByTitle(WidgetVariables.WEB_PROJECTS).bot();
    SWTBotTree tree = webProjects.tree();

    tree.setFocus();
    
    if (treePathItems != null && treePathItems.length > 0){
      SWTBotTreeItem parentTreeItem = tree.getTreeItem(treePathItems[0]);
      parentTreeItem.expand();
      int index = 1;
      while (treePathItems.length > index){
        parentTreeItem = parentTreeItem.getNode(treePathItems[index]);
        parentTreeItem.expand();
        index++;
      }
      // Open File
      ContextMenuHelper.prepareTreeItemForContextMenu(tree , parentTreeItem.getNode(oldFileName));
      new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.OPEN, true)).click();
      bot.sleep(sleepTime); 
      // Rename file
      new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.RENAME, true)).click();
      bot.shell(IDELabel.Shell.RENAME_RESOURCE).activate();
      bot.textWithLabel(IDELabel.RenameResourceDialog.NEW_NAME)
        .setText(newFileName);
      bot.button(IDELabel.Button.OK).click();
      bot.sleep(sleepTime); 
      // Check Results
      // File with Old Name doesn't exists within WebProjects View
      try{
        parentTreeItem.getNode(oldFileName);
        return "File " + oldFileName + " was not renamed to " + newFileName + ".";
      }catch (WidgetNotFoundException wnfe) {
        // do nothing 
      }
      // File with New Name exists within WebProjects View
      try{
        parentTreeItem.getNode(newFileName);
      }catch (WidgetNotFoundException wnfe) {
        return "Renamed File " + newFileName + " was not found."; 
      }
      // Editor Title was renamed
      try{
        bot.editorByTitle(newFileName);
      }catch (WidgetNotFoundException wnfe) {
        return "Editor Title was not changed to " + newFileName + " after renaming."; 
      }
    }
    else{
      return "Unable to find file for renaming.";
    }
    
    return null;
    
  }

}
