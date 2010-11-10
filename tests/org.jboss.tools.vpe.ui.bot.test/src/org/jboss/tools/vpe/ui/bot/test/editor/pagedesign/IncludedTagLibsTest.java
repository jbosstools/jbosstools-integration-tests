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
 * Tests functionality of Included Tag Libs tab page of Page Design Options Dialog 
 * @author vlado pakan
 *
 */
public class IncludedTagLibsTest extends PageDesignTestCase {
  
  private SWTBot addTaglibReferenceDialogBot = null;
  private SWTBot optionsDialogBot  = null;
  private SWTBot editTaglibReferenceDialogBot = null;
  
  public void testIncludedTagLibs(){
    openPage();
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.INCLUDED_TAG_LIBS_TAB).activate();
    optionsDialogBot.button(IDELabel.Button.ADD_WITHOUT_DOTS).click();
    addTaglibReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_TAGLIB_REFRENCE).activate().bot();
    SWTBotText txMessage = addTaglibReferenceDialogBot.text(2);
    SWTBotText txURI = addTaglibReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.INCLUDED_TAG_LIBS_URI);
    SWTBotText txPrefix = addTaglibReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.INCLUDED_TAG_LIBS_PREFIX);
    // Tests default message
    final String defaultMessage = txMessage.getText();
    final String defaultMessageStartsWith = "Add TLD definition";
    assertTrue("Default Dialog Message has to start with '" + defaultMessageStartsWith + "' but is: " + defaultMessage,
        defaultMessage.startsWith(defaultMessageStartsWith));
    // Tests empty prefix message
    final String testURI = "http://java.sun.com/jsf/core";
    txURI.setText(testURI);
    final String emptyPrefixMessage = " Prefix should be set.";
    assertTrue("Dialog Message has to be '" + emptyPrefixMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(emptyPrefixMessage));
    // Tests when URI and Prefix is properly set 
    final String testPrefix = "pf";
    txPrefix.setText(testPrefix);
    assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(defaultMessage));
    // Tests incorrect prefix
    final String incorrectPrefix = testPrefix + ";";
    txPrefix.setText(incorrectPrefix);
    final String incorrectPrefixMessage = " Incorrect Prefix: " + incorrectPrefix;
    assertTrue("Dialog Message has to be '" + incorrectPrefixMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(incorrectPrefixMessage));
    // Sets proper values and close dialog
    txPrefix.setText(testPrefix);
    txURI.setText(testURI);
    assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(defaultMessage));
    addTaglibReferenceDialogBot.button(IDELabel.Button.FINISH).click();
    addTaglibReferenceDialogBot = null;
    // Reopens dialog and tests saved values
    optionsDialogBot.button(IDELabel.Button.EDIT_WITHOUT_DOTS).click();
    editTaglibReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_TAGLIB_REFRENCE).activate().bot();
    txMessage = editTaglibReferenceDialogBot.text(2);
    txURI = editTaglibReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.INCLUDED_TAG_LIBS_URI);
    txPrefix = editTaglibReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.INCLUDED_TAG_LIBS_PREFIX);
    assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
        txMessage.getText().equals(defaultMessage));
    assertTrue("URI value has to be'" + testURI + "' but is: " + txURI.getText(),
        txURI.getText().equals(testURI));
    assertTrue("Prefix value has to be'" + testPrefix + "' but is: " + txPrefix.getText(),
        txPrefix.getText().equals(testPrefix));
    editTaglibReferenceDialogBot.button(IDELabel.Button.CANCEL).click();
    editTaglibReferenceDialogBot = null;
    optionsDialogBot.button(IDELabel.Button.OK).click();
    optionsDialogBot = null;
	}
	
	@Override
	protected void closeUnuseDialogs() {
    if (addTaglibReferenceDialogBot != null){
      addTaglibReferenceDialogBot.button(IDELabel.Button.CANCEL).click();
      addTaglibReferenceDialogBot = null;
    }
    if (editTaglibReferenceDialogBot != null){
      editTaglibReferenceDialogBot.button(IDELabel.Button.CANCEL).click();
      editTaglibReferenceDialogBot = null;
    }
    if (optionsDialogBot != null){
      optionsDialogBot.button(IDELabel.Button.OK).click();
      optionsDialogBot = null;
    }
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return optionsDialogBot != null
		    || addTaglibReferenceDialogBot != null
		    || editTaglibReferenceDialogBot != null;
	}
  
}
