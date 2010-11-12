/*******************************************************************************

 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Tests functionality of Substituted EL Expressions tab page of Page Design Options Dialog 
 * @author vlado pakan
 *
 */
public class SubstitutedELExressionsTest extends PageDesignTestCase {
  
  private SWTBot addELReferenceDialogBot = null;
  private SWTBot optionsDialogBot  = null;
  private SWTBot editELReferenceDialogBot = null;
  
  public void testSubstitutedELExressions(){
    openPage();
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_TAB).activate();
    optionsDialogBot.button(IDELabel.Button.ADD_WITHOUT_DOTS).click();
    addELReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_EL_REFERENCE).activate().bot();
    SWTBotText txMessage = addELReferenceDialogBot.text(2);
    SWTBotText txValue = addELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_VALUE);
    SWTBotText txName = addELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_EL_NAME);
    // Tests default message
    final String defaultMessage = txMessage.getText();
    final String defaultMessageStartsWith = "Add El variable";
    assertTrue("Default Dialog Message has to start with '" + defaultMessageStartsWith + "' but is: " + defaultMessage,
        defaultMessage.startsWith(defaultMessageStartsWith));
    // Tests properly set Value
    final String testName = "page.consist";
    txName.setText(testName);
    assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(defaultMessage));
    // Tests incorrect name message
    txName.setText(testName + " error");
    final String invalidELNameMessage = " Invalid EL expression."; 
    assertTrue("Dialog Message has to be '" + invalidELNameMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(invalidELNameMessage));
    // Tests properly set Name and Value
    txName.setText(testName);
    final String testValue = "test value";
    txValue.setText(testValue);
    assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(defaultMessage));
    addELReferenceDialogBot.button(IDELabel.Button.FINISH).click();
    addELReferenceDialogBot = null;
    // Reopens dialog and tests saved values
    optionsDialogBot.button(IDELabel.Button.EDIT_WITHOUT_DOTS).click();
    editELReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_EL_REFERENCE).activate().bot();
    txMessage = editELReferenceDialogBot.text(2);
    txName = editELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_EL_NAME);
    txValue = editELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_VALUE);
    assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(defaultMessage));
    assertTrue("Value has to be'" + testValue + "' but is: " + txValue.getText(),
        txValue.getText().equals(testValue));
    assertTrue("Name value has to be'" + testName + "' but is: " + txName.getText(),
        txName.getText().equals(testName));
    editELReferenceDialogBot.button(IDELabel.Button.CANCEL).click();
    editELReferenceDialogBot = null;
    optionsDialogBot.button(IDELabel.Button.OK).click();
    optionsDialogBot = null;
	}
	
	@Override
	protected void closeUnuseDialogs() {
    if (addELReferenceDialogBot != null){
      addELReferenceDialogBot.button(IDELabel.Button.CANCEL).click();
      addELReferenceDialogBot = null;
    }
    if (editELReferenceDialogBot != null){
      editELReferenceDialogBot.button(IDELabel.Button.CANCEL).click();
      editELReferenceDialogBot = null;
    }
    if (optionsDialogBot != null){
      optionsDialogBot.button(IDELabel.Button.OK).click();
      optionsDialogBot = null;
    }
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return optionsDialogBot != null
		    || addELReferenceDialogBot != null
		    || editELReferenceDialogBot != null;
	}
  
}
