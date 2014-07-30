/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ui.bot.ext.condition;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
/**
 * Condition test for Eclipse Editor Text
 * @author vlado pakan
 *
 */
public class EclipseEditorText implements ICondition {

  private String textToCheck;
  private SWTBotEclipseEditor editor;
  private boolean equals;
	
  /**
   * Creates EclipseEditorText condition
   * @param editor
   * @param textToCheck
   * @param equals - when true use equal method for evaluating condition otherwise use contains
   */
	public EclipseEditorText(SWTBotEclipseEditor editor, String textToCheck, boolean equals) {
		this.editor = editor;
		this.textToCheck = textToCheck;
		this.equals= equals; 
	}

	@Override
	public void init(SWTBot bot) {

	}

	@Override
	/**
	 * Compares Eclipse Editor text with textTpCheck using compare method
	 * dependent on equals field
	 */
	public boolean test() throws Exception {
	  boolean result;
		if (equals){
		  result = editor.getText().equals(textToCheck);
		}
		else{
		  result = editor.getText().contains(textToCheck);
		}
		
		return result;
		
	}

	@Override
	public String getFailureMessage() {
		return "EclipseEditorText condition not fulfilled" +
		    "\nEditor text:\n" + editor.getText() +
		    "\nText to check:\n" + textToCheck +
		    "\nCompare method used: " + (equals ? "equals" : "contains");
	}
}
