/*******************************************************************************
 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.api.ToolItem;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Ignore;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMRange;

/**
 * Tests VPE Toolbar Buttons
 * 
 * @author vlado pakan
 *
 */
@Ignore
public class ToolbarTextFormattingTest extends VPEAutoTestCase {
	private static final String TEXT_TEST_EXPRESSION = "Test String Expression";
	private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
			+ "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + "<html>\n" + "<head>\n"
			+ "<title></title>\n" + "</head>\n" + "<body>\n" + "<f:view>\n"
			+ ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION + "\n" + "</f:view>\n" + "</body>\n" + "</html>";

	private static final String TEST_PAGE_NAME = "ToolbarTest.jsp";
	@Test
	public void testToolbarTextFormatting() {
		createJspPage(ToolbarTextFormattingTest.TEST_PAGE_NAME, JBT_TEST_PROJECT_NAME, "WebContent", "pages");
		final TextEditor jspTextEditor = new TextEditor(ToolbarTextFormattingTest.TEST_PAGE_NAME);
		jspTextEditor.setText(ToolbarTextFormattingTest.PAGE_TEXT);
		jspTextEditor.save();
		jspTextEditor.setCursorPosition(8, 3);
		SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(ToolbarTextFormattingTest.TEST_PAGE_NAME);
		// Selects text using right arrow and shift
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, SWT.ARROW_RIGHT);
		nsIDOMRange firstRange = webBrowser.getSelection().getRangeAt(0);
		int webBrowserSelectionSize = firstRange.getEndOffset() - firstRange.getStartOffset();
		assertTrue("Selection within Visual Editor has to contain 3 characters but it contains "
				+ webBrowserSelectionSize + " characters.", webBrowserSelectionSize == 3);
		jspTextEditor.setCursorPosition(8, 10);
		// Selects text using left arrow and shift
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, SWT.ARROW_LEFT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, SWT.ARROW_LEFT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, SWT.ARROW_LEFT);
		firstRange = webBrowser.getSelection().getRangeAt(0);
		assertTrue("Selection within Visual Editor has to contain 3 characters but it contains "
				+ webBrowserSelectionSize + " characters.", webBrowserSelectionSize == 3);
		jspTextEditor.setCursorPosition(8, 10);
		ToolItem boldButton = new DefaultToolItem("Bold");
		ToolItem italicButton = new DefaultToolItem("Italic");
		ToolItem underlinedButton = new DefaultToolItem("Underline");
		// Check if Text Format Buttons buttons are enabled within VPE Editor
		assertToolbarButtonIsEnabled(boldButton, "https://issues.jboss.org/browse/JBIDE-16938");
		assertToolbarButtonIsEnabled(italicButton);
		assertToolbarButtonIsEnabled(underlinedButton);
		boldButton.click();
		jspTextEditor.save();
		int editorLine = jspTextEditor.getCursorPosition().y;
		assertTagIsInserted("b", jspTextEditor.getTextAtLine(editorLine - 1) + jspTextEditor.getTextAtLine(editorLine),
				ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION);
		italicButton.click();
		jspTextEditor.save();
		editorLine = jspTextEditor.getCursorPosition().y;
		assertTagIsInserted("i", jspTextEditor.getTextAtLine(editorLine - 1) + jspTextEditor.getTextAtLine(editorLine),
				ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION, "b");
		underlinedButton.click();
		jspTextEditor.save();
		editorLine = jspTextEditor.getCursorPosition().y;
		assertTagIsInserted("u", jspTextEditor.getTextAtLine(editorLine - 1) + jspTextEditor.getTextAtLine(editorLine),
				ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION, "b", "i");
		bot.sleep(Timing.time2S());
		// Check if Text Format Buttons buttons are toggled
		assertToggleToolbarSelection(true, boldButton);
		assertToggleToolbarSelection(true, italicButton);
		assertToggleToolbarSelection(true, underlinedButton);
		underlinedButton.click();
		jspTextEditor.save();
		assertTagIsInserted("i", jspTextEditor.getTextAtLine(jspTextEditor.getCursorPosition().y), ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION,
				"b");
		italicButton.click();
		jspTextEditor.save();
		assertTagIsInserted("b", jspTextEditor.getTextAtLine(jspTextEditor.getCursorPosition().y), ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION);
		boldButton.click();
		jspTextEditor.save();
		String currentLine = jspTextEditor.getTextAtLine(jspTextEditor.getCursorPosition().y);
		String expectedLineText = "<f:view>" + ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION;
		assertTrue("Selected line has text: '" + currentLine + "'.\n" + "Expected text is: '" + expectedLineText + "'.",
				currentLine.equals(expectedLineText));
		// Check if Text Format Buttons buttons are toggled
		assertToggleToolbarSelection(false, boldButton);
		assertToggleToolbarSelection(false, italicButton);
		assertToggleToolbarSelection(false, underlinedButton);
		jspTextEditor.close();
	}

	/**
	 * Asserts if tag is correctly inserted to lineText
	 * 
	 * @param tag
	 * @param lineText
	 * @param expressionString
	 *            - string expression which has to be inserted inside tag
	 * @param previousTags
	 *            previous tags added to expressionString
	 */
	private void assertTagIsInserted(String tag, String lineText, String expressionString, String... previousTags) {
		StringBuffer sb = new StringBuffer("");
		StringBuilder sbSuffix = new StringBuilder("");
		if (previousTags != null) {
			for (String previousTag : previousTags) {
				sb.append("<");
				sb.append(previousTag);
				sb.append(">");
				sbSuffix.insert(0, ">");
				sbSuffix.insert(0, previousTag);
				sbSuffix.insert(0, "</");
			}
		}
		sb.append("<");
		sb.append(tag);
		sb.append(">");
		sb.append(expressionString);
		sb.append("</");
		sb.append(tag);
		sb.append(">");
		sb.append(sbSuffix);
		final String expectedLineText = sb.toString();
		assertTrue("Selected line has text: '" + lineText + "'.\n" + "Expected text is: '" + expectedLineText + "'.",
				lineText.endsWith(expectedLineText));
	}

	/**
	 * Asserts if Toolbar Button is enabled
	 * 
	 * @param toolbarButton
	 */
	private void assertToolbarButtonIsEnabled(ToolItem toolItem) {
		assertToolbarButtonIsEnabled(toolItem,
				toolItem.getToolTipText() + " Toolbar Button has to be enabled but it is not.");
	}

	/**
	 * Asserts if Toolbar Button is enabled
	 * 
	 * @param toolbarButton
	 */
	private void assertToolbarButtonIsEnabled(ToolItem toolItem, String message) {
		assertTrue(toolItem.getToolTipText() + " Toolbar Button has to be enabled but it is not."
				+ ((message == null || message.length() == 0) ? "" : message), toolItem.isEnabled());
	}

	/**
	 * Asserts Toolbar Button selection
	 * 
	 * @param expectedSelection
	 * @param toolbarButton
	 */
	private void assertToggleToolbarSelection(boolean expectedSelection, ToolItem toolItem) {
		assertTrue(
				toolItem.getToolTipText() + " Toolbar Button has " + (expectedSelection ? "" : "not ")
						+ "to be toggled but it is" + (toolItem.isSelected() ? "" : " not") + ".",
				toolItem.isSelected() == expectedSelection);
	}

}
