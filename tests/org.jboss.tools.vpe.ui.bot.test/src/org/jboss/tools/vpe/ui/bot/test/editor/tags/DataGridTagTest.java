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
public class DataGridTagTest extends AbstractTagTest{
  private static final String HEADER_VALUE = "!-*Header Value";
  private static final String FOOTER_VALUE = "!-*Footer Value";
  private static final String CELL_0_VALUE = "!-*Cell 0 Value";
  private static final String CELL_1_VALUE = "!-*Cell 1 Value";
  private static final String GRID_COLUMNS = "3";
  private static final String GRID_ELEMENTS = "6";
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.XHTML,
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" +
      "  xmlns:h=\"http://java.sun.com/jsf/html\">\n" +
      "  <head>\n" +
      "  </head>\n" +
      "  <body>\n" +
      "    <f:view>\n" +
      "      <rich:dataGrid columns=\"" + DataGridTagTest.GRID_COLUMNS + "\" elements=\"" + DataGridTagTest.GRID_ELEMENTS + "\">\n" +
      "        <f:facet name=\"header\">\n" +
      "          <h:outputText value=\"" + DataGridTagTest.HEADER_VALUE + "\" />\n" +
      "        </f:facet>\n" +
      "        <h:outputText value=\"" + DataGridTagTest.CELL_0_VALUE + "\"/>\n" +
      "        <h:outputText value=\"" + DataGridTagTest.CELL_1_VALUE + "\"/>\n" +
      "        <f:facet name=\"footer\">\n" +
      "          <h:outputText value=\"" + DataGridTagTest.FOOTER_VALUE + "\" />\n" +
      "        </f:facet>\n" +
      "      </rich:dataGrid>\n" +
      "    </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "TABLE", 
      new String[]{"class","columns","elements"},
      new String[]{"dr-table rich-table",DataGridTagTest.GRID_COLUMNS,DataGridTagTest.GRID_ELEMENTS},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "THEAD", 
      null,
      null,
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TFOOT", 
      null,
      null,
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
      new String[]{"dr-table-footer rich-table-footer"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TD", 
      new String[]{"class"},
      new String[]{"dr-table-footercell rich-table-footercell"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TR", 
      new String[]{"class"},
      new String[]{"dr-table-row rich-table-row"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "TD", 
      new String[]{"class"},
      new String[]{"dr-table-cell rich-table-cell"},
      getTestPageFileName());    
    assertVisualEditorContainsManyNodes(getVisualEditor(),
      "SPAN", 
      2 + (2 * Integer.parseInt(DataGridTagTest.GRID_ELEMENTS)),
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      DataGridTagTest.HEADER_VALUE,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      DataGridTagTest.FOOTER_VALUE,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      DataGridTagTest.CELL_0_VALUE,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      DataGridTagTest.CELL_1_VALUE,
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("THEAD",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    String hasToStartWith = "<rich:dataGrid";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:dataGrid>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
  }

}
