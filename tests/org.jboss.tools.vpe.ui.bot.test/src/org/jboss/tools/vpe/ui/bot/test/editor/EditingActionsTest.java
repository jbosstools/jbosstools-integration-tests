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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMNode;
/**
 * Tests JSP file Cut, Copy, Paste, Undo and Delete actions through Visual Editor Menu  
 * @author vlado pakan
 *
 */
public class EditingActionsTest extends VPEEditorTestCase {
  
  private SWTBotExt botExt = null;
  
  private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
    "<%@ taglib uri=\"http://richfaces.org/rich\" prefix=\"rich\"%>\n" +
    "<html>\n" +
    "  <body>\n" +
    "    <h:inputText/>\n" + 
    "    <h:outputText value = \"outputText\"/>\n" +
    "    <h:inputText/>\n" + 
    "    <rich:comboBox></rich:comboBox>\n" +
    "  </body>\n" +
    "</html>";

  private static final String TEST_PAGE_NAME = "EditingActionsTest.jsp";
  
  private SWTBotEclipseEditor jspEditor;
  private SWTBotWebBrowser webBrowser;
  
	public EditingActionsTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	public void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(EditingActionsTest.TEST_PAGE_NAME);
    jspEditor = botExt.editorByTitle(EditingActionsTest.TEST_PAGE_NAME).toTextEditor();
    webBrowser = new SWTBotWebBrowser(EditingActionsTest.TEST_PAGE_NAME,botExt);	  
    
	}
	/**
	 * Tests Cut Copy Paste Operations
	 */
	public void testCutCopyPasteUndo(){
	  
	  nsIDOMNode node = initJspPageBeforeInserting(EditingActionsTest.PAGE_TEXT, "INPUT");
    // Check Copy Functionality
	  webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.COPY_MENU_LABEL);
    jspEditor.setFocus();
    jspEditor.selectRange(4,18,1);
    jspEditor.insertText(4,18,"");
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.PASTE_MENU_LABEL);
    jspEditor.save();
    botExt.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "<h:inputText/><h:inputText/><h:outputTextvalue=\"outputText\"/><h:inputText/>", 
        EditingActionsTest.TEST_PAGE);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 6, EditingActionsTest.TEST_PAGE_NAME);
    assertProbelmsViewNoErrors(botExt);      
    // Check Cut Functionality
    node = webBrowser.getDomNodeByTagName("INPUT",0);
    webBrowser.selectDomNode(node, 0);
    botExt.sleep(Timing.time1S());
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.CUT_MENU_LABEL);
    jspEditor.save();
    botExt.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "<body><h:inputText/><h:outputTextvalue=\"outputText\"/><h:inputText/><rich:comboBox>", 
        EditingActionsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 5, EditingActionsTest.TEST_PAGE_NAME);
    assertProbelmsViewNoErrors(botExt);      
    // Check Paste Functionality
    node = webBrowser.getDomNodeByTagName("INPUT",1);
    jspEditor.setFocus();
    jspEditor.selectRange(6,18,1);
    jspEditor.insertText(6,18,"");
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.PASTE_MENU_LABEL);
    jspEditor.save();
    botExt.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "<h:inputText/><h:outputTextvalue=\"outputText\"/><h:inputText/><h:inputText/>", 
        EditingActionsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 6, EditingActionsTest.TEST_PAGE_NAME);
    assertProbelmsViewNoErrors(botExt);
    // Check Undo Functionality
    webBrowser.setFocus();
    if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND, 'z'); 
    }
    else{
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);  
    }    
    jspEditor.save();
    botExt.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "<h:outputTextvalue=\"outputText\"/><h:inputText/><rich:comboBox>", 
        EditingActionsTest.TEST_PAGE_NAME);
    if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND, 'z'); 
    }
    else{
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);
    }
    jspEditor.save();
    botExt.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "<h:inputText/><h:inputText/><h:outputTextvalue=\"outputText\"/>", 
        EditingActionsTest.TEST_PAGE_NAME);
    if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND, 'z'); 
    }
    else{
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);
    }
    jspEditor.save();
    botExt.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "<body><h:inputText/><h:outputTextvalue=\"outputText\"/>", 
        EditingActionsTest.TEST_PAGE_NAME);
    // Check Delete Functionality
    webBrowser.setFocus();
    botExt.sleep(Timing.time2S());
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT",2),0);
    botExt.sleep(Timing.time2S());
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_DELETE);
    jspEditor.save();
    botExt.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "<body><h:inputText/><h:outputTextvalue=\"outputText\"/><h:inputText/></body>", 
        EditingActionsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 2, EditingActionsTest.TEST_PAGE_NAME);
	}
	/**
	 * Inits JSP Page before Tag will be inserted
	 */
  private nsIDOMNode initJspPageBeforeInserting(String pageText , String nodeText) {
    
    jspEditor.show();
    jspEditor.setText(pageText);
    jspEditor.save();
    botExt.sleep(Timing.time3S());
    nsIDOMNode node = webBrowser.getDomNodeByTagName(nodeText, 0);
    botExt.sleep(Timing.time1S());
    webBrowser.setFocus();
    botExt.sleep(Timing.time1S());
    webBrowser.selectDomNode(node, 0);
    botExt.sleep(Timing.time1S());
    
    return node;

  }
  /**
   * Check Replace with Functionality
   */
  public void testReplaceWith(){
    nsIDOMNode node = initJspPageBeforeInserting("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<html>\n" +
        "  <body>\n" +
        "    <h:inputText/>\n" + 
        "  </body>\n" +
        "</html>",
        "INPUT");
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.REPLACE_WITH_MENU_LABEL,
        SWTBotWebBrowser.JSF_MENU_LABEL,
        SWTBotWebBrowser.HTML_MENU_LABEL,
        SWTBotWebBrowser.H_OUTPUT_TEXT_TAG_MENU_LABEL);
    jspEditor.save();
    bot.sleep(Timing.time2S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "h:outputText", 
        EditingActionsTest.TEST_PAGE_NAME);
    Assertions.assertSourceEditorNotContain(stripHTMLSourceText(jspEditor.getText()), 
        "h:inputText", 
        EditingActionsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsManyNodes(webBrowser, "SPAN", 1, EditingActionsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 0, EditingActionsTest.TEST_PAGE_NAME);
  }    

  public void testSetupTemplateFor(){
    final String unknownTag = "h:text";
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\"%>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" +
        "<html>\n" +
        "  <head>\n" +
        "    <title>Input User Name Page</title>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <" + unknownTag + " value=\"Text to edit\"/>\n" +
        "    </f:view>\n" +
        "  </body>\n" +
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time2S());
    assertVisualEditorContainsNodeWithValue(webBrowser, "h:text", EditingActionsTest.TEST_PAGE_NAME);
    nsIDOMNode node = webBrowser.getDomNodeByTagName("DIV", 4);
    bot.sleep(Timing.time2S());
    webBrowser.selectDomNode(node, 0);
    bot.sleep(Timing.time2S());
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.SETUP_VISUAL_TEMPLATE_FOR_MENU_LABEL + "<" + unknownTag + ">...");
    // Test if window for Tag Template definition was properly opened
    WidgetNotFoundException wnfe = null;
    try{
      bot.shell(IDELabel.Shell.USER_SPECIFIED_TAG_TEMPLATE).activate();
      bot.textWithLabel(IDELabel.UserSpecifiedTagTemplateDialog.TAG_FOR_DISPLAY).setText("tt");
      bot.button(IDELabel.Button.OK).click();
    } catch (WidgetNotFoundException wnfetmp){
        wnfe = wnfetmp;
    }
    assertNull("Dialog for User Specified Tag Template does not work properly" + wnfe,wnfe);
  }  
  /**
   * Tests Parent Tag Menu
   */
  public void testParentTagMenu(){
    
    nsIDOMNode node = initJspPageBeforeInserting("<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\"%>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" +
        "<html>\n" +
        "  <head>\n" +
        "    <title>Input User Name Page</title>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:inputText value=\"Text to edit\"/>\n" +
        "    </f:view>\n" +
        "  </body>\n" +
        "</html>", "INPUT");
    webBrowser.selectDomNode(node, 0);
    final String parentTagFViewLabel = SWTBotWebBrowser.PARENT_TAG_MENU_LABEL + " (f:view)";
    final Menu topMenu = webBrowser.getTopMenu(node, parentTagFViewLabel);
        
    UIThreadRunnable.syncExec(new VoidResult() {
      public void run() {
        ContextMenuHelper.clickContextMenu(topMenu, parentTagFViewLabel);
        // Parent Tag f:view check
        MenuItem parentTagFViewMenuItem = ContextMenuHelper.getContextMenu(topMenu, parentTagFViewLabel, false);
        Menu parentTagFViewMenu = ContextMenuHelper.showMenuOfMenuItem(parentTagFViewMenuItem);
        // Parent Tag Body check
        final String parentTagBodyLabel = SWTBotWebBrowser.PARENT_TAG_MENU_LABEL + " (body)";
        MenuItem parentTagBodyMenuItem = ContextMenuHelper.getContextMenu(parentTagFViewMenu, parentTagBodyLabel, false);
        Menu parentTagBodyMenu = ContextMenuHelper.showMenuOfMenuItem(parentTagBodyMenuItem);
        // Parent Tag HTML check
        final String parentTagHTMLLabel = SWTBotWebBrowser.PARENT_TAG_MENU_LABEL + " (html)";
        MenuItem parentTagHTMLMenuItem = ContextMenuHelper.getContextMenu(parentTagBodyMenu, parentTagHTMLLabel, false);
        Menu parentTagHTMLMenu = ContextMenuHelper.showMenuOfMenuItem(parentTagHTMLMenuItem);
        // There should by no parent tag anymore
        String[] menuItemLabels = ContextMenuHelper.getMenuItemLabels(parentTagHTMLMenu);
        String menuItemLabelStartingWithParentTag = null;
        int index = 0;
        while (index < menuItemLabels.length && menuItemLabelStartingWithParentTag == null){
          if (menuItemLabels[index].startsWith(SWTBotWebBrowser.PARENT_TAG_MENU_LABEL)){
            menuItemLabelStartingWithParentTag = menuItemLabels[index];
          }
          else{
            index++;
          }
        }
        ContextMenuHelper.hideMenuNonRecursively(parentTagHTMLMenu);
        ContextMenuHelper.hideMenuNonRecursively(parentTagBodyMenu);
        ContextMenuHelper.hideMenuNonRecursively(parentTagFViewMenu);
        ContextMenuHelper.hideMenuNonRecursively(topMenu);
        assertNull("There has to be no menu item in HTML tag submenu starting with " + SWTBotWebBrowser.PARENT_TAG_MENU_LABEL +
            "\nbut there is this one: " + menuItemLabelStartingWithParentTag,
            menuItemLabelStartingWithParentTag);
      }
    });
    
  }  
    
  /**
   * Tests Strip Tag Menu
   */
  public void testStripTagMenu(){
    
    nsIDOMNode node = initJspPageBeforeInserting("<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\"%>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" +
        "<html>\n" +
        "  <head>\n" +
        "    <title>Input User Name Page</title>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:inputText value=\"Text to edit\"/>\n" +
        "    </f:view>\n" +
        "  </body>\n" +
        "</html>", "INPUT");
    jspEditor.save();
    bot.sleep(Timing.time2S());
    webBrowser.selectDomNode(node, 0);
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.PARENT_TAG_MENU_LABEL + " (f:view)",
        SWTBotWebBrowser.STRIP_TAG_MENU_LABEL);
    jspEditor.save();
    bot.sleep(Timing.time2S());
    Assertions.assertSourceEditorNotContain(jspEditor.getText(), "<f:view>", EditingActionsTest.TEST_PAGE_NAME);
    // Undo Changes
    webBrowser.setFocus();
    if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND, 'z'); 
    }
    else{
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);
    }
    jspEditor.save();
    bot.sleep(Timing.time2S());
    Assertions.assertSourceEditorContains(jspEditor.getText(), "<f:view>", EditingActionsTest.TEST_PAGE_NAME);
  }  
  /**
   * Tests Select This Tag Menu
   */
  public void testSelectThisTagMenu(){
    
    nsIDOMNode node = initJspPageBeforeInserting("<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\"%>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" +
        "<html>\n" +
        "  <head>\n" +
        "    <title>Input User Name Page</title>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:inputText value=\"Text to edit\"/>\n" +
        "    </f:view>\n" +
        "  </body>\n" +
        "</html>", "INPUT");
    jspEditor.save();
    bot.sleep(Timing.time2S());
    webBrowser.selectDomNode(node, 0);
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.PARENT_TAG_MENU_LABEL + " (f:view)",
        SWTBotWebBrowser.SELECT_THIS_TAG_MENU_LABEL);
    jspEditor.save();
    bot.sleep(Timing.time2S());
    Assertions.assertSourceEditorContains(stripHTMLSourceText(jspEditor.getText()), 
        "<f:view><h:inputText",
        EditingActionsTest.TEST_PAGE_NAME);
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
