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
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import java.awt.event.KeyEvent;

import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarToggleButton;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMRange;
/**
 * Tests VPE Toolbar Buttons  
 * @author vlado pakan
 *
 */
public class ToolbarTextFormattingTest extends VPEAutoTestCase {
  private static final String TEXT_TEST_EXPRESSION = "Test String Expression";
  private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" + 
    "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + 
    "<html>\n" + 
    "<head>\n" +
    "<title></title>\n" + 
    "</head>\n" + 
    "<body>\n" + 
    "<f:view>\n" + 
    ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION + "\n" + 
    "</f:view>\n" + 
    "</body>\n" + 
    "</html>";
  
  private static final String TEST_PAGE_NAME = "ToolbarTest.jsp";
  
  private SWTBotExt botExt = null;
    
  public ToolbarTextFormattingTest() {
    super();
    botExt = new SWTBotExt();
  }
  
	public void testToolbarTextFormatting(){
	  createJspPage(ToolbarTextFormattingTest.TEST_PAGE_NAME);
	  final SWTBotEclipseEditor jspTextEditor = botExt.editorByTitle(ToolbarTextFormattingTest.TEST_PAGE_NAME)
      .toTextEditor();
	  jspTextEditor.setText(ToolbarTextFormattingTest.PAGE_TEXT);
	  jspTextEditor.save();
	  jspTextEditor.selectRange(8, 3, 0);
	  SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(ToolbarTextFormattingTest.TEST_PAGE_NAME,botExt);
	  // Selects text using right arrow and shift
	  KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_SHIFT);
	  KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
	  KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
	  KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
	  KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_SHIFT);
    bot.sleep(Timing.time2S());
	  nsIDOMRange firstRange = webBrowser.getSelection().getRangeAt(0);
	  int webBrowserSelectionSize = firstRange.getEndOffset() - firstRange.getStartOffset();
	  assertTrue("Selection within Visual Editor has to contain 3 characters but it contains " +
	      webBrowserSelectionSize + " characters.",
      webBrowserSelectionSize == 3);
	  jspTextEditor.selectRange(8, 10, 0);
  	// Selects text using left arrow and shift
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_SHIFT);
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_LEFT);
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_LEFT);
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_LEFT);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_SHIFT);
    bot.sleep(Timing.time2S());
    firstRange = webBrowser.getSelection().getRangeAt(0);
    assertTrue("Selection within Visual Editor has to contain 3 characters but it contains " +
        webBrowserSelectionSize + " characters.",
      webBrowserSelectionSize == 3);
    jspTextEditor.selectRange(8, 10, 0);
	  SWTBot editorBot = jspTextEditor.bot();
	  SWTBotToolbarToggleButton boldButton = editorBot.toolbarToggleButtonWithTooltip(IDELabel.VisualPageEditor.BOLD_TOOLBAR_BUTTON_LABEL);
	  SWTBotToolbarToggleButton italicButton = editorBot.toolbarToggleButtonWithTooltip(IDELabel.VisualPageEditor.ITALIC_TOOLBAR_BUTTON_LABEL);
	  SWTBotToolbarToggleButton underlinedButton = editorBot.toolbarToggleButtonWithTooltip(IDELabel.VisualPageEditor.UNDERLINE_TOOLBAR_BUTTON_LABEL);
	  // Check if Text Format Buttons buttons are enabled within VPE Editor
	  assertToolbarButtonIsEnabled(boldButton);
	  assertToolbarButtonIsEnabled(italicButton);
	  assertToolbarButtonIsEnabled(underlinedButton);
	  boldButton.click();
	  jspTextEditor.save();
	  assertTagIsInserted("b", jspTextEditor.getTextOnCurrentLine(), ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION);
	  italicButton.click();
    jspTextEditor.save();
    assertTagIsInserted("i", jspTextEditor.getTextOnCurrentLine(), ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION, "b");   
	  underlinedButton.click();
    jspTextEditor.save();
    assertTagIsInserted("u", jspTextEditor.getTextOnCurrentLine(), ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION ,"b","i");
    bot.sleep(Timing.time2S());
    // Check if Text Format Buttons buttons are toggled
    assertToggleToolbarSelection(true, boldButton);
    assertToggleToolbarSelection(true, italicButton);
    assertToggleToolbarSelection(true, underlinedButton);
    underlinedButton.click();
    jspTextEditor.save();
    assertTagIsInserted("i", jspTextEditor.getTextOnCurrentLine(), ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION, "b");   
    italicButton.click();
    jspTextEditor.save();
    assertTagIsInserted("b", jspTextEditor.getTextOnCurrentLine(), ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION);
    boldButton.click();
    jspTextEditor.save();
    String currentLine = jspTextEditor.getTextOnCurrentLine();
    assertTrue("Selected line has text: '" + currentLine + "'.\n" +
        "Expected text is: '" + currentLine.equals(ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION) + "'.",
      currentLine.equals(ToolbarTextFormattingTest.TEXT_TEST_EXPRESSION));
    // Check if Text Format Buttons buttons are toggled
    assertToggleToolbarSelection(false, boldButton);
    assertToggleToolbarSelection(false, italicButton);
    assertToggleToolbarSelection(false, underlinedButton);
    jspTextEditor.close();
	}
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
	/**
	 * Asserts if tag is correctly inserted to lineText
	 * @param tag
	 * @param lineText
	 * @param expressionString - string expression which has to be inserted inside tag
	 * @param previousTags previous tags added to expressionString
	 */
	private void assertTagIsInserted (String tag , String lineText, String expressionString, String... previousTags){
	  StringBuffer sb = new StringBuffer("");
	  StringBuilder sbSuffix = new StringBuilder("");
	  if (previousTags != null){
	    for (String previousTag : previousTags){
	      sb.append("<");
	      sb.append(previousTag);
	      sb.append(">");
	      sbSuffix.insert(0,">");
	      sbSuffix.insert(0,previousTag);
	      sbSuffix.insert(0,"</");
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
	  assertTrue ("Selected line has text: '" + lineText + "'.\n" +
	      "Expected text is: '" + expectedLineText + "'.",
	    lineText.equals(expectedLineText ));
	}
	 /**
   * Asserts if Toolbar Button is enabled
   * @param toolbarButton
   */
  private void assertToolbarButtonIsEnabled (SWTBotToolbarButton toolbarButton){
    assertTrue (toolbarButton.getToolTipText() + " Toolbar Button has to be enabled but it is not.",
      toolbarButton.isEnabled());
  }
  /**
   * Asserts Toolbar Button selection
   * @param expectedSelection
   * @param toolbarButton
   */
  private void assertToggleToolbarSelection (final boolean expectedSelection,final SWTBotToolbarToggleButton toolbarButton){
    final ToolItem toolItem = toolbarButton.widget;
    boolean isSelected = UIThreadRunnable.syncExec(new Result<Boolean>() {
      public Boolean run() {
        return toolItem.getSelection();
      }
    });
    assertTrue (toolbarButton.getToolTipText() + 
        " Toolbar Button has " + (expectedSelection ? "" : "not ") +
        "to be toggled but it is"  + (isSelected ? "" : " not") + ".",
      isSelected == expectedSelection);
  }

}
