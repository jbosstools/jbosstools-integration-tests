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

/**
 * Tests Rich Faces Columns Tag behavior 
 * @author vlado pakan
 *
 */
public class ColumnsTagTest extends AbstractTagTest{
  private static final String columnHeader = "Column Header";
  private static final String columnValue = "Column Value";

  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.XHTML,
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n" +
      "  xmlns:h=\"http://java.sun.com/jsf/html\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" + 
      "  xmlns:a4j=\"http://richfaces.org/a4j\">\n" +
      "<html>\n" +
      "  <head>\n" +
      "  </head>\n" +
      "  <body>\n" +
      "    <f:view>\n" +
      "      <rich:dataTable value=\"\" var=\"model\" width=\"750\">\n" +
      "        <rich:columns value=\"\" var=\"columns\" index=\"ind\">\n" +
      "          <f:facet name=\"header\">\n" +
      "            <h:outputText value=\"" + columnHeader + "\" />\n" +
      "          </f:facet>\n" +
      "          <h:outputText value=\"" + columnValue + "\"/> \n" +
      "        </rich:columns>\n" +
      "      </rich:dataTable>\n" +     
      "    </f:view>\n" +
      "  </body>\n" + 
      "</html>\n" +
      "</ui:composition>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      columnHeader, 
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      columnValue, 
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
  }

}
