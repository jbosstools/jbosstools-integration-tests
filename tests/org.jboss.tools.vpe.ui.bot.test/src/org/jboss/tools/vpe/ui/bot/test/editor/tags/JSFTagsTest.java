/*******************************************************************************

 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.tags;

import java.awt.event.KeyEvent;

import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests JSF Tags behavior 
 * @author vlado pakan
 *
 */
public class JSFTagsTest extends VPEEditorTestCase {
  
  private static final String TEST_PAGE_NAME = "JSFTagsTest.jsp";
  
  private SWTBotEditorExt jspEditor;
  private SWTBotWebBrowser webBrowser;
  private SWTBotExt botExt;
  
	public JSFTagsTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	public void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(JSFTagsTest.TEST_PAGE_NAME);
    jspEditor = botExt.swtBotEditorExtByTitle(JSFTagsTest.TEST_PAGE_NAME);
    webBrowser = new SWTBotWebBrowser(JSFTagsTest.TEST_PAGE_NAME,botExt);
	}
	/**
   * Tests h:commandLink Tag
   */
  public void testCommandLinkTag(){
    
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:form id=\"form1\">\n" +
        "        <h:commandLink value=\"Command Link\"/>\n" +
        "      </h:form>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "A", 
        new String[]{"title"},
        new String[]{"h:commandLink value: Command Link"},
        JSFTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser, "Command Link", JSFTagsTest.TEST_PAGE_NAME);
    // move h:commandLink from h:form tag and check correct behavior
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <h:commandLink value=\"Command Link\"/>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorNotContain(webBrowser,
        "A", 
        null,
        null,
        JSFTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser, "Command Link", JSFTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser, ": This link is disabled as it is not nested within a JSF form.", JSFTagsTest.TEST_PAGE_NAME);

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
  /**
   * Tests h:inputText Tag
   */
  public void testInputTextTag(){
    
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:inputText/>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "INPUT", 
        new String[]{"title"},
        new String[]{"h:inputText"},
        JSFTagsTest.TEST_PAGE_NAME);
    // check tag selection
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT"), 0);
    bot.sleep(Timing.time3S());
    String selectedText = jspEditor.getSelection();
    final String hasToStartWith = "<h:inputText";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    // check text insertion
    webBrowser.setFocus();
    final String insertText = "insertText";
    KeyboardHelper.typeBasicStringUsingAWT(insertText);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "INPUT", 
        new String[]{"title","value"},
        new String[]{"h:inputText value: " + insertText,insertText},
        JSFTagsTest.TEST_PAGE_NAME);
    assertSourceEditorContains(jspEditor.getText(), 
        "<h:inputText value=\"" + insertText + "\"",
        JSFTagsTest.TEST_PAGE_NAME);
  }
  /**
   * Tests h:inputTextArea Tag
   */
  public void testInputTextAreaTag(){
    
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:inputTextarea/>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "TEXTAREA", 
        new String[]{"title"},
        new String[]{"h:inputTextarea"},
        JSFTagsTest.TEST_PAGE_NAME);
    // check tag selection
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("TEXTAREA"), 0);
    bot.sleep(Timing.time3S());
    String selectedText = jspEditor.getSelection();
    final String hasToStartWith = "<h:inputTextarea";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    // check text insertion
    webBrowser.setFocus();
    final String insertText = "insertText";
    KeyboardHelper.typeBasicStringUsingAWT(insertText);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "TEXTAREA", 
        new String[]{"title"},
        new String[]{"h:inputTextarea value: " + insertText},
        JSFTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser, insertText, JSFTagsTest.TEST_PAGE_NAME);
    assertSourceEditorContains(jspEditor.getText(), 
        "<h:inputTextarea value=\"" + insertText + "\"",
        JSFTagsTest.TEST_PAGE_NAME);
  }
  /**
   * Tests h:outputText Tag
   */
  public void testOutputTextTag(){
    
    final String outputText = "Output Text";
    final String normalText = "Normal Text";
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:form id=\"form1\">\n" +
        "        <h:outputText value=\"" + outputText + "\"/>" + normalText + "\n" +
        "      </h:form>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        outputText, 
        JSFTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        normalText, 
        JSFTagsTest.TEST_PAGE_NAME);
    // check editing via Visual Pane
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("SPAN"), 0);
    webBrowser.setFocus();
    final String insertText = "inserted";
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_LEFT);
    KeyboardHelper.typeBasicStringUsingAWT(insertText);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertSourceEditorContains(jspEditor.getText(), 
        "<h:outputText value=\"" + insertText + outputText + "\"",
        JSFTagsTest.TEST_PAGE_NAME);
    KeyboardHelper.typeKeyCodeUsingAWTRepeately(KeyEvent.VK_RIGHT,outputText.length() + normalText.length());
    KeyboardHelper.typeBasicStringUsingAWT(insertText);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertSourceEditorContains(jspEditor.getText(), 
        normalText + insertText,
        JSFTagsTest.TEST_PAGE_NAME);
  }
  /**
   * Tests h:selectManyCheckbox Tag
   */
  public void testSelectManyCheckbox(){
    final String itemLabel = "item1";
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:form id=\"form1\">\n" +
        "        <h:selectManyCheckbox value=\"checkbox\">\n" +
        "          <f:selectItem itemLabel=\"" + itemLabel + "\"/>\n" +
        "        </h:selectManyCheckbox>\n" +
        "      </h:form>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "INPUT", 
        new String[]{"type"},
        new String[]{"checkbox"},
        JSFTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser, itemLabel, JSFTagsTest.TEST_PAGE_NAME);
    // check tag selection
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT"), 0);
    bot.sleep(Timing.time3S());
    String selectedText = jspEditor.getSelection();
    final String hasToStartWith = "<f:selectItem itemLabel=\"" + itemLabel + "\"";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    // check text insertion
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("LABEL"), 0);
    bot.sleep(Timing.time3S());
    webBrowser.setFocus();
    final String insertText = "insertText";
    KeyboardHelper.typeBasicStringUsingAWT(insertText);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContainsNodeWithValue(webBrowser, insertText + itemLabel, JSFTagsTest.TEST_PAGE_NAME);
    assertSourceEditorContains(jspEditor.getText(), 
        "<f:selectItem itemLabel=\"" + insertText + itemLabel + "\"",
        JSFTagsTest.TEST_PAGE_NAME);
  }
  /**
   * Tests h:selectOneRadio Tag
   */
  public void testSelectOneRadio(){
    final String itemLabel = "item1";
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:form id=\"form1\">\n" +
        "        <h:selectOneRadio value=\"radio\">\n" +
        "          <f:selectItem itemLabel=\"" + itemLabel + "\"/>\n" +
        "        </h:selectOneRadio>\n" +
        "      </h:form>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "INPUT", 
        new String[]{"type"},
        new String[]{"radio"},
        JSFTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser, itemLabel, JSFTagsTest.TEST_PAGE_NAME);
    // check tag selection
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT"), 0);
    bot.sleep(Timing.time3S());
    String selectedText = jspEditor.getSelection();
    final String hasToStartWith = "<f:selectItem itemLabel=\"" + itemLabel + "\"";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    // check text insertion
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("LABEL"), 0);
    bot.sleep(Timing.time3S());
    webBrowser.setFocus();
    final String insertText = "insertText";
    KeyboardHelper.typeBasicStringUsingAWT(insertText);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContainsNodeWithValue(webBrowser, insertText + itemLabel, JSFTagsTest.TEST_PAGE_NAME);
    assertSourceEditorContains(jspEditor.getText(), 
        "<f:selectItem itemLabel=\"" + insertText + itemLabel + "\"",
        JSFTagsTest.TEST_PAGE_NAME);
  } 
}
