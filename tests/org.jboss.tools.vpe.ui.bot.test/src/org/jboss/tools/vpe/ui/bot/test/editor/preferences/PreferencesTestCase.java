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

import java.io.File;
import java.io.IOException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public abstract class PreferencesTestCase extends VPEAutoTestCase{

	protected static final String TOGGLE_SELECTION_BAR_TOOLTIP = "Toggle selection tag bar (" +  //$NON-NLS-1$
	  (SWTJBTExt.isRunningOnMacOs() ? "⌘6" : "Ctrl+6") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	protected static final String SHOW_NON_VISUAL_TAGS = "Show non-visual tags"; //$NON-NLS-1$
	protected static final String SHOW_BORDER_FOR_UNKNOWN_TAGS = "Show border for unknown tags"; //$NON-NLS-1$
	protected static final String SHOW_RESOURCE_BUNDLES = "Show resource bundles usage as EL expressions"; //$NON-NLS-1$
	protected static final String ASK_FOR_ATTRIBUTES = "Ask for tag attributes during tag insert"; //$NON-NLS-1$
	protected static final String SELECT_DEFAULT_TAB = "Select the default active editor's tab"; //$NON-NLS-1$
	protected static final String EDITOR_SPLITTING = "Visual/Source editors splitting"; //$NON-NLS-1$
	protected static final String SHOW_TEXY_FORMAT = "Show text formatting bar"; //$NON-NLS-1$
	protected static final String PREF_TOOLTIP = "Preferences"; //$NON-NLS-1$
	protected static final String PREF_FILTER_SHELL_TITLE = "Preferences (Filtered)"; //$NON-NLS-1$
	protected static final String SCROLL_LOCK_TOOLTIP = "Synchronize scrolling between source and visual panes"; //$NON-NLS-1$
	protected static final String I18N_BUTTON_TOOLTIP1 = "Externalize selected string..."; //$NON-NLS-1$
	protected static final String I18N_BUTTON_TOOLTIP = "Externalize selected string... (" + //$NON-NLS-1$
			(SWTJBTExt.isRunningOnMacOs() ? "⌘7" : "Ctrl+7") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	protected static final String SHOW_VISUAL_TOOLBAR_PREFS_CHECKBOX_NAME = "Show toolbar within the editor (otherwise in Eclipse's toolbar)"; //$NON-NLS-1$
	protected static final String SHOW_SELECTION_TAG_BAR = "Show selection tag bar"; //$NON-NLS-1$
	
	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell("Preferences (Filtered)").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		boolean isOpened = false;
		try {
			bot.shell("Preferences (Filtered)").activate(); //$NON-NLS-1$
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
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"resources/preferences/"+testPage; //$NON-NLS-1$ //$NON-NLS-2$
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"preferences/"+testPage;  //$NON-NLS-1$//$NON-NLS-2$
		}
		return filePath;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		openPage();
		setPreferencesToDefault(true);
	}
	
	@Override
	public void tearDown() throws Exception {
		openPage();
		setPreferencesToDefault(true);
		super.tearDown();
	}
	
	void setPreferencesToDefault(boolean fromEditor) throws WidgetNotFoundException{
	  if (fromEditor){
	    bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click(); //$NON-NLS-1$
	    bot.shell(PREF_FILTER_SHELL_TITLE).activate(); //$NON-NLS-1$
	  }
	  else{
	    bot.menu(IDELabel.Menu.WINDOW).menu(IDELabel.Menu.PREFERENCES).click(); //$NON-NLS-1$ //$NON-NLS-2$
	    SWTBotTree preferenceTree = bot.tree();
	    preferenceTree
	      .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS) //$NON-NLS-1$
  	    .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB) //$NON-NLS-1$
	      .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB_EDITORS) //$NON-NLS-1$
	      .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB_EDITORS_VPE).select();
	  }
		SWTBotCheckBox checkBox = bot.checkBox(SHOW_SELECTION_TAG_BAR);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(SHOW_BORDER_FOR_UNKNOWN_TAGS);
    if (!checkBox.isChecked()) {
      checkBox.click();
    }
		checkBox = bot.checkBox(SHOW_NON_VISUAL_TAGS);
		if (checkBox.isChecked()) {
			checkBox.click();
		}
		
		checkBox = bot.checkBox(SHOW_RESOURCE_BUNDLES);
		if (checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(ASK_FOR_ATTRIBUTES);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(SHOW_TEXY_FORMAT);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		SWTBotCombo combo = bot.comboBoxWithLabel(SELECT_DEFAULT_TAB);
		combo.setSelection("Visual/Source"); //$NON-NLS-1$
		combo = bot.comboBoxWithLabel(EDITOR_SPLITTING);
		combo.setSelection("Vertical splitting with Source Editor on the top"); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
	}
	
}
