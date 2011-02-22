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
 * Tests Rich Faces Progress Tag behavior 
 * @author vlado pakan
 *
 */
public class ProgressTagTest extends RichFacesTagsTest{
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
        "      <rich:progressBar mode=\"client\" id=\"progressBar\">\n" +
        "      </rich:progressBar>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(jspWebBrowser,
        "DIV", 
        new String[]{"title","class"},
        new String[]{"rich:progressBar mode: client id: progressBar","rich-progress-bar-block rich-progress-bar-width rich-progress-bar-shell"},
        RichFacesTagsTest.TEST_PAGE_NAME_JSP);
      // check tag selection
      jspWebBrowser.selectDomNode(jspWebBrowser.getDomNodeByTagName("DIV",4), 0);
      bot.sleep(Timing.time3S());
      String selectedText = jspEditor.getSelection();
      final String hasToStartWith = "<rich:progressBar";
      assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
          "\nbut it is '" + selectedText + "'",
          selectedText.trim().startsWith(hasToStartWith));
      final String hasEndWith = "</rich:progressBar>";
      assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
          "\nbut it is '" + selectedText + "'",
          selectedText.trim().endsWith(hasEndWith));
  }

}
