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
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests JSP file text selection
 * 
 * @author vlado pakan
 *
 */
public class TextSelectionTest extends VPEEditorTestCase {

	private static final String TEXT_TO_SELECT = "select";
	private static final String TEXT_TO_TEST = "Text " + TextSelectionTest.TEXT_TO_SELECT + " test";

	private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\"%>\n"
			+ "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" + "<html>\n" + "  <head>\n"
			+ "    <title>Input User Name Page</title>\n" + "  </head>\n" + "  <body>\n" + "    <f:view>\n"
			+ "      <h:outputText value=\"" + TextSelectionTest.TEXT_TO_TEST + "\"/>\n" + "    </f:view>\n"
			+ "  </body>\n" + "</html>";

	private static final String TEST_PAGE_NAME = "TextSelectionTest.jsp";

	private TextEditor jspEditor;
	private SWTBotWebBrowser webBrowser;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		createJspPage(TextSelectionTest.TEST_PAGE_NAME);
		openPage(TextSelectionTest.TEST_PAGE_NAME);
		jspEditor = new TextEditor(TextSelectionTest.TEST_PAGE_NAME);
		webBrowser = new SWTBotWebBrowser(TextSelectionTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests Text Selection
	 */
	@Test
	public void testTextSelection() {

		jspEditor.setText(TextSelectionTest.PAGE_TEXT);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		// select text in Source Pane
		jspEditor.selectText(TextSelectionTest.TEXT_TO_SELECT);
		webBrowser.setFocus();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		String selectedText = webBrowser.getSelectionText();
		// check selected text in Visual Pane
		assertTrue("Selected text in Visual Pane has to be '" + TextSelectionTest.TEXT_TO_SELECT + "'" + "\nBut is '"
				+ selectedText + "'", TextSelectionTest.TEXT_TO_SELECT.equals(selectedText));
		String newText = "newText";
		// Type text replacing selected text in Visual Pane and check Source
		// Pane content
		KeyboardFactory.getKeyboard().type(newText);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		Assertions.assertSourceEditorContains(jspEditor.getText(), " " + newText + " ",
				TextSelectionTest.TEST_PAGE_NAME);
		jspEditor.activate();
		jspEditor.setText(TextSelectionTest.PAGE_TEXT);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		// select text in Visual Page
		jspEditor.setCursorPosition(jspEditor.getPositionOfText(TextSelectionTest.TEXT_TO_SELECT));
		webBrowser.setFocus();
		invokeKeyCombinationRepeately(TextSelectionTest.TEXT_TO_SELECT.length(), SWT.SHIFT,SWT.ARROW_RIGHT);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		// check selected text in Source Pane
		selectedText = jspEditor.getSelectedText();
		assertTrue("Selected text in Source Pane has to be '" + TextSelectionTest.TEXT_TO_SELECT + "'" + "\nBut is '"
				+ selectedText + "'", TextSelectionTest.TEXT_TO_SELECT.equals(selectedText));
		jspEditor.activate();
		// type text replacing selected text in Source Pane and check Visual
		// Pane content
		KeyboardFactory.getKeyboard().type(newText);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContainsNodeWithValue(webBrowser,
				TextSelectionTest.TEXT_TO_TEST.replaceFirst(TextSelectionTest.TEXT_TO_SELECT, newText),
				TextSelectionTest.TEST_PAGE_NAME);
		// check if selection made on Source tab appears on Visual/Source tab
		// Visual Pane
		jspEditor.setText(TextSelectionTest.PAGE_TEXT);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		jspEditor.activate();
		new DefaultCTabItem("Source").activate();
		jspEditor.selectText(TextSelectionTest.TEXT_TO_SELECT);
		new DefaultCTabItem("Visual/Source").activate();
		webBrowser.setFocus();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		selectedText = webBrowser.getSelectionText();
		// check selected text in Visual Pane
		assertTrue("Selected text in Visual Pane has to be '" + TextSelectionTest.TEXT_TO_SELECT + "'" + "\nBut is '"
				+ selectedText + "'", TextSelectionTest.TEXT_TO_SELECT.equals(selectedText));
	}

	/**
	 * Tests Text Selection of Special Symbols
	 */
	@Test
	public void testTextSelectionOfSpecialSymbols() {

		jspEditor.setText("&copy;2007 Exposure. All Rights Reserved." + "&bull;"
				+ "Design by <a href=\"http://www.freecsstemplates.org/\">Free CSS Templates</a>" + "&bull;"
				+ "Icons by <a href=\"http://famfamfam.com/\">FAMFAMFAM</a>.");
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContainsNodeWithValue(webBrowser, "©2007 Exposure. All Rights Reserved.•Design by",
				TextSelectionTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, "•Icons by", TextSelectionTest.TEST_PAGE_NAME);
		jspEditor.setCursorPosition(0);
		webBrowser.setFocus();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT,SWT.ARROW_RIGHT);
		String selectedText = jspEditor.getSelectedText();
		String copySymbol = "&copy;";
		assertTrue("Selected text in Source Pane has to be '" + copySymbol + "'" + "\nBut is '" + selectedText + "'",
				copySymbol.equals(selectedText));
		jspEditor.setCursorPosition(0);
		webBrowser.setFocus();
		invokeKeyCombinationRepeately(36, SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT,SWT.ARROW_RIGHT);
		selectedText = jspEditor.getSelectedText();
		String bullSymbol = "&bull;";
		assertTrue("Selected text in Source Pane has to be '" + bullSymbol + "'" + "\nBut is '" + selectedText + "'",
				bullSymbol.equals(selectedText));

	}
	
	private void invokeKeyCombinationRepeately (int num, int... keyCodes){
		for (int index = 0 ; index < num ; index++){
			KeyboardFactory.getKeyboard().invokeKeyCombination(keyCodes);
		}
	}

	@Override
	public void tearDown() throws Exception {
		jspEditor.close();
		super.tearDown();
	}
}
