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

import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

public class ShowNonVisualTagsTest extends PreferencesTestCase{
	
	public void testShowNonVisualTags() throws Throwable{
	  openPage();
	  //Test Show Non-Visual Tags
		selectShowNonVisual();
		closePage();
		openPage();
		SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(TEST_PAGE, new SWTBotExt());
		assertVisualEditorContainsNodeWithValue(webBrowser, 
        "jsp:directive.taglib",
        TEST_PAGE);
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        "f:loadBundle",
        TEST_PAGE);		
		//Test Hide Non-Visual Tags
		selectShowNonVisual();
		closePage();
		openPage();
		webBrowser = new SWTBotWebBrowser(TEST_PAGE, new SWTBotExt());
		assertVisualEditorNotContainNodeWithValue(webBrowser, 
        "jsp:directive.taglib",
        TEST_PAGE);
    assertVisualEditorNotContainNodeWithValue(webBrowser, 
        "f:loadBundle",
        TEST_PAGE);
	}
	
	private void selectShowNonVisual(){
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.checkBox(SHOW_NON_VISUAL_TAGS).click();
		bot.button("OK").click(); //$NON-NLS-1$
	}

}
