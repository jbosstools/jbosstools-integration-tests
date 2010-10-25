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
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMNode;
/**
 * Tests JSP file Insert Actions  
 * @author vlado pakan
 *
 */
public class InsertActionsTest extends VPEEditorTestCase {
  
  private SWTBotExt botExt = null;
  
  private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
    "<html>\n" +
    "  <body>\n" +
    "  plain text inserted\n" +
    "    <h:outputText value=\"Studio\" />\n" +
    "    <h:inputText/>\n" + 
    "  </body>\n" +
    "</html>";

  private static final String TEST_PAGE_NAME = "InsertActionsTest.jsp";
  
  private SWTBotEclipseEditor jspTextEditor;
  private SWTBotWebBrowser webBrowser;
  
	public InsertActionsTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	protected void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(InsertActionsTest.TEST_PAGE_NAME);
    jspTextEditor = botExt.editorByTitle(InsertActionsTest.TEST_PAGE_NAME).toTextEditor();
    webBrowser = new SWTBotWebBrowser(InsertActionsTest.TEST_PAGE_NAME,botExt);	  
    
	}
	/**
	 * Insert Tag After Selected Tag
	 */
	public void testInsertTagAfter(){
	  
	  nsIDOMNode node = initJspPageBeforeInserting(InsertActionsTest.PAGE_TEXT, "INPUT");
	  
	  webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL,
        SWTBotWebBrowser.JBOSS_MENU_LABEL, SWTBotWebBrowser.RICH_FACES_MENU_LABEL,
        SWTBotWebBrowser.RICH_CALENDAR_TAG_MENU_LABEL);

    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    String editorText = jspTextEditor.getText();
    // Check if tag <rich:calendar> was properly added
    assertTrue("File " + TEST_PAGE
        + " has to contain string '<h:inputText/><rich:calendar>' but it doesn't.\nIt contains: " + editorText, 
        editorText.contains("<h:inputText/><rich:calendar>"));
 
    assertTrue("Visual Representation of file " + TEST_PAGE
        + " has to contain rich:calendar but it doesn't",
        webBrowser.containsNodeWithNameAndAttributes(webBrowser.getMozillaEditor().getDomDocument(), "SPAN",
            new String[]{"title"},new String[] {"rich:calendar"}));
    
    assertProbelmsViewNoErrors(botExt);    
    
	}
	 /**
   * Insert Tag Before Selected Tag
   */
  public void testInsertTagBefore(){
    
    nsIDOMNode node = initJspPageBeforeInserting(InsertActionsTest.PAGE_TEXT,"INPUT");
    
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.INSERT_BEFORE_MENU_LABEL,
        SWTBotWebBrowser.JBOSS_MENU_LABEL, SWTBotWebBrowser.RICH_FACES_MENU_LABEL,
        SWTBotWebBrowser.RICH_CALENDAR_TAG_MENU_LABEL);

    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    String trimedEditorText = VPEEditorTestCase.stripHTMLSourceText(jspTextEditor.getText());
    // Check if tag <rich:calendar> was properly added
    assertTrue("File " + TEST_PAGE
        + " has to contain string '<rich:calendar></rich:calendar><h:inputText/>' but it doesn't.\nIt contains: " + trimedEditorText,
        trimedEditorText.contains("<rich:calendar></rich:calendar><h:inputText/>"));
 
    assertTrue("Visual Representation of file " + TEST_PAGE
        + " has to contain rich:calendar but it doesn't",
        webBrowser.containsNodeWithNameAndAttributes(webBrowser.getMozillaEditor().getDomDocument(), "SPAN",
            new String[]{"title"},new String[] {"rich:calendar"}));
    
    assertProbelmsViewNoErrors(botExt);
    
  }

  /**
   * Insert Tag Into Selected Tag
   */
  public void testInsertTagInto(){
    
    nsIDOMNode node = initJspPageBeforeInserting("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<html>\n" +
        "  <body>\n" +
        "  plain text inserted\n" +
        "    <form>\n" +
        "    </form>\n" + 
        "  </body>\n" +
        "</html>",
        "FORM");
    
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.INSERT_INTO_MENU_LABEL,
        SWTBotWebBrowser.JBOSS_MENU_LABEL, SWTBotWebBrowser.RICH_FACES_MENU_LABEL,
        SWTBotWebBrowser.RICH_CALENDAR_TAG_MENU_LABEL);

    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    String trimedEditorText = VPEEditorTestCase.stripHTMLSourceText(jspTextEditor.getText());
    // Check if tag <rich:calendar> was properly added
    assertTrue("File " + TEST_PAGE
        + " has to contain string '<form><rich:calendar></rich:calendar></form>' but it doesn't.\nIt contains: " + trimedEditorText,
        trimedEditorText.contains("<form><rich:calendar></rich:calendar></form>"));
 
    assertTrue("Visual Representation of file " + TEST_PAGE
        + " has to contain rich:calendar but it doesn't",
        webBrowser.containsNodeWithNameAndAttributes(webBrowser.getMozillaEditor().getDomDocument(), "FORM"));
    
    assertProbelmsViewNoErrors(botExt);
    
  }
  
  /**
   * Insert Tag Around Selected Tag
   */
  public void testInsertTagAround(){
    
    nsIDOMNode node = initJspPageBeforeInserting(InsertActionsTest.PAGE_TEXT, "INPUT");
    
    webBrowser.clickContextMenu(node,
        SWTBotWebBrowser.INSERT_AROUND_MENU_LABEL,
        SWTBotWebBrowser.JSF_MENU_LABEL, SWTBotWebBrowser.HTML_MENU_LABEL,
        SWTBotWebBrowser.H_FORM_TAG_MENU_LABEL);

    jspTextEditor.save();
    botExt.sleep(Timing.time3S());
    String trimedEditorText = VPEEditorTestCase.stripHTMLSourceText(jspTextEditor.getText());
    // Check if tag <h:form> was properly added
    assertTrue("File " + TEST_PAGE
        + " has to contain string '<h:form><h:inputText/></h:form>' but it doesn't.\nIt contains: " + trimedEditorText,
        trimedEditorText.contains("<h:form><h:inputText/></h:form>"));
 
    assertTrue("Visual Representation of file " + TEST_PAGE
        + " has to contain h:form but it doesn't",
        webBrowser.containsNodeWithNameAndAttributes(webBrowser.getMozillaEditor().getDomDocument(), "FORM"));
    
    assertProbelmsViewNoErrors(botExt);
    
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
  
}
