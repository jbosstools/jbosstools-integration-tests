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

import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;

/**
 * Provides methods to manage LogCat view
 * @author Vlado Pakan
 *
 */
public class LogCat {
  private final SWTBotExt bot;
  public LogCat (SWTBotExt bot){
    this.bot = bot;
  }
  /**
   * Opens LogCat view
   * @return
   */
  public void open() {
    new SWTOpenExt(this.bot).viewOpen(ActionItem.View.LogCat.LABEL);    
  }
  /**
   * Closes LogCat view
   * @return
   */
  public void close() {
    bot.editorByTitle(ActionItem.View.LogCat.LABEL.getName()).close();    
  }
  /**
   * Closes LogCat view
   * @param stringToContain
   * @return
   */
  public boolean containsString(String stringToContain) {
    open();
    bot.text(0).setText(stringToContain);
    System.out.println(bot.table(1).rowCount());
    return bot.table(1).rowCount() > 0;
  }
  /**
   * Clears log
   * @return
   */
  public void clearLog() {
    open();
    bot.toolbarButtonWithTooltip("Clear Log").click();
  }

}
