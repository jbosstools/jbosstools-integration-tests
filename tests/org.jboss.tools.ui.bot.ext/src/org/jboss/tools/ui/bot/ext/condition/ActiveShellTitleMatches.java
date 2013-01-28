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
package org.jboss.tools.ui.bot.ext.condition;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTBotExt;

/**
 * Checks if active shell title matches pattern 
 * 
 * @author vlado pakan
 *
 */
public class ActiveShellTitleMatches implements ICondition {

	private String pattern = null;
	private SWTBotExt bot = null;
	
	public ActiveShellTitleMatches(SWTBotExt bot, String pattern) {
		this.pattern = pattern;
		this.bot = bot;
	}
	
	@Override
	public boolean test() throws Exception {
	  boolean result = false;
		String activeShellTitle = getActiveShellTitle();
		if (activeShellTitle != null){
		  result = activeShellTitle.matches(pattern);
		}
		return result;
	}

	@Override
	public void init(SWTBot bot) {
		// do nothing here
	}

	@Override
	public String getFailureMessage() {
		return "Active shell title '" + getActiveShellTitle() + "' doesn't match pattern '" + pattern + "'";
	}
	/**
	 * Returns active shell title
	 * @return
	 */
	private String getActiveShellTitle(){
	  String result = null;
	  SWTBotShell activeShell = bot.activeShell();
    if (activeShell != null){
      result = activeShell.getText();
    }
	  return result;
	}

}
