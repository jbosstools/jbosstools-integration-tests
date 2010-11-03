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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
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
  
  private SWTBotEclipseEditor jspTextEditor;
  private SWTBotWebBrowser webBrowser;
  
	public EditingActionsTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	protected void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(EditingActionsTest.TEST_PAGE_NAME);
    jspTextEditor = botExt.editorByTitle(EditingActionsTest.TEST_PAGE_NAME).toTextEditor();
    webBrowser = new SWTBotWebBrowser(EditingActionsTest.TEST_PAGE_NAME,botExt);	  
    
	}
	/**
	 * Insert Tag After Selected Tag
	 */
	public void testCutCopyPasteUndo(){
	  
	  nsIDOMNode node = initJspPageBeforeInserting(EditingActionsTest.PAGE_TEXT, "INPUT");
    // Check Copy Functionality
	  webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.COPY_MENU_LABEL);
    jspTextEditor.setFocus();
    jspTextEditor.selectRange(4,18,1);
    jspTextEditor.insertText(4,18,"");
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.PASTE_MENU_LABEL);
    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    assertSourceEditorContains(stripHTMLSourceText(jspTextEditor.getText()), 
        "<h:inputText/><h:inputText/><h:outputTextvalue=\"outputText\"/><h:inputText/>", 
        EditingActionsTest.TEST_PAGE);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 6, EditingActionsTest.TEST_PAGE);
    assertProbelmsViewNoErrors(botExt);      
    // Check Cut Functionality
    node = webBrowser.getDomNodeByTagName("INPUT",0);
    webBrowser.selectDomNode(node, 0);
    botExt.sleep(Timing.time1S());
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.CUT_MENU_LABEL);
    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    assertSourceEditorContains(stripHTMLSourceText(jspTextEditor.getText()), 
        "<body><h:inputText/><h:outputTextvalue=\"outputText\"/><h:inputText/><rich:comboBox>", 
        EditingActionsTest.TEST_PAGE);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 5, EditingActionsTest.TEST_PAGE);
    assertProbelmsViewNoErrors(botExt);      
    // Check Paste Functionality
    node = webBrowser.getDomNodeByTagName("INPUT",1);
    jspTextEditor.setFocus();
    jspTextEditor.selectRange(6,18,1);
    jspTextEditor.insertText(6,18,"");
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.PASTE_MENU_LABEL);
    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    assertSourceEditorContains(stripHTMLSourceText(jspTextEditor.getText()), 
        "<h:inputText/><h:outputTextvalue=\"outputText\"/><h:inputText/><h:inputText/>", 
        EditingActionsTest.TEST_PAGE);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 6, EditingActionsTest.TEST_PAGE);
    assertProbelmsViewNoErrors(botExt);
    // Check Undo Functionality
    webBrowser.setFocus();
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);
    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    assertSourceEditorContains(stripHTMLSourceText(jspTextEditor.getText()), 
        "<h:outputTextvalue=\"outputText\"/><h:inputText/><rich:comboBox>", 
        EditingActionsTest.TEST_PAGE);
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);
    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    assertSourceEditorContains(stripHTMLSourceText(jspTextEditor.getText()), 
        "<h:inputText/><h:inputText/><h:outputTextvalue=\"outputText\"/>", 
        EditingActionsTest.TEST_PAGE);KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);
    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    assertSourceEditorContains(stripHTMLSourceText(jspTextEditor.getText()), 
        "<body><h:inputText/><h:outputTextvalue=\"outputText\"/>", 
        EditingActionsTest.TEST_PAGE);
    // Check Delete Functionality
    webBrowser.setFocus();
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT",2),0);
    botExt.sleep(Timing.time1S());
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_DELETE);
    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    assertSourceEditorContains(stripHTMLSourceText(jspTextEditor.getText()), 
        "<body><h:inputText/><h:outputTextvalue=\"outputText\"/><h:inputText/></body>", 
        EditingActionsTest.TEST_PAGE);
    assertVisualEditorContainsManyNodes(webBrowser, "INPUT", 2, EditingActionsTest.TEST_PAGE);
	}
	/**
	 * Inits JSP Page before Tag will be inserted
	 */
  private nsIDOMNode initJspPageBeforeInserting(String pageText , String nodeText) {
    
    jspTextEditor.setText(pageText);
    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    nsIDOMNode node = webBrowser.getDomNodeByTagName(nodeText, 0);
    webBrowser.selectDomNode(node, 0);
    botExt.sleep(Timing.time1S());
    
    return node;

  }

	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  @Override
  protected void tearDown() throws Exception {
    jspTextEditor.close();
    super.tearDown();
  } 
}
