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

package org.jboss.tools.ui.bot.ext.helper;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Matcher;

/**
 * Helper to find context menu of widget
 * @author Vladimir Pakan
 *
 */
public class ContextMenuHelper {

  /**
   * Clicks the context menu matching the text.
   *
   * @param bot bot containing context menu.
   * @param text the text on the context menu.
   * @param hideAfterwards hide menu when method is finished.
   * @throws WidgetNotFoundException if the widget is not found.
   */
  public static MenuItem getContextMenu(final AbstractSWTBot<?> bot,
      final String text, final boolean hideAfterwards) {
    final MenuItem menuItem = UIThreadRunnable
        .syncExec(new WidgetResult<MenuItem>() {
          @SuppressWarnings("unchecked")
		public MenuItem run() {
            MenuItem menuItem = null;
            Menu menu = getWidgetMenu(bot.widget);
            Matcher<?> matcher = allOf(instanceOf(MenuItem.class),withMnemonic(text));
            menuItem = show(menu, matcher, hideAfterwards);
            if (menuItem != null) {
              menu = menuItem.getMenu();
            } else {
              hide(menu);
            }
            return menuItem;
          }
        });
    if (menuItem == null) {
      throw new WidgetNotFoundException("Could not find menu: " + text);
    }
    else{
      if (hideAfterwards){
        // hide
        UIThreadRunnable.syncExec(new VoidResult() {
          public void run() {
            hide(menuItem.getParent());
          }
        });
      }

      return menuItem;
    }  
  }
  /**
   * Simulate Show event to menu and returns MenuItem matching to matcher
   * @param menu
   * @param matcher
   * @param hideAfterwards
   * @return
   */
  private static MenuItem show(final Menu menu, final Matcher<?> matcher, final boolean hideAfterwards) {
    if (menu != null) {
      menu.notifyListeners(SWT.Show, new Event());
      MenuItem[] items = menu.getItems();
      for (final MenuItem menuItem : items) {
        if (matcher.matches(menuItem)) {
          return menuItem;
        }
      }
      if (hideAfterwards){
        menu.notifyListeners(SWT.Hide, new Event());
      }
    }
    return null;
  }
  /**
   * Recursively hide menus
   * @param menu bottom menu to start hiding from
   */
  private static void hide(final Menu menu) {
    menu.notifyListeners(SWT.Hide, new Event());
    if (menu.getParentMenu() != null) {
      hide(menu.getParentMenu());
    }
  }
  /**
   * Returns menu of input widget
   * @param widget
   * @return
   */
  private static Menu getWidgetMenu (Widget widget){
    
    Menu result = null;
    
    if (widget instanceof MenuItem){
      result = ((MenuItem)widget).getMenu();
    }
    else{
      result = ((Control)widget).getMenu();
    }
    
    return result;
    
  }
  /**
   * Executes proper steps to be able call getContextMenu on input treeItem
   * @param tree
   * @param treeItem
   */
  public static void prepareTreeItemForContextMenu(SWTBotTree tree , SWTBotTreeItem treeItem){
    tree.setFocus();
    treeItem.select();
    treeItem.click();
  }
  /**
   * Executes proper steps to be able call getContextMenu on first Tree Item within tree
   * @param tree
   */
  public static void prepareTreeItemForContextMenu(SWTBotTree tree){
    
    tree.setFocus();
    if (tree.getAllItems().length > 0){
      tree.select(0);
    }
    
  }
  /**
   * Executes proper steps to be able call getContextMenu on Tree Item within tree on specified position
   * @param tree
   * @param index - zero based index of Tree Item to be selected
   */
  public static void prepareTreeItemForContextMenu(SWTBotTree tree, int index){
    
    tree.setFocus();
    SWTBotTreeItem[] treeItems = tree.getAllItems(); 
    if (treeItems.length > 0){
      ContextMenuHelper.prepareTreeItemForContextMenu(tree,treeItems[index]);
    }
    
  }
} 
