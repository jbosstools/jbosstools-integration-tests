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
 * Tests Rich Faces ComboBox Tag behavior 
 * @author vlado pakan
 *
 */
public class ComboBoxTagTest extends AbstractTagTest{
  private static final String defaultLabel = "DefaultLabel";

  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.JSP,
        "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<%@ taglib uri=\"http://richfaces.org/rich\" prefix=\"rich\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:form>\n" +
        "        <rich:comboBox id=\"comboBox\" defaultLabel=\"" + defaultLabel + "\">\n" +
        "          <f:selectItem itemValue=\"item 1\"/>\n" +
        "          <f:selectItem itemValue=\"item 2\"/>\n" +
        "        </rich:comboBox>\n" +
        "      </h:form>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "INPUT", 
      new String[]{"type","class","value"},
      new String[]{"text","rich-combobox-font-disabled rich-combobox-input-inactive",defaultLabel},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "INPUT", 
      new String[]{"type","class"},
      new String[]{"text","rich-combobox-font-inactive rich-combobox-button-background rich-combobox-button-inactive"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "INPUT", 
      new String[]{"type","class"},
      new String[]{"text","rich-combobox-font-inactive rich-combobox-button-icon-inactive rich-combobox-button-inactive"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV", 
      new String[]{"class"},
      new String[]{"rich-combobox-strut rich-combobox-font"},
      getTestPageFileName());    
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("INPUT"), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    final String hasToStartWith = "<rich:comboBox";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    final String hasToEndWith = "</rich:comboBox>";
    assertTrue("Selected text in Source Pane has to end with '" + hasToEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().endsWith(hasToEndWith));
  }

}
