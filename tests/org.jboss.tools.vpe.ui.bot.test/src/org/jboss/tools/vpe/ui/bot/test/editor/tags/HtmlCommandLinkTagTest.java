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
 * Tests ajax Html Command Link Tag behavior 
 * @author vlado pakan
 *
 */
public class HtmlCommandLinkTagTest extends AbstractTagTest{
  private static final String LINK_VALUE = "*! Html Link Value";
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.JSP,
      "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
      "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
      "<%@ taglib uri=\"http://richfaces.org/a4j\" prefix=\"a4j\" %>\n" +
      "<html>\n" +
      " <head>\n" +
      " </head>\n" +
      " <body>\n" +
      "   <f:view>\n" +
      "     <a4j:htmlCommandLink>\n" +
      "       " + HtmlCommandLinkTagTest.LINK_VALUE + "\n" +
      "       <a4j:commandButton type=\"Submit\">\n" +
      "       </a4j:commandButton>\n" +
      "     </a4j:htmlCommandLink>\n" +
      "   </f:view>\n" +
      " </body> \n" +
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "INPUT", 
      new String[]{"type"},
      new String[]{"Submit"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "A", 
      null,
      null,
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      HtmlCommandLinkTagTest.LINK_VALUE, 
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("A",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    String hasToStartWith = "<a4j:htmlCommandLink";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</a4j:htmlCommandLink>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
    String hasToContain = "<a4j:commandButton type=\"Submit\">";
    assertTrue("Selected text in Source Pane has to contain '" + hasToContain + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().contains(hasToContain));
    hasToContain = HtmlCommandLinkTagTest.LINK_VALUE;
    assertTrue("Selected text in Source Pane has to contain '" + hasToContain + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().contains(hasToContain));
  }

}
