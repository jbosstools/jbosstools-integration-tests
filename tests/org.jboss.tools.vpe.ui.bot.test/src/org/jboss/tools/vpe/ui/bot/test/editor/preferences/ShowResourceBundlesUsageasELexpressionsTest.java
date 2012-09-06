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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

public class ShowResourceBundlesUsageasELexpressionsTest extends PreferencesTestCase{

	private static String textEditor;
	private static SWTBotEclipseEditor editor;

	public void testShowResourceBundlesUsageasELexpressions() throws Throwable{
	  
	  openPage();
		editor = bot.editorByTitle(TEST_PAGE).toTextEditor();
		textEditor = editor.getText();
    SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(TEST_PAGE, new SWTBotExt());
		//Test check VPE content with resource bundles
		selectELExpressions();
		assertVisualEditorContainsNodeWithValue(webBrowser, 
		    "#{Message.prompt_message}",
		    TEST_PAGE);
		assertVisualEditorContainsNodeWithValue(webBrowser, 
        "#{Message.header}",
        TEST_PAGE);
		assertVisualEditorNotContainNodeWithValue(webBrowser, 
        "Hello Demo Application",
        TEST_PAGE);
    assertVisualEditorNotContainNodeWithValue(webBrowser, 
        "Name:",
        TEST_PAGE);
		//Test check VPE content without resource bundles
		
		selectELExpressions();
		new SWTBotWebBrowser(TEST_PAGE, new SWTBotExt()).displayWebBrowserDOM();
		assertVisualEditorNotContainNodeWithValue(webBrowser, 
        "#{Message.prompt_message}",
        TEST_PAGE);
    assertVisualEditorNotContainNodeWithValue(webBrowser, 
        "#{Message.header}",
        TEST_PAGE);
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        "Hello Demo Application",
        TEST_PAGE);
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        "Name:",
        TEST_PAGE);
	}
	
	@Override
	public void tearDown() throws Exception {

		//Restore page state before tests
		
		editor.setFocus();
		bot.menu("Edit").menu("Select All").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$ //$NON-NLS-2$
		editor.setText(textEditor);
		editor.save();
		super.tearDown();
	}
	
	private void selectELExpressions(){
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.checkBox(SHOW_RESOURCE_BUNDLES).click();
		bot.button("OK").click(); //$NON-NLS-1$
	}
	
}
