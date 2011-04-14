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

import org.jboss.tools.ui.bot.ext.Timing;

/**
 * Tests Rich Faces Pick List Tag behavior 
 * @author vlado pakan
 *
 */
public class PickListTagTest extends AbstractTagTest{
  private static final String[] options = new String[] {"Option0","Option1","Option2","Option3"};

  @Override
  protected void initTestPage() {
    StringBuffer sbOptions = new StringBuffer("");
    int index = 0;
    for (String option : options){
      sbOptions.append("      <f:selectItem itemLabel=\"");
      sbOptions.append(option);
      sbOptions.append("\" itemValue=\"");
      sbOptions.append(index);
      sbOptions.append("\" id=\"");
      sbOptions.append("id" + index);
      sbOptions.append("\"/>\n");
      index++;
    }
    initTestPage(TestPageType.XHTML,
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" + 
      "  xmlns:a4j=\"http://richfaces.org/a4j\">\n" +
      "  <head>\n" +
      "  </head>\n" +
      "  <body>\n" +
      "    <f:view>\n" +
      "      <rich:pickList value=\"\">\n" +
      sbOptions.toString() +
      "      </rich:pickList>\n" +   
      "    </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      "Copy all", 
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      "Copy", 
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      "Remove All", 
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      "Remove", 
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TABLE", 
      new String[]{"class"},
      new String[]{"rich-list-picklist"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TABLE", 
      new String[]{"class"},
      new String[]{"rich-picklist-body"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TABLE", 
      new String[]{"class"},
      new String[]{"rich-picklist-internal-tab"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TBODY", 
      null,
      null,
      getTestPageFileName());
    int index = 0;
    for (String option : options ){
      assertVisualEditorContainsNodeWithValue(getVisualEditor(),
        option, 
        getTestPageFileName());
      StringBuffer sbTitle = new StringBuffer("");
      sbTitle.append("f:selectItem itemLabel: ");
      sbTitle.append(option);
      sbTitle.append(" itemValue: ");
      sbTitle.append(index);
      sbTitle.append(" id: id");
      sbTitle.append(index);
      assertVisualEditorContains(getVisualEditor(),
        "OPTION", 
        new String[]{"title"},
        new String[]{sbTitle.toString()},
        getTestPageFileName());
      index++;
    }
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("TABLE",1), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    String hasToStartWith = "<rich:pickList value=\"\">";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
      "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:pickList>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
      "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
    // select first item in VPE
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("OPTION",0), 0);
    bot.sleep(Timing.time3S());    
    String currentLineText = getSourceEditor().getTextOnCurrentLine();
    StringBuffer sbOption = new StringBuffer("");
    sbOption.append("<f:selectItem itemLabel=\"");
    sbOption.append(options[0]);
    sbOption.append("\" itemValue=\"0\" id=\"id0\"/>");
    assertTrue("Current Line text in Source Pane has to be '" + sbOption.toString() + "'" +
      "\nbut it is '" + currentLineText + "'",
      currentLineText.trim().equals(sbOption.toString()));
    // select second item in Source Editor
    getSourceEditor().setFocus();
    getSourceEditor().selectRange(getSourceEditor().cursorPosition().line + 1,getSourceEditor().cursorPosition().column,0);
    bot.sleep(Timing.time3S());
    getVisualEditor().setFocus();
    bot.sleep(Timing.time3S());
    StringBuffer sbOptionTitle = new StringBuffer("");
    sbOptionTitle.append("f:selectItem itemLabel: ");
    sbOptionTitle.append(options[1]);
    sbOptionTitle.append(" itemValue: 1 id: id1");
    assertVisualEditorContains(getVisualEditor(),
      "OPTION", 
      new String[]{"title","style"},
      new String[]{sbOptionTitle.toString(),"border: 2px solid rgb(0, 0, 255) ! important;"},
      getTestPageFileName());
  }

}
