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
  protected void initPageContent() {
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
      "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
      "<%@ taglib uri=\"http://richfaces.org/a4j\" prefix=\"a4j\" %>\n" +
      "<%@ taglib uri=\"http://richfaces.org/rich\" prefix=\"rich\" %>\n" +
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
    assertVisualEditorContains(jspWebBrowser,
      "INPUT", 
      new String[]{"type"},
      new String[]{"Submit"},
      AbstractTagTest.TEST_PAGE_NAME_JSP);
    assertVisualEditorContains(jspWebBrowser,
      "A", 
      null,
      null,
      AbstractTagTest.TEST_PAGE_NAME_JSP);
    assertVisualEditorContainsNodeWithValue(jspWebBrowser,
        HtmlCommandLinkTagTest.LINK_VALUE, 
        AbstractTagTest.TEST_PAGE_NAME_JSP);
    // check tag selection
    jspWebBrowser.selectDomNode(jspWebBrowser.getDomNodeByTagName("A",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = jspEditor.getSelection();
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
