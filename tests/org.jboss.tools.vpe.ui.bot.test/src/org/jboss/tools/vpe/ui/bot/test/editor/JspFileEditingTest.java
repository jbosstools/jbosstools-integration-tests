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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotTableExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMNode;
/**
 * Tests JSP file editing and synchronization between Source Editor and Visual Editor  
 * @author vlado pakan
 *
 */
public class JspFileEditingTest extends VPEEditorTestCase {
  
  private SWTBotExt botExt = null;
  
	public JspFileEditingTest() {
		super();
		botExt = new SWTBotExt();
	}

	public void testJspFileEditing(){
	  
	  eclipse.maximizeActiveShell();

	  insertTagUsingContextMenu();
	  insertTagUsingPalette();
	  
    
	}
	/**
	 * Inserts tag to html page using Context Menu of Visual Editor
	 */
  private void insertTagUsingContextMenu() {
    
    openPage();
    SWTBotWebBrowser swtBotWebBrowser = new SWTBotWebBrowser(TEST_PAGE, botExt);
    nsIDOMNode node = swtBotWebBrowser.getDomNodeByTagName("INPUT", 1);
    swtBotWebBrowser.selectDomNode(node, 0);
    botExt.sleep(Timing.time2S());
    swtBotWebBrowser.clickContextMenu(node,
        SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL,
        SWTBotWebBrowser.JSF_MENU_LABEL, SWTBotWebBrowser.HTML_MENU_LABEL,
        SWTBotWebBrowser.H_OUTPUT_TEXT_TAG_MENU_LABEL);
    botExt.sleep(Timing.time2S());
    final SWTBotEclipseEditor jspTextEditor = botExt.editorByTitle(TEST_PAGE)
        .toTextEditor();
    jspTextEditor.save();
    botExt.sleep(Timing.time2S());
    // Check if tag h:outputText was properly added
    String editorText = jspTextEditor.getText();
    assertTrue("File " + TEST_PAGE
        + " has to contain string '<h:outputText/>' but it doesn't",
        editorText.contains("<h:outputText/>"));
  }
	
	/**
   * Inserts tag to html page using JBoss Tools Palette
   */
  private void insertTagUsingPalette(){
    
    openPage();
    openPalette();
    
    SWTBotWebBrowser swtBotWebBrowser = new SWTBotWebBrowser(TEST_PAGE, botExt);
    nsIDOMNode node = swtBotWebBrowser.getDomNodeByTagName("INPUT", 1);
    swtBotWebBrowser.selectDomNode(node, 0);
    botExt.sleep(Timing.time1S());
    
    swtBotWebBrowser.activatePaletteTool("outputText");
    SWTBot dialogBot = botExt.shell(IDELabel.Shell.INSERT_TAG).activate().bot();
    SWTBotTable swtBotTable = dialogBot.table();
    String outputTextValue = "123 !! Test value !! 321";
    new SWTBotTableExt(swtBotTable).setTableCellWithTextEditorText(
        outputTextValue, swtBotTable.indexOf("value"), 1, "", dialogBot);
    dialogBot.button(IDELabel.Button.FINISH).click();
    final SWTBotEclipseEditor jspTextEditor = botExt.editorByTitle(TEST_PAGE)
        .toTextEditor();
    jspTextEditor.save();
    botExt.toolbarButtonWithTooltip(IDELabel.Button.REFRESH).click();
    botExt.sleep(Timing.time1S());
    String editorText = jspTextEditor.getText();
    String testText = "<h:outputText value=\"" + outputTextValue + "\"/>";
    assertTrue("File " + TEST_PAGE + " has to contain string '" + testText
        + "' but it doesn't", editorText.contains(testText));
    // Insert text via Visual Editor to inserted h:outputText tag
    node = swtBotWebBrowser.getDomNodeByTagName(
        swtBotWebBrowser.getNsIDOMDocument(), "#text", 5);
    botExt.sleep(Timing.time2S());
    swtBotWebBrowser.selectDomNode(node, 5);
    String insertString = "ab9876CD";
    KeyboardHelper.typeBasicStringUsingAWT(insertString);
    botExt.sleep(Timing.time2S());
    jspTextEditor.save();
    editorText = jspTextEditor.getText();
    outputTextValue = outputTextValue.substring(0, 5) + insertString
        + outputTextValue.substring(5);
    testText = "<h:outputText value=\"" + outputTextValue + "\"/>";
    assertTrue("File " + TEST_PAGE + " has to contain string '" + testText
        + "' but it doesn't", editorText.contains(testText));
    jspTextEditor.close();
  }
	
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  
}
