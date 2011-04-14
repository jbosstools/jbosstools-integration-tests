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
public class DataTableTagTest extends AbstractTagTest{
  private static final String TABLE_HEADER_VALUE = "!-*Table Header Value";
  private static final String COLUMN_HEADER_VALUE = "!-*Column Header Value";
  private static final String COLUMN_0_VALUE = "!-*Column 0 Value";
  private static final String COLUMN_1_VALUE = "!-*Column 1 Value";
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
      "      <f:facet name=\"header\">\n" +
      "        <h:outputText value=\"" + DataTableTagTest.TABLE_HEADER_VALUE + "\"></h:outputText>\n" +
      "      </f:facet>\n" +
      "      <rich:columns>\n" +
      "        <f:facet name=\"header\">\n" +
      "          <h:outputText value=\"" + DataTableTagTest.COLUMN_HEADER_VALUE + "\"/>\n" +
      "        </f:facet>\n" +
      "        <h:outputText value=\"" + DataTableTagTest.COLUMN_0_VALUE + "\" style=\"font-style:italic;\" />\n" +
      "        <h:outputText value=\"" + DataTableTagTest.COLUMN_1_VALUE + "\" />\n" +
      "      </rich:columns>\n" +
      "    </rich:dataTable>\n" +
      "  </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "TABLE", 
      new String[]{"class"},
      new String[]{"dr-table rich-table"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TR", 
      new String[]{"class"},
      new String[]{"dr-table-header rich-table-header"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TD", 
      new String[]{"class"},
      new String[]{"dr-table-headercell rich-table-headercell"},
      getTestPageFileName());
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
      DataTableTagTest.TABLE_HEADER_VALUE,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      DataTableTagTest.COLUMN_HEADER_VALUE,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      DataTableTagTest.COLUMN_0_VALUE,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      DataTableTagTest.COLUMN_1_VALUE,
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("THEAD",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    String hasToStartWith = "<rich:dataTable>";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:dataTable>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
  }

}
