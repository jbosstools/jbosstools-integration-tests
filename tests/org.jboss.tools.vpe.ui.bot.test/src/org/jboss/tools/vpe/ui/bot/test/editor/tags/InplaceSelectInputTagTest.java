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
 * Tests Rich Faces InplaceSelectInput Tag behavior 
 * @author vlado pakan
 *
 */
public class InplaceSelectInputTagTest extends RichFacesTagsTest{
  private static final String defaultLabel = "DefaultLabel";
  private static final String option1 = "Option 1";
  private static final String option2 = "Option 2";
  @Override
  protected void initPageContent() {
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<%@ taglib uri=\"http://richfaces.org/rich\" prefix=\"rich\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <rich:inplaceSelect value=\"0\" defaultLabel=\"" + defaultLabel + "\">\n"+
        "        <f:selectItem itemValue=\"0\" itemLabel=\"" + option1 +"\" />\n" +
        "        <f:selectItem itemValue=\"1\" itemLabel=\"" + option2 +"\" />\n" +
        "      </rich:inplaceSelect>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(jspWebBrowser,
        "SPAN", 
        new String[]{"vpe-user-toggle-id","title","class"},
        new String[]{"false","rich:inplaceSelect value: 0 defaultLabel: " + defaultLabel,"rich-inplace-select rich-inplace-select-view"},
        RichFacesTagsTest.TEST_PAGE_NAME_JSP);
      assertVisualEditorContainsNodeWithValue(jspWebBrowser,
          defaultLabel, 
          RichFacesTagsTest.TEST_PAGE_NAME_JSP);
      // check tag selection
      jspWebBrowser.selectDomNode(jspWebBrowser.getDomNodeByTagName("SPAN",2), 0);
      bot.sleep(Timing.time3S());
      String selectedText = jspEditor.getSelection();
      final String hasToStartWith = "<rich:inplaceSelect value=\"0\" defaultLabel=\"" + defaultLabel + "\">";
      assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
          "\nbut it is '" + selectedText + "'",
          selectedText.trim().startsWith(hasToStartWith));
      final String hasEndWith = "</rich:inplaceSelect>";
      assertTrue("Selected text in Source Pane has to end with '" + hasToStartWith + "'" +
          "\nbut it is '" + selectedText + "'",
          selectedText.trim().endsWith(hasEndWith));
      // Click on tag and check correct tag displaying
      jspWebBrowser.mouseClickOnNode(jspWebBrowser.getDomNodeByTagName("SPAN",2));
      bot.sleep(Timing.time3S());
      assertVisualEditorContains(jspWebBrowser,
          "SPAN", 
          new String[]{"vpe-user-toggle-id","class"},
          new String[]{"true","rich-inplace-select rich-inplace-select-edit"},
          RichFacesTagsTest.TEST_PAGE_NAME_JSP);
      assertVisualEditorContainsNodeWithValue(jspWebBrowser,
          option1,
          RichFacesTagsTest.TEST_PAGE_NAME_JSP);
      assertVisualEditorContainsNodeWithValue(jspWebBrowser,
          option2,
          RichFacesTagsTest.TEST_PAGE_NAME_JSP);
      assertVisualEditorNotContainNodeWithValue(jspWebBrowser, 
          defaultLabel,
          RichFacesTagsTest.TEST_PAGE_NAME_JSP);
      selectedText = jspEditor.getSelection();
      // check tag selection
      assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
          "\nbut it is '" + selectedText + "'",
          selectedText.trim().startsWith(hasToStartWith));
      assertTrue("Selected text in Source Pane has to end with '" + hasToStartWith + "'" +
          "\nbut it is '" + selectedText + "'",
          selectedText.trim().endsWith(hasEndWith));
  }

}
