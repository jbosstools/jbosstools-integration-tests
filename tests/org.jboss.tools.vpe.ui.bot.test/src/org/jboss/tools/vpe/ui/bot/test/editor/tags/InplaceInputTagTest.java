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
 * Tests Rich Faces InplaceInput Tag behavior 
 * @author vlado pakan
 *
 */
public class InplaceInputTagTest extends AbstractTagTest{
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
        "      <rich:inplaceInput defaultLabel=\"" + defaultLabel+ "\"/>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "SPAN", 
      new String[]{"vpe-user-toggle-id","title","class"},
      new String[]{"false","rich:inplaceInput defaultLabel: " + defaultLabel,"rich-inplace rich-inplace-view"},
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      defaultLabel, 
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("SPAN",2), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    final String expectedSelectedText = "<rich:inplaceInput defaultLabel=\"" + defaultLabel+ "\"/>";
    assertTrue("Selected text in Source Pane has to be '" + expectedSelectedText + "'" +
      "\nbut it is '" + selectedText + "'",
      selectedText.trim().equals(expectedSelectedText));
    getVisualEditor().mouseClickOnNode(getVisualEditor().getDomNodeByTagName("SPAN",2));
    bot.sleep(Timing.time3S());
    selectedText = getSourceEditor().getSelection();
    assertVisualEditorContains(getVisualEditor(),
      "SPAN", 
      new String[]{"vpe-user-toggle-id","class"},
      new String[]{"true","rich-inplace rich-inplace-edit"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "INPUT", 
      new String[]{"type","class","value"},
      new String[]{"text","rich-inplace-field",defaultLabel},
      getTestPageFileName());
    assertTrue("Selected text in Source Pane has to be '" + expectedSelectedText + "'" +
      "\nbut it is '" + selectedText + "'",
      selectedText.trim().equals(expectedSelectedText));
  
  }

}
