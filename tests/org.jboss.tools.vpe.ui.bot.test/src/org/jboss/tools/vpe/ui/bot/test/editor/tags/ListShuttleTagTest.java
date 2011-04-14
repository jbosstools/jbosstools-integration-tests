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
 * Tests Rich Faces List Shuttle Tag behavior 
 * @author vlado pakan
 *
 */
public class ListShuttleTagTest extends AbstractTagTest{
  @Override
  protected void initTestPage() {
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
      "      <rich:listShuttle>\n" +
      "      </rich:listShuttle>\n" +
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
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      "First",
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      "Up",
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      "Last",
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      "Down",
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV",
      new String[] { "class" },
      new String[] { "rich-shuttle-button-content" }, 
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TABLE",
      new String[] { "class" }, 
      new String[] { "rich-list-shuttle" },
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV",
      new String[] { "class" },
      new String[] { "rich-shuttle-controls" },
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV",
      new String[] { "class" }, 
      new String[] { "rich-shuttle-control" },
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("DIV", 4), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    String hasToStartWith = "<rich:listShuttle>";
    assertTrue("Selected text in Source Pane has to start with '"
      + hasToStartWith + "'" + "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:listShuttle>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith
      + "'" + "\nbut it is '" + selectedText + "'", selectedText.trim().endsWith(hasEndWith));
  }

}
