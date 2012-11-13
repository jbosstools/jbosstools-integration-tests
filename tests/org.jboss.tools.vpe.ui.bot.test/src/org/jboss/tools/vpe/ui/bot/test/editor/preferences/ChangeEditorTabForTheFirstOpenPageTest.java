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

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class ChangeEditorTabForTheFirstOpenPageTest extends PreferencesTestCase{
	
	public void testChangeEditorTabForTheFirstOpenPage(){
		
		//Test set default source tab
	  openPage();
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.comboBoxWithLabel(SELECT_DEFAULT_TAB)
		  .setSelection(IDELabel.VisualPageEditor.SOURCE_TAB_LABEL);
		bot.button("OK").click(); //$NON-NLS-1$
		
		//Create and open new page
		
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
	
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent") //$NON-NLS-1$
		.getNode("pages").select(); //$NON-NLS-1$
		
		open.newObject(ActionItem.NewObject.WebJSP.LABEL);
		bot.shell(IDELabel.Shell.NEW_JSP_FILE).activate(); //$NON-NLS-1$
		bot.textWithLabel(ActionItem.NewObject.WebJSP.TEXT_FILE_NAME).setText("testPage"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button(IDELabel.Button.FINISH).click(); //$NON-NLS-1$
		bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).setFocus();
		
		//Check if the tab changed
		Exception exception = null;
		try {
			bot.toolbarButtonWithTooltip("Refresh").click(); //$NON-NLS-1$
		} catch (WidgetNotFoundException wnfe) {
			exception = wnfe;
		} catch (TimeoutException te){
		  exception = te;
		}
		assertNotNull(exception);
	}
	
	@Override
	public void tearDown() throws Exception {
  	//Delete test page if it has been created
    eclipse.deleteFile(VPEAutoTestCase.JBT_TEST_PROJECT_NAME,  "WebContent","pages","testPage.jsp");
		util.waitForNonIgnoredJobs(Timing.time3S());
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
    bot.shell(PREF_FILTER_SHELL_TITLE).activate();
    bot.comboBoxWithLabel(SELECT_DEFAULT_TAB).setSelection(IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL); //$NON-NLS-1$
    bot.button(IDELabel.Button.OK).click();
    super.tearDown();
	}
	
}
