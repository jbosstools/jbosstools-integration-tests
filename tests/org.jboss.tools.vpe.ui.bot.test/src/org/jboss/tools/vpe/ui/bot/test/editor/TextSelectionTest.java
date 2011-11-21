/*******************************************************************************

 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

import java.awt.event.KeyEvent;

import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests JSP file Editing and Cut, Copy, Paste actions through Visual Editor Menu for Text selection  
 * @author vlado pakan
 *
 */
public class TextSelectionTest extends VPEEditorTestCase {
  
  private SWTBotExt botExt = null;
  
  private static final String TEXT_TO_SELECT = "select";
  private static final String TEXT_TO_TEST = "Text " + TextSelectionTest.TEXT_TO_SELECT +" test";
  private static final int SELECTION_START_LINE = 8;
  private static final int SELECTION_START_COLUMN = 32;
  
  private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\"%>\n" +
    "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" +
    "<html>\n" +
    "  <head>\n" +
    "    <title>Input User Name Page</title>\n" +
    "  </head>\n" + 
    "  <body>\n" +
    "    <f:view>\n" +
    "      <h:outputText value=\"" + TextSelectionTest.TEXT_TO_TEST + "\"/>\n" +
    "    </f:view>\n" +
    "  </body>\n" +
    "</html>";
      
  private static final String TEST_PAGE_NAME = "TextSelectionTest.jsp";
  
  private SWTBotEditorExt jspEditor;
  private SWTBotWebBrowser webBrowser;
  
	public TextSelectionTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	public void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(TextSelectionTest.TEST_PAGE_NAME);
    jspEditor = botExt.swtBotEditorExtByTitle(TextSelectionTest.TEST_PAGE_NAME);
    webBrowser = new SWTBotWebBrowser(TextSelectionTest.TEST_PAGE_NAME,botExt);
	}
	/**
   * Tests Text Selection
   */
  public void testTextSelection(){
    
    jspEditor.setText(TextSelectionTest.PAGE_TEXT);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    // select text in Source Pane
    jspEditor.selectRange(TextSelectionTest.SELECTION_START_LINE, TextSelectionTest.SELECTION_START_COLUMN, TextSelectionTest.TEXT_TO_SELECT.length());
    webBrowser.setFocus();
    bot.sleep(Timing.time3S());
    String selectedText = webBrowser.getSelectionText();
    // check selected text in Visual Pane
    assertTrue("Selected text in Visual Pane has to be '" + TextSelectionTest.TEXT_TO_SELECT + "'" +
        "\nBut is '" + selectedText+  "'",
      TextSelectionTest.TEXT_TO_SELECT.equals(selectedText));
    String newText = "newText";
    // Type text replacing selected text in Visual Pane and check Source Pane content
    KeyboardHelper.typeBasicStringUsingAWT(newText);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(jspEditor.getText(), " " + newText + " ", TextSelectionTest.TEST_PAGE_NAME);
    jspEditor.setText(TextSelectionTest.PAGE_TEXT);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    // select text in Visual Page
    jspEditor.deselectAndSetCursorPosition(TextSelectionTest.SELECTION_START_LINE, TextSelectionTest.SELECTION_START_COLUMN);
    webBrowser.setFocus();
    bot.sleep(Timing.time1S());
    KeyboardHelper.typeKeyCodeUsingAWTRepeately(KeyEvent.VK_RIGHT, 5);
    KeyboardHelper.typeKeyCodeUsingAWTRepeately(KeyEvent.VK_RIGHT, TextSelectionTest.TEXT_TO_SELECT.length(),KeyEvent.VK_SHIFT);
    bot.sleep(Timing.time1S());
    // check selected text in Source Pane
    selectedText = jspEditor.getSelection();
    assertTrue("Selected text in Source Pane has to be '" + TextSelectionTest.TEXT_TO_SELECT + "'" +
        "\nBut is '" + selectedText+  "'",
      TextSelectionTest.TEXT_TO_SELECT.equals(selectedText));
    jspEditor.setFocus();
    // type text replacing selected text in Source Pane and check Visual Pane content
    KeyboardHelper.typeBasicStringUsingAWT(newText);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        TextSelectionTest.TEXT_TO_TEST.replaceFirst(TextSelectionTest.TEXT_TO_SELECT, newText),
        TextSelectionTest.TEST_PAGE_NAME);
    // check if selection made on Source tab appears on Visual/Source tab Visual Pane 
    jspEditor.setText(TextSelectionTest.PAGE_TEXT);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    SWTBotEditorExt botEditorExt = new SWTBotExt().swtBotEditorExtByTitle(TextSelectionTest.TEST_PAGE_NAME);
    botEditorExt.selectPage(IDELabel.VisualPageEditor.SOURCE_TAB_LABEL);
    jspEditor.selectRange(TextSelectionTest.SELECTION_START_LINE, TextSelectionTest.SELECTION_START_COLUMN, TextSelectionTest.TEXT_TO_SELECT.length());
    botEditorExt.selectPage(IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);
    webBrowser.setFocus();
    bot.sleep(Timing.time3S());
    selectedText = webBrowser.getSelectionText();
    // check selected text in Visual Pane
    assertTrue("Selected text in Visual Pane has to be '" + TextSelectionTest.TEXT_TO_SELECT + "'" +
        "\nBut is '" + selectedText+  "'",
      TextSelectionTest.TEXT_TO_SELECT.equals(selectedText));
  }

  /**
   * Tests Text Selection of Special Symbols
   */
  public void testTextSelectionOfSpecialSymbols(){
    
    jspEditor.setText("&copy;2007 Exposure. All Rights Reserved." +
        "&bull;" +  
        "Design by <a href=\"http://www.freecsstemplates.org/\">Free CSS Templates</a>" +
        "&bull;" +  
        "Icons by <a href=\"http://famfamfam.com/\">FAMFAMFAM</a>.");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        "©2007 Exposure. All Rights Reserved.•Design by",
        TextSelectionTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        "•Icons by",
        TextSelectionTest.TEST_PAGE_NAME);
    jspEditor.deselectAndSetCursorPosition(0, 0);
    webBrowser.setFocus();
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_RIGHT,KeyEvent.VK_SHIFT);
    String selectedText = jspEditor.getSelection();
    String copySymbol = "&copy;";
    assertTrue("Selected text in Source Pane has to be '" +  copySymbol + "'" +
        "\nBut is '" + selectedText+  "'",
        copySymbol.equals(selectedText));
    jspEditor.deselectAndSetCursorPosition(0, 0);
    webBrowser.setFocus();
    KeyboardHelper.typeKeyCodeUsingAWTRepeately(KeyEvent.VK_RIGHT, 36);
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_RIGHT,KeyEvent.VK_SHIFT);
    selectedText = jspEditor.getSelection();
    String bullSymbol = "&bull;";
    assertTrue("Selected text in Source Pane has to be '" +  bullSymbol + "'" +
        "\nBut is '" + selectedText+  "'",
        bullSymbol.equals(selectedText));

  } 

  
  @Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  @Override
  public void tearDown() throws Exception {
    jspEditor.close();
    super.tearDown();
  } 
}
