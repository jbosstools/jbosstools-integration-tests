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
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.parts.SWTBotTableExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;

public abstract class PageDesignTestCase extends VPEEditorTestCase{
	
	final static String PAGE_DESIGN = "Page Design Options"; //$NON-NLS-1$

	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell(PAGE_DESIGN).close();
		} catch (WidgetNotFoundException e) {
		}
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		boolean isOpened = false;
		try {
			bot.shell(PAGE_DESIGN).activate();
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
	void closePage(){
		bot.editorByTitle(TEST_PAGE).close();
	}
	
	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"resources/pagedesign/"+testPage;  //$NON-NLS-1$//$NON-NLS-2$
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"pagedesign/"+testPage; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return filePath;
	}
	/**
	 * Deletes all defined EL Substitutions. VPE has to be opened when called this method
	 */
	public void deleteAllELSubstitutions(){
	  bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
	  SWTBot optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_TAB).activate();
    SWTBotTable elVariablesTable = optionsDialogBot.table();
    while (elVariablesTable.rowCount() > 0){
      elVariablesTable.select(0);
      optionsDialogBot.button(IDELabel.Button.REMOVE).click();
    }
    optionsDialogBot.button(IDELabel.Button.OK).click();
    bot.sleep(Timing.time2S());
	}
	/**
	 * Adds EL Definition
	 * @param elName
	 * @param value
	 * @param scope
	 */
	public void addELSubstitution (String elName , String value , String scope){
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    SWTBot optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_TAB).activate();
    optionsDialogBot.button(IDELabel.Button.ADD_WITHOUT_DOTS).click();
    SWTBot addELReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_EL_REFERENCE).activate().bot();
    addELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_EL_NAME)
      .setText(elName);
    addELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_VALUE)
      .setText(value);
    addELReferenceDialogBot.radio(scope).click();
    addELReferenceDialogBot.button(IDELabel.Button.FINISH).click();
    optionsDialogBot.button(IDELabel.Button.OK).click();
	}
	 /**
   * Edits EL Variable elName Definition
   * @param elName
   * @param oldScope
   * @param newValue
   * @param scopeRadioLabel
   */
  public void editELSubstitution (String elName , String oldScope, String newValue , String scopeRadioLabel){
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    SWTBot optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_TAB).activate();
    new SWTBotTableExt(optionsDialogBot.table())
      .getTableItem(oldScope,elName)
      .select();
    optionsDialogBot.button(IDELabel.Button.EDIT_WITHOUT_DOTS).click();
    SWTBot addELReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_EL_REFERENCE).activate().bot();
    addELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_VALUE)
      .setText(newValue);
    addELReferenceDialogBot.radio(scopeRadioLabel).click();
    addELReferenceDialogBot.button(IDELabel.Button.FINISH).click();
    optionsDialogBot.button(IDELabel.Button.OK).click();
  }
  /**
   * Deletes EL Variable elName Definition
   * @param elName
   * @param scope
   */
  public void deleteELSubstitution (String elName , String scope){
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    SWTBot optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_TAB).activate();
    new SWTBotTableExt(optionsDialogBot.table())
      .getTableItem(scope,elName)
      .select();
    optionsDialogBot.button(IDELabel.Button.REMOVE).click();
    optionsDialogBot.button(IDELabel.Button.OK).click();
  }
}
