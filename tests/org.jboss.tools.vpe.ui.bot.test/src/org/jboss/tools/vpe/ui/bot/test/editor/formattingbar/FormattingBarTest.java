/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.formattingbar;

import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;

/**
 * Class for testing VPE Formatting Bar
 * @author dmaliarevich
 */
public class FormattingBarTest extends VPEEditorTestCase {
	
	private static final String DIV_TEXT = "<div> div with text </div>"; //$NON-NLS-1$
	private static final String STYLE_TEXT = " style = \"color: red; font-size: 14px;\""; //$NON-NLS-1$
	private static final String STYLE_TEXT2 = " style = \"color: red;font-size: 14px;\""; //$NON-NLS-1$
	private static final String STYLE_TEXT3 = " style = \"color: red;font-size:20px;\""; //$NON-NLS-1$
	
	private SWTBotExt botExt = null;
	private SWTBotEditorExt jspEditor;
	
	public FormattingBarTest() {
		super();
		botExt = new SWTBotExt();
	}

	public void testEditStyleButton() throws Throwable {
		/*
		 * Open test page
		 */
		openPage();
		jspEditor = botExt.swtBotEditorExtByTitle(TEST_PAGE);
		/*
		 * Set cursor to some html tag
		 */
		jspEditor.insertText(7, 6, ""); //$NON-NLS-1$
		bot.sleep(Timing.time2S());
		/*
		 * Style formatting should be disabled
		 */
		assertFalse("Style formatting should be disabled", bot //$NON-NLS-1$
				.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE)
				.isEnabled());
		/*
		 * Add some html text
		 */
		jspEditor.insertText(10, 0, DIV_TEXT);
		/*
		 * Put the cursor
		 */
		jspEditor.insertText(10, 2, ""); //$NON-NLS-1$
		bot.sleep(Timing.time2S());
		/*
		 * Check the button
		 */
		assertTrue("Style formatting should be enabled", bot //$NON-NLS-1$
				.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE)
				.isEnabled());
		/*
		 * Open the dialog
		 */
		bot.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE).click();
		bot.shell(JstUIMessages.CSS_STYLE_EDITOR_TITLE).setFocus();
		bot.shell(JstUIMessages.CSS_STYLE_EDITOR_TITLE).activate();
		/*
		 * Press OK with no changes
		 */
		assertTrue("(OK) button should be enabled.", //$NON-NLS-1$
		bot.button(WidgetVariables.OK_BUTTON).isEnabled());
		bot.button(WidgetVariables.OK_BUTTON).click();
		/*
		 * Check base style values
		 */
		jspEditor.insertText(10, 4, STYLE_TEXT);
		jspEditor.insertText(10, 2, ""); //$NON-NLS-1$
		bot.sleep(Timing.time2S());
		/*
		 * Check the button
		 */
		assertTrue("Style formatting should be enabled", bot //$NON-NLS-1$
				.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE)
				.isEnabled());
		/*
		 * Open the dialog
		 */
		bot.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE).click();
		bot.shell(JstUIMessages.CSS_STYLE_EDITOR_TITLE).setFocus();
		bot.shell(JstUIMessages.CSS_STYLE_EDITOR_TITLE).activate();
		/*
		 * Press OK with no changes
		 */
		assertTrue("(OK) button should be enabled.", //$NON-NLS-1$
		bot.button(WidgetVariables.OK_BUTTON).isEnabled());
		bot.button(WidgetVariables.OK_BUTTON).click();
		/*
		 * Check the updated style text
		 */
		jspEditor.selectRange(10, 4, 38);
		assertEquals("Style string should be updated", STYLE_TEXT2, jspEditor.getSelection()); //$NON-NLS-1$
		/*
		 * Check the button
		 */
		assertTrue("Style formatting should be enabled", bot //$NON-NLS-1$
				.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE)
				.isEnabled());
		/*
		 * Open the dialog
		 */
		bot.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE).click();
		bot.shell(JstUIMessages.CSS_STYLE_EDITOR_TITLE).setFocus();
		bot.shell(JstUIMessages.CSS_STYLE_EDITOR_TITLE).activate();
		bot.comboBoxWithLabel("font-size:").setText("20"); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * Press OK with no changes
		 */
		assertTrue("(OK) button should be enabled.", //$NON-NLS-1$
		bot.button(WidgetVariables.OK_BUTTON).isEnabled());
		bot.button(WidgetVariables.OK_BUTTON).click();
		/*
		 * Check the updated style text
		 */
		jspEditor.selectRange(10, 4, 37);
		assertEquals("Style string should be updated", STYLE_TEXT3, jspEditor.getSelection()); //$NON-NLS-1$
		/*
		 * Check the button
		 */
		assertTrue("Style formatting should be enabled", bot //$NON-NLS-1$
				.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE)
				.isEnabled());
		/*
		 * Open the dialog
		 */
		bot.toolbarButtonWithTooltip(VpeUIMessages.EDIT_STYLE_ATTRIBUTE).click();
		bot.shell(JstUIMessages.CSS_STYLE_EDITOR_TITLE).setFocus();
		bot.shell(JstUIMessages.CSS_STYLE_EDITOR_TITLE).activate();
		bot.comboBoxWithLabel("font-size:").setText("8"); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * Press OK with no changes
		 */
		assertTrue("(Cancel) button should be enabled.", //$NON-NLS-1$
		bot.button(WidgetVariables.CANCEL_BUTTON).isEnabled());
		bot.button(WidgetVariables.CANCEL_BUTTON).click();
		jspEditor.selectRange(10, 4, 37);
		assertEquals("Style string should be updated", STYLE_TEXT3, jspEditor.getSelection()); //$NON-NLS-1$
	}

}
