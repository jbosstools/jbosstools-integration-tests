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
 * Tests Rich Faces columnGroup Tag behavior 
 * @author vlado pakan
 *
 */
public class ColumnGroupTagTest extends RichFacesTagsTest{
  private static final String SPAN_COLUMN_VALUE = "!-*Span Column Value";
  private static final String SPAN_ROW__VALUE = "!-*Span Row Value";
  private static final String CELL_1_0_VALUE = "!-* Cell 1x0 Value";
  private static final String CELL_1_1_VALUE = "!-* Cell 1x1 Value";
  @Override
  protected void initPageContent() {
    xhtmlEditor.setText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" +
      "  xmlns:h=\"http://java.sun.com/jsf/html\">\n" +
      "  <head>\n" +
      "  </head>\n" +
      "  <body>\n" +
      "    <f:view>\n" +
      "      <rich:columnGroup>\n" +
      "        <rich:column rowspan=\"2\">\n" +
      "          <h:outputText value=\"" + ColumnGroupTagTest.SPAN_ROW__VALUE + "\" />\n" +
      "        </rich:column>\n" +
      "        <rich:column colspan=\"2\">\n" +
      "          <h:outputText value=\"" + ColumnGroupTagTest.SPAN_COLUMN_VALUE + "\" />\n" +
      "        </rich:column>\n" +
      "        <rich:column breakBefore=\"true\">\n" +
      "          <h:outputText value=\"" + ColumnGroupTagTest.CELL_1_0_VALUE + "\" />\n" +
      "        </rich:column>\n" +
      "        <rich:column>\n" +
      "          <h:outputText value=\"" + ColumnGroupTagTest.CELL_1_1_VALUE + "\" />\n" +
      "        </rich:column>\n" +
      "      </rich:columnGroup>\n" +
      "    </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(xhtmlWebBrowser,
      "TD", 
      new String[]{"class","rowspan"},
      new String[]{"dr-table-cell rich-table-cell dr-subtable-cell","2"},
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContains(xhtmlWebBrowser,
      "TD", 
      new String[]{"class","colspan"},
      new String[]{"dr-table-cell rich-table-cell dr-subtable-cell","2"},
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContains(xhtmlWebBrowser,
      "TD", 
      new String[]{"class","breakbefore"},
      new String[]{"dr-table-cell rich-table-cell dr-subtable-cell","true"},
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      ColumnGroupTagTest.SPAN_COLUMN_VALUE,
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      ColumnGroupTagTest.SPAN_ROW__VALUE,
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      ColumnGroupTagTest.CELL_1_0_VALUE,
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      ColumnGroupTagTest.CELL_1_1_VALUE,
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    // check tag selection
    xhtmlWebBrowser.selectDomNode(xhtmlWebBrowser.getDomNodeByTagName("TBODY",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = xhtmlEditor.getSelection();
    String hasToStartWith = "<rich:columnGroup>";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:columnGroup>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
  }

}
