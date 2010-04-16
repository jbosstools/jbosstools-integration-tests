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
package org.jboss.tools.ui.bot.ext.view;

import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.GeneralConsole;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
/**
 * Manage Console View related tasks
 * @author Vlado Pakan
 *
 */
public class ConsoleView extends SWTBotExt {
	protected IView viewObject;
  protected final SWTOpenExt open;
  public static final int PROBLEMS_DESCRIPTION_COLUMN_INDEX = 0;
  public static final int PROBLEMS_RESOURCE_COLUMN_INDEX = 1;
  public static final int PROBLEMS_PATH_COLUMN_INDEX = 2;
  public static final int PROBLEMS_TYPE_COLUMN_INDEX = 4;
	Logger log = Logger.getLogger(ConsoleView.class);
	public ConsoleView() {
	  viewObject = GeneralConsole.LABEL;
		open = new SWTOpenExt(this);
	}
	
	/**
	 * shows Explorer view
	 */
	public void show() {
		open.viewOpen(viewObject);
	}
  /**
   * Returns actual console text
   * @return
   */
	public String getConsoleText(){
	  
	  show();
	  SWTBot viewBot = viewByTitle(viewObject.getName()).bot();
	  String consoleText = null;
	  try{
	    consoleText = viewBot.styledText().getText();
    }catch (WidgetNotFoundException wnfe){
      consoleText = null; 
    }
	  
	  return consoleText;
	  
	}
  /**
   * Clears console content	
   */
	public void clearConsole(){
	  
    show();
    SWTBot viewBot = viewByTitle(viewObject.getName()).bot();
    try{
      SWTBotButton clearConsole = viewBot.buttonWithTooltip(IDELabel.ConsoleView.BUTTON_CLEAR_CONSOLE_TOOLTIP).click();
      clearConsole.click();
    }catch (WidgetNotFoundException wnfe){
      // Do nothing Clear Console button is not available 
    }
	}
	
	public String getConsoleText (long sleepTime , long timeOut , boolean quitWhenNoChange){
	  
    long estimatedTime = 0;
    SWTBot viewBot = viewByTitle(viewObject.getName()).bot();
    String prevConsoleText = getConsoleText();
    String consoleText = prevConsoleText;
    log.info("Waiting for console text with TimeOut: " + timeOut);
    while ((estimatedTime <= timeOut)
      && (!quitWhenNoChange 
        || prevConsoleText == null
        || prevConsoleText.length() == 0
        || !prevConsoleText.equals(consoleText))){
      prevConsoleText = consoleText;
      viewBot.sleep(sleepTime);
      estimatedTime += sleepTime;
      consoleText = getConsoleText();
    }
    log.info("Waiting for console text finished");
    
    return consoleText;
    
	}
	
}
