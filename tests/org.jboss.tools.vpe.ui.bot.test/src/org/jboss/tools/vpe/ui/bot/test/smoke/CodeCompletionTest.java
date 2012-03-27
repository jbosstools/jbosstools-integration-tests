/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import java.util.LinkedList;
import java.util.List;

import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ContentAssistHelper;
import org.jboss.tools.ui.bot.ext.parts.ContentAssistBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
/**
 * Test Code Completion functionality
 * @author Vladimir Pakan
 *
 */
public class CodeCompletionTest extends VPEEditorTestCase{
  static final private String HTML_PAGE_NAME = "CodeComletionPage.html";
  static final private String XHTML_PAGE_NAME = "CodeComletionPage.xhtml";
  private SWTBotEditorExt editor;
  private SWTBotEditorExt htmlEditor;
  private SWTBotEditorExt xhtmlEditor;
  private String originalEditorText;
  @Override
  public void setUp() throws Exception {
    super.setUp();
    openPage(TEST_PAGE);
    editor = SWTTestExt.bot.swtBotEditorExtByTitle(TEST_PAGE);
    originalEditorText = editor.getText();
  }
  @Override
  public void tearDown() throws Exception {
    if (editor != null){
      editor.setText(originalEditorText);
      editor.save();
      editor.close();
    }
    if (htmlEditor != null){
      htmlEditor.save();
      htmlEditor.close();
    }
    if (xhtmlEditor != null){
      xhtmlEditor.save();
      xhtmlEditor.close();
    }
    super.tearDown();
  }
  /**
   * Tests Code Completion for JSP Page within <f:view> tag
   */
  public void testCodeCompletionOfJspPage(){
    ContentAssistBot contentAssist = editor.contentAssist();
    String textForSelection = "<f:view>";
    // Check content assist menu content
    ContentAssistHelper.checkContentAssistContent(SWTTestExt.bot, 
        TEST_PAGE,
        textForSelection, 
        textForSelection.length(), 
        0, 
        getJspPageProposalList());
    // Check content assist insertion    
    String contentAssistToUse = "h:commandButton"; 
    contentAssist.checkContentAssist(contentAssistToUse, true);
    editor.save();
    String expectedInsertedText = textForSelection + "<" + contentAssistToUse + " action=\"\" value=\"\" />";
    if (!editor.getText().contains(expectedInsertedText)){
      expectedInsertedText = textForSelection + "<" + contentAssistToUse + " value=\"\" action=\"\" />";
    }
    assertTrue("Editor has to contain text '" + expectedInsertedText + "' but it doesn't\n" +
        "Editor Text is\n" + editor.getText(),
      editor.getText().contains(expectedInsertedText));
    // Check content assist for action attribute of <h:commandButton> tag
    ContentAssistHelper.checkContentAssistContent(SWTTestExt.bot, 
        TEST_PAGE,
        textForSelection, 
        expectedInsertedText.indexOf("action=\"") + 8, 
        0, 
        getCommandButtonActionAttrProposalList());
    // Check content assist for value attribute of <h:commandButton> tag
    ContentAssistHelper.checkContentAssistContent(SWTTestExt.bot, 
        TEST_PAGE,
        textForSelection, 
        expectedInsertedText.indexOf("value=\"") + 7, 
        0, 
        getCommandButtonValueAttrProposalList());
  }
  
