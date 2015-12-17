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
package org.jboss.tools.vpe.ui.bot.test.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Tests JSP file Editing and Cut, Copy, Paste actions through Visual Editor
 * Menu for Text selection
 * 
 * @author vlado pakan
 *
 */
public class TextEditingActionsTest extends VPEEditorTestCase {
	private static final String TEXT_TO_EDIT = "Text to edit";

	private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\"%>\n"
			+ "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" + "<html>\n" + "  <head>\n"
			+ "    <title>Input User Name Page</title>\n" + "  </head>\n" + "  <body>\n" + "    <f:view>\n"
			+ "      <h:outputText value=\"" + TextEditingActionsTest.TEXT_TO_EDIT + "\"/>\n" + "    </f:view>\n"
			+ "  </body>\n" + "</html>";

	private static final String TEST_PAGE_NAME = "TextEditingActionsTest.jsp";

	private TextEditor jspEditor = null;
	private SWTBotWebBrowser webBrowser = null;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		createJspPage(TextEditingActionsTest.TEST_PAGE_NAME,JBT_TEST_PROJECT_NAME,"WebContent", "pages");
		openPage(TextEditingActionsTest.TEST_PAGE_NAME);
		new WaitWhile(new JobIsRunning());
		jspEditor = new TextEditor(TextEditingActionsTest.TEST_PAGE_NAME);
		webBrowser = new SWTBotWebBrowser(TextEditingActionsTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests Cut Copy Paste Operations on Blank Page
	 */
	@Test
	public void testCutCopyPasteBlankPage() {
		jspEditor.setText("");
		jspEditor.save();
		new WaitWhile(new JobIsRunning());
		jspEditor.setText(TextEditingActionsTest.TEXT_TO_EDIT);
		jspEditor.save();
		new WaitWhile(new JobIsRunning());
		// Check Copy Functionality
		String textToCutCopy = TextEditingActionsTest.TEXT_TO_EDIT.substring(0, 4);
		nsIDOMNode node = webBrowser.getDomNodeByTagName("SPAN", 0);
		webBrowser.selectDomNode(node, 0);
		jspEditor.setCursorPosition(0);
		webBrowser.setFocus();
		TextEditingActionsTest.selectTextUsingKeyBoard(true, textToCutCopy.length());
		webBrowser.clickContextMenu(node, SWTBotWebBrowser.COPY_MENU_LABEL);
		jspEditor.setCursorPosition(0, TextEditingActionsTest.TEXT_TO_EDIT.length());
		webBrowser.setFocus();
		webBrowser.clickContextMenu(node, SWTBotWebBrowser.PASTE_MENU_LABEL);
		jspEditor.save();
		new WaitWhile(new JobIsRunning());
		String textToContain = TextEditingActionsTest.TEXT_TO_EDIT + textToCutCopy;
		Assertions.assertSourceEditorContains(jspEditor.getText(), textToContain,
				TextEditingActionsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, textToContain, TextEditingActionsTest.TEST_PAGE_NAME);
		// Check Cut Functionality
		webBrowser.selectDomNode(node, 0);
		jspEditor.setCursorPosition(0, 0);
		webBrowser.setFocus();
		TextEditingActionsTest.typeKeyCodeRepeately(SWT.ARROW_RIGHT, textToContain.length());
		TextEditingActionsTest.selectTextUsingKeyBoard(false, textToCutCopy.length());
		webBrowser.clickContextMenu(node, SWTBotWebBrowser.CUT_MENU_LABEL);
		jspEditor.save();
		jspEditor.setCursorPosition(0);
		Assertions.assertSourceEditorIs(jspEditor.getText(), TextEditingActionsTest.TEXT_TO_EDIT,
				TextEditingActionsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, TextEditingActionsTest.TEXT_TO_EDIT,
				TextEditingActionsTest.TEST_PAGE_NAME);
		webBrowser.setFocus();
		webBrowser.clickContextMenu(node, SWTBotWebBrowser.PASTE_MENU_LABEL);
		jspEditor.save();
		textToContain = textToCutCopy + TextEditingActionsTest.TEXT_TO_EDIT;
		Assertions.assertSourceEditorContains(jspEditor.getText(), textToContain,
				TextEditingActionsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, textToContain, TextEditingActionsTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests insert Enter in Visual Editor
	 */
	@Test
	public void testInsertEnter() {

		jspEditor.setText(TextEditingActionsTest.PAGE_TEXT);
		jspEditor.save();
		nsIDOMNode node = webBrowser.getDomNodeByTagName("SPAN", 0);
		webBrowser.selectDomNode(node, 0);
		// Check inserting Enter Functionality
		jspEditor.setCursorPosition(jspEditor.getPositionOfText(TextEditingActionsTest.TEXT_TO_EDIT));
		webBrowser.setFocus();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		TextEditingActionsTest.typeKeyCodeRepeately(SWT.CR, 6);
		jspEditor.save();
		String jspEditorText = jspEditor.getText();
		assertTrue(
				"Source Editor should has text " + TextEditingActionsTest.PAGE_TEXT + "\nbut it is\n" + jspEditorText,
				jspEditorText.equals(TextEditingActionsTest.PAGE_TEXT));
	}

	/**
	 * Tests page editing via keyboard Enter in Visual Editor
	 */
	@Test
	public void testKeyboardEditing() {

		jspEditor.setText(TextEditingActionsTest.PAGE_TEXT);
		jspEditor.save();
		nsIDOMNode node = webBrowser.getDomNodeByTagName("SPAN", 0);
		webBrowser.selectDomNode(node, 0);
		// Check inserting Enter Functionality
		jspEditor.setCursorPosition(jspEditor.getPositionOfText(TextEditingActionsTest.TEXT_TO_EDIT));
		webBrowser.setFocus();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_LEFT);
		String testText = "insertedTextDeleteBackspace";
		KeyboardFactory.getKeyboard().type(testText);
		jspEditor.save();
		String sourceEditorText = jspEditor.getText();
		String textToContain = "<h:outputText value=\"" + testText;
		assertTrue("Source Editor has to containt text " + textToContain + "\nbut it doesn't.\nSource Editor text is:\n"
				+ sourceEditorText, sourceEditorText.contains(textToContain));
		// Test Backspace
		int lengthToRemove = 9;
		TextEditingActionsTest.typeKeyCodeRepeately(SWT.BS, lengthToRemove);
		jspEditor.save();
		textToContain = textToContain.substring(0, textToContain.length() - lengthToRemove);
		assertTrue("Source Editor has to containt text " + textToContain + "\nbut it doesn't.\nSource Editor text is:\n"
				+ sourceEditorText, sourceEditorText.contains(textToContain));
		// Test Delete
		lengthToRemove = 6;
		TextEditingActionsTest.typeKeyCodeRepeately(SWT.ARROW_LEFT, lengthToRemove);
		TextEditingActionsTest.typeKeyCodeRepeately(SWT.DEL, lengthToRemove);
		jspEditor.save();
		textToContain = textToContain.substring(0, textToContain.length() - lengthToRemove);
		assertTrue("Source Editor has to containt text " + textToContain + "\nbut it doesn't.\nSource Editor text is:\n"
				+ sourceEditorText, sourceEditorText.contains(textToContain));

	}

	@Override
	public void tearDown() throws Exception {
		jspEditor.close();
		super.tearDown();
	}

	/**
	 * Tests Cut Copy Paste Operations on Value Attribute
	 */
	@Test
	public void testCutCopyPasteValueAttribute() {
		jspEditor.setText(TextEditingActionsTest.PAGE_TEXT);
		jspEditor.save();
		nsIDOMNode node = webBrowser.getDomNodeByTagName("SPAN", 0);
		webBrowser.selectDomNode(node, 0);
		// Check Copy Functionality
		jspEditor.setCursorPosition(jspEditor.getPositionOfText(TextEditingActionsTest.TEXT_TO_EDIT));
		Point cursorPosition = jspEditor.getCursorPosition();
		webBrowser.setFocus();
		String textToCutCopy = TextEditingActionsTest.TEXT_TO_EDIT.substring(0, 4);
		TextEditingActionsTest.selectTextUsingKeyBoard(true, textToCutCopy.length());
		webBrowser.setFocus();
		webBrowser.clickContextMenu(node, SWTBotWebBrowser.COPY_MENU_LABEL);
		webBrowser.setFocus();
		TextEditingActionsTest.typeKeyCodeRepeately(SWT.ARROW_RIGHT,
				TextEditingActionsTest.TEXT_TO_EDIT.length() - textToCutCopy.length());
		webBrowser.setFocus();
		webBrowser.clickContextMenu(node, SWTBotWebBrowser.PASTE_MENU_LABEL);
		jspEditor.save();
		String textToContain = TextEditingActionsTest.TEXT_TO_EDIT + textToCutCopy;
		Assertions.assertSourceEditorContains(jspEditor.getText(), textToContain,
				TextEditingActionsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, textToContain, TextEditingActionsTest.TEST_PAGE_NAME);
		// Check Cut Functionality
		webBrowser.selectDomNode(node, 0);
		jspEditor.setCursorPosition(cursorPosition.x, cursorPosition.y);
		webBrowser.setFocus();
		TextEditingActionsTest.typeKeyCodeRepeately(SWT.ARROW_RIGHT, textToContain.length());
		TextEditingActionsTest.selectTextUsingKeyBoard(false, textToCutCopy.length());
		webBrowser.setFocus();
		webBrowser.clickContextMenu(node, SWTBotWebBrowser.CUT_MENU_LABEL);
		jspEditor.save();
		Assertions.assertSourceEditorContains(jspEditor.getText(), "\"" + TextEditingActionsTest.TEXT_TO_EDIT + "\"",
				TextEditingActionsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, TextEditingActionsTest.TEXT_TO_EDIT,
				TextEditingActionsTest.TEST_PAGE_NAME);
		webBrowser.selectDomNode(node, 0);
		jspEditor.setCursorPosition(cursorPosition.x, cursorPosition.y);
		webBrowser.setFocus();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_LEFT);
		webBrowser.setFocus();
		webBrowser.clickContextMenu(node, SWTBotWebBrowser.PASTE_MENU_LABEL);
		jspEditor.save();
		textToContain = textToCutCopy + TextEditingActionsTest.TEXT_TO_EDIT;
		Assertions.assertSourceEditorContains(jspEditor.getText(), textToContain,
				TextEditingActionsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, textToContain, TextEditingActionsTest.TEST_PAGE_NAME);
		assertProbelmsViewNoErrorsForPage(TextEditingActionsTest.TEST_PAGE_NAME);
	}

	/**
	 * Selects text from current cursor position and selectionLength length.
	 * Selection direction is specified by forward parameter
	 * 
	 * @param forward
	 * @param selectionLength
	 */
	private static void selectTextUsingKeyBoard(boolean forward, int selectionLength) {
		int arrowCode = forward ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
		for (int index = 0; index < selectionLength; index++) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, arrowCode);
		}
	}

	/***
	 * Simulate typing of key with keyCode repeating numOfRepeats times
	 * 
	 * @param keyCode
	 * @param numOfRepeats
	 * @param modifiers
	 */
	private static void typeKeyCodeRepeately(int keyCode, int numOfRepeats) {
		for (int index = 0; index < numOfRepeats; index++) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(keyCode);
		}
	}
}
