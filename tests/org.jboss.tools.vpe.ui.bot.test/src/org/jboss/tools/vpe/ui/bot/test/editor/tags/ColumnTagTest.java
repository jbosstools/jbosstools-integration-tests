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
 * Tests Rich Faces DataTable Tag behavior 
 * @author vlado pakan
 *
 */
public class ColumnTagTest extends AbstractTagTest{
  private static final String COLUMN_HEADER_VALUE = "!-*Column Header Value";
  private static final String COLUMN_VALUE = "!-*Column Value";
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.XHTML,
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" +
      "  xmlns:h=\"http://java.sun.com/jsf/html\">\n" +
      "<head>\n" +
      "</head>\n" +
      "<body>\n" +
      "  <f:view>\n" +
      "    <rich:dataTable>\n" +
      "      <rich:column>\n" +
      "        <f:facet name=\"header\">\n" +
      "          <h:outputText value=\"" + ColumnTagTest.COLUMN_HEADER_VALUE + "\"/>\n" +
      "        </f:facet>\n" +
      "        <h:outputText value=\"" + ColumnTagTest.COLUMN_VALUE + "\"/>\n" +
      "      </rich:column>\n" +
      "    </rich:dataTable>\n" +
      "  </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "TR", 
      new String[]{"class"},
      new String[]{"dr-table-subheader rich-table-subheader"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TD", 
      new String[]{"class"},
      new String[]{"dr-table-subheadercell rich-table-subheadercell"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TR", 
      new String[]{"class"},
      new String[]{"dr-table-firstrow rich-table-firstrow"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TD", 
      new String[]{"class"},
      new String[]{"dr-table-cell rich-table-cell"},
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      ColumnTagTest.COLUMN_HEADER_VALUE,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      ColumnTagTest.COLUMN_VALUE,
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("TD",2), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    String hasToStartWith = "<rich:column>";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:column>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
  }

}