  /**
   * Tests Code Completion for HTML Page
   */
  public void testCodeCompletionOfHtmlPage(){
    createHtmlPage(CodeCompletionTest.HTML_PAGE_NAME);
    SWTBotExt botExt = new SWTBotExt();
    SWTBotEditorExt htmlEditor = botExt.swtBotEditorExtByTitle(CodeCompletionTest.HTML_PAGE_NAME);
    htmlEditor.setText("<!DOCTYPE html>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "  </boyd>\n" +
        "</html>");
    bot.sleep(Timing.time2S());
    final String tagToSelect = "<body>";
    SWTJBTExt.selectTextInSourcePane(botExt,
        CodeCompletionTest.HTML_PAGE_NAME,
        tagToSelect,
        tagToSelect.length(),
        0,
        0);
    List<String> proposals = htmlEditor.contentAssist().getProposalList();
    StringBuffer sbMissingProposals = new StringBuffer("");
    for (String expectedItem : getHTML5ProposalList()){
      if (!proposals.contains(expectedItem)){
        if (sbMissingProposals.length() != 0){
          sbMissingProposals.append(",");
        }
        sbMissingProposals.append(expectedItem);
      }
    }
    assertTrue("There are missing Code Assist proposals for these HTML 5 tags: " + sbMissingProposals.toString(),
        sbMissingProposals.length() == 0 );
  }
  /**
   * Tests Code Completion for RichFaces tags
   */
  public void testCodeCompletionOfRichFacesTags(){
    createXhtmlPage(CodeCompletionTest.XHTML_PAGE_NAME);
    SWTBotExt botExt = new SWTBotExt();
    SWTBotEditorExt xhtmlEditor = botExt.swtBotEditorExtByTitle(CodeCompletionTest.XHTML_PAGE_NAME);
    xhtmlEditor.setText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
        "<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
        "  xmlns:h=\"http://java.sun.com/jsf/html\"\n" +
        "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
        "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n" +
        "  xmlns:a4j=\"http://richfaces.org/a4j\"\n" +
        "  xmlns:rich=\"http://richfaces.org/rich\">\n" +
        "  <rich:\n" +
        "</ui:composition>");
    xhtmlEditor.save();
    bot.sleep(Timing.time2S());
    final String tagToSelect = "<rich:";
    SWTJBTExt.selectTextInSourcePane(botExt,
        CodeCompletionTest.XHTML_PAGE_NAME,
        tagToSelect,
        tagToSelect.length(),
        0,
        0);
    List<String> proposals = xhtmlEditor.contentAssist().getProposalList();
    StringBuffer sbMissingProposals = new StringBuffer("");
    for (String expectedItem : getRichFacesProposalList()){
      if (!proposals.contains(expectedItem)){
        if (sbMissingProposals.length() != 0){
          sbMissingProposals.append(",");
        }
        sbMissingProposals.append(expectedItem);
      }
    }
    assertTrue("There are missing Code Assist proposals for these HTML 5 tags: " + sbMissingProposals.toString(),
        sbMissingProposals.length() == 0 );

  }
  /**
   * Returns list of expected Content Assist proposals for jsp page within <f:view> tag
   * @return
   */
  private static List<String> getJspPageProposalList(){
    LinkedList<String> result = new LinkedList<String>();
    
    result.add("New JSF EL Expression - Create a new attribute value with #{}");
    result.add("f:actionListener");
    result.add("f:attribute");
    result.add("f:convertDateTime");
    result.add("f:convertNumber");
    result.add("f:converter");
    result.add("f:facet");
    result.add("f:loadBundle");
    result.add("f:param");
    result.add("f:phaseListener");
    result.add("f:selectItem");
    result.add("f:selectItems");
    result.add("f:setPropertyActionListener");
    result.add("f:subview");
    result.add("f:validateDoubleRange");
    result.add("f:validateLength");
    result.add("f:validateLongRange");
    result.add("f:validator");
    result.add("f:valueChangeListener");
    result.add("f:verbatim");
    result.add("f:view");
    result.add("h:column");
    result.add("h:commandButton");
    result.add("h:commandLink");
    result.add("h:dataTable");
    result.add("h:form");
    result.add("h:graphicImage");
    result.add("h:inputHidden");
    result.add("h:inputSecret");
    result.add("h:inputText");
    result.add("h:inputTextarea");
    result.add("h:message");
    result.add("h:messages");
    result.add("h:outputFormat");
    result.add("h:outputLabel");
    result.add("h:outputLink");
    result.add("h:outputText");
    result.add("h:panelGrid");
    result.add("h:panelGroup");
    result.add("h:selectBooleanCheckbox");
    result.add("h:selectManyCheckbox");
    result.add("h:selectManyListbox");
    result.add("h:selectManyMenu");
    result.add("h:selectOneListbox");
    result.add("h:selectOneMenu");
    result.add("h:selectOneRadio");
    result.add("Message");
    result.add("user : User");
    result.add("jsp:attribute");
    result.add("jsp:body");
    result.add("jsp:element");
    result.add("jsp:fallback");
    result.add("jsp:forward");
    result.add("jsp:getProperty");
    result.add("jsp:include");
    result.add("jsp:output");
    result.add("jsp:param");
    result.add("jsp:params");
    result.add("jsp:plugin");
    result.add("jsp:setProperty");
    result.add("jsp:useBean");
    result.add("Facelets ui:Composition - Composition with one definition");
    result.add("JSP declaration(s) - JSP declaration(s) <%!..%>");
    result.add("Tag attribute directive - Tag attribute directive");
    result.add("comment - comment");
    result.add("dl - definition list");
    result.add("img - img     (map)");
    result.add("ol - ordered list");
    result.add("script - script     (commented)");
    result.add("style - style     (commented)");
    result.add("table - table");
    result.add("ul - unordered list");
    
    return result;
  }
  /**
   * Returns list of expected Content Assist proposals for action attribute of <h:commandButton> tag
   * @return
   */
  private static List<String> getCommandButtonActionAttrProposalList(){
    LinkedList<String> result = new LinkedList<String>();
    
    result.add("hello");
    result.add("Message");
    result.add("user : User");
        
    return result;
  } 
  /**
   * Returns list of expected Content Assist proposals for value attribute of <h:commandButton> tag
   * @return
   */
  private static List<String> getCommandButtonValueAttrProposalList(){
    LinkedList<String> result = new LinkedList<String>();
    
    result.add("Message");
    result.add("user : User");
    
    return result;
  } 
  /**
   * Returns list of HTML 5 tags which should be in Code Assist proposals 
   * @return
   */
  private static List<String> getHTML5ProposalList() {
    List<String> result = new LinkedList<String>();
    result.add("article");
    result.add("aside");
    result.add("audio");
    result.add("command");
    result.add("canvas");
    result.add("details");
    result.add("hgroup");
    result.add("meter");
    result.add("progress");
    result.add("time");
    result.add("wbr");
    result.add("embed");
    result.add("datalist");
    result.add("keygen");
    result.add("output");
    result.add("figure");
    result.add("footer");
    result.add("header");
    result.add("mark");
    result.add("nav");
    result.add("ruby");
    result.add("section");
    result.add("video");
    return result;
  }
  
  /**
   * Returns list of richFaces tags which should be in Code Assist proposals 
   * @return
   */
  private static List<String> getRichFacesProposalList() {
    List<String> result = new LinkedList<String>();
    result.add("rich:calendar");
    result.add("rich:tree");
    result.add("rich:inplaceSelect");
    result.add("rich:select");
    return result;
  }
}