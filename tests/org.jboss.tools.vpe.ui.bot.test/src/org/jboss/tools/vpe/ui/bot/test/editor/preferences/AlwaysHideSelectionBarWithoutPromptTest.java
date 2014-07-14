/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;

public class AlwaysHideSelectionBarWithoutPromptTest extends PreferencesTestCase{
	
	private static final String HID_SEL_BAR = "Hide selection bar"; //$NON-NLS-1$
	
	public void testAlwaysHideSelectionBarWithoutPrompt(){
		
		//Test Hide Selection Bar
	  openPage();
		selectSelection();
		checkIsHide();
		
		//Test Hide selection after reopen
		
		closePage();
		openPage();
		checkIsHide("https://issues.jboss.org/browse/JBIDE-17896");
		
		//Test Show Selection Bar
		
		selectSelection();
		checkIsShow();
		
		//Test Show Selection Bar after reopen
		
		closePage();
		openPage();
		checkIsShow();
		
		//Test Hide Selection Bar button without confirm
		Throwable exception = getException();
		bot.toolbarButtonWithTooltip(HID_SEL_BAR).click();
    if (exception == null){
      exception = getException();
      if (exception != null && exception instanceof NullPointerException){
        setException(null);
      }
    }
		checkIsHide();
	
		//Test Show selection after reopen
		
		closePage();
		openPage();
		checkIsHide();
				
	}
	
	private void checkIsHide(){
		checkIsHide("Toolbar button " + HID_SEL_BAR + " is not hidden");
	}
	
	private void checkIsHide(String message){
		WidgetNotFoundException exception = null;
		try {
			bot.toolbarButtonWithTooltip(HID_SEL_BAR);
		} catch (WidgetNotFoundException e) {
			exception = e;
		}
		assertNotNull(message,exception);
	}
	
	private void checkIsShow(){
		WidgetNotFoundException exception = null;
		try {
			bot.toolbarButtonWithTooltip(HID_SEL_BAR);
		} catch (WidgetNotFoundException e) {
			exception = e;
		}
		assertNull(exception);
	}

	private void selectSelection(){
	  Throwable exception = getException();
	  bot.toolbarToggleButtonWithTooltip(TOGGLE_SELECTION_BAR_TOOLTIP).click();
	  if (exception == null){
      exception = getException();
      if (exception != null && exception instanceof NullPointerException){
        setException(null);
      }
    }
	}
	
}
