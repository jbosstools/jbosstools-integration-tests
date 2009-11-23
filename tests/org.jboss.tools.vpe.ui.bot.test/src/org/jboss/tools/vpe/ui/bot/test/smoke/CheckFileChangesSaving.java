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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.SWTJBTBot;

/**
 * Test Saving Changes To File
 * @author Vladimir Pakan
 *
 */
public class CheckFileChangesSaving {
  private static final int sleepTime = 1000;
  /**
   * Insert changeText to file in editor, close file, save file dependent on saveFile input parameter
   * reopen file and check if change was saved or not
   * @param editor
   * @param tree
   * @param fileTreeItem
   * @param changeText
   * @param saveFile
   */
  public static String checkIt (SWTJBTBot bot, SWTBotEclipseEditor editor , SWTBotTree tree, SWTBotTreeItem fileTreeItem, 
    String changeText, boolean saveFile){
    String result = null;
    // Test Saving    
    editor.insertText(changeText);
    bot.sleep(sleepTime);
    bot.menu(IDELabel.Menu.FILE).menu(IDELabel.Menu.CLOSE).click();
    bot.shell(IDELabel.Shell.SAVE_RESOURCE).activate();
    bot.button(saveFile ? IDELabel.Button.YES : IDELabel.Button.NO).click();
    bot.sleep(sleepTime);
    // Reopen Test File
    ContextMenuHelper.prepareTreeItemForContextMenu(tree , fileTreeItem);
    
    new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.OPEN, true)).click();
    bot.sleep(sleepTime);
    
    if (saveFile && !bot.editorByTitle(fileTreeItem.getText()).toTextEditor().getText().startsWith(changeText)){
      result = fileTreeItem.getText() + " was not saved properly.";
    }
    else if (!saveFile && bot.editorByTitle(fileTreeItem.getText()).toTextEditor().getText().startsWith(changeText)){
      result = fileTreeItem.getText()  + " was saved even it should not be.";      
    }
    
    return result;
 
  }
}
