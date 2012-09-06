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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

public class BorderForUnknownTagsTest extends PreferencesTestCase{

	private static String textEditor;
	private static SWTBotEclipseEditor editor;

	public void testBorderForUnknownTags() throws Throwable{
		
		//Test open page
	  openPage();
		editor = bot.editorByTitle(TEST_PAGE).toTextEditor();
		textEditor = editor.getText();
			
		//Test insert unknown tag
		
		editor.navigateTo(12, 52);
		final String unknownTag = "tagunknown";
		editor.insertText("<" + unknownTag + "></" + unknownTag + ">"); //$NON-NLS-1$
	
		//Test default Show Border value
		
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		SWTBotCheckBox checkBox = bot.checkBox(SHOW_BORDER_FOR_UNKNOWN_TAGS);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		bot.button("OK").click(); //$NON-NLS-1$
	
		//Test check VPE content
		
		SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(TEST_PAGE, new SWTBotExt());
		assertVisualEditorContains(webBrowser, 
		    "DIV",
		    new String[]{"style","title"},
		    new String[]{"-moz-user-modify: read-only; border: 1px solid green;",unknownTag}, 
		    TEST_PAGE);
		//Test hide border for unknown tag
		
		selectBorder();
		assertVisualEditorContains(webBrowser, 
        "DIV",
        new String[]{"style","title"},
        new String[]{"-moz-user-modify: read-only;",unknownTag}, 
        TEST_PAGE);
				
		//Test restore previous state
		
		selectBorder();
		assertVisualEditorContains(webBrowser, 
        "DIV",
        new String[]{"style","title"},
        new String[]{"-moz-user-modify: read-only; border: 1px solid green;",unknownTag}, 
        TEST_PAGE);
		
	}
	
	private void selectBorder(){
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.checkBox(SHOW_BORDER_FOR_UNKNOWN_TAGS).click();
		bot.button("OK").click(); //$NON-NLS-1$
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
	
}
