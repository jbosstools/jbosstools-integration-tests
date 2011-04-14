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
 * Tests Rich Faces toolBar and toolBarGroup Tags behavior 
 * @author vlado pakan
 *
 */
public class ToolbarAndToolbarGroupTagTest extends AbstractTagTest{
  private static final String GROUP_0_LABEL = "!-* Group 0 Label";
  private static final String GROUP_1_LABEL = "!-* Group 1 Label";
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.XHTML,
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" +
      "  xmlns:h=\"http://java.sun.com/jsf/html\">\n" +
      "  <head>\n" +
      "  </head>\n" +
      "  <body>\n" +
      "    <f:view>\n" +
      "      <rich:toolBar>\n" +
      "        <rich:toolBarGroup>\n" +
      "          <h:outputText value=\"" + ToolbarAndToolbarGroupTagTest.GROUP_0_LABEL + "\"/>\n" +
      "        </rich:toolBarGroup>\n" +
      "        <rich:toolBarGroup>\n" +
      "          <h:outputText value=\"" + ToolbarAndToolbarGroupTagTest.GROUP_1_LABEL + "\"/>\n" + 
      "        </rich:toolBarGroup>\n" +
      "      </rich:toolBar>\n" +
      "    </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "TD", 
      new String[]{"class"},
      new String[]{"dr-toolbar-int rich-toolbar-item"},
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      ToolbarAndToolbarGroupTagTest.GROUP_0_LABEL,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      ToolbarAndToolbarGroupTagTest.GROUP_1_LABEL,
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("TBODY",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    String hasToStartWith = "<rich:toolBar>";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:toolBar>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
    // check rich:toolBarGroup selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("TBODY",1), 0);
    bot.sleep(Timing.time3S());
    selectedText = getSourceEditor().getSelection();
    hasToStartWith = "<rich:toolBarGroup>";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    hasEndWith = "</rich:toolBarGroup>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));

  }

}
