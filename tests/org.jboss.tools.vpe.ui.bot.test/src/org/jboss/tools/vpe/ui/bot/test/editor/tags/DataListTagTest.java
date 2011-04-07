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
 * Tests Rich Faces dataList Tag behavior 
 * @author vlado pakan
 *
 */
public class DataListTagTest extends RichFacesTagsTest{
  private static final String ROW_0_VALUE = "!-* Row 0 Value";
  private static final String ROW_1_VALUE = "!-* Row 1 Value";
  private static final String ROW_2_VALUE = "!-* Row 2 Value";
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
      "      <rich:dataList>\n" +
      "        <h:outputText value=\"" + DataListTagTest.ROW_0_VALUE + "\"/>\n" +
      "        <br/>\n" +
      "        <h:outputText value=\"" + DataListTagTest.ROW_1_VALUE + "\"/>\n" + 
      "        <br/>\n" +
      "        <h:outputText value=\"" + DataListTagTest.ROW_2_VALUE + "\"/>\n" +
      "      </rich:dataList>\n" +
      "    </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(xhtmlWebBrowser,
      "UL", 
      new String[]{"class"},
      new String[]{"dr-list rich-datalist"},
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContains(xhtmlWebBrowser,
      "LI", 
      new String[]{"class"},
      new String[]{"dr-list-item rich-list-item"},
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      DataListTagTest.ROW_0_VALUE,
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      DataListTagTest.ROW_1_VALUE,
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      DataListTagTest.ROW_2_VALUE,
      RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    // check tag selecti
    xhtmlWebBrowser.selectDomNode(xhtmlWebBrowser.getDomNodeByTagName("LI",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = xhtmlEditor.getSelection();
    String hasToStartWith = "<rich:dataList>";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:dataList>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
  }

}
