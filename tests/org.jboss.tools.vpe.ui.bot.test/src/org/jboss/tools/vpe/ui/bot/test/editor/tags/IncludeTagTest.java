/*******************************************************************************

 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.tags;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
/**
 * Tests ajax include Tag behavior 
 * @author vlado pakan
 *
 */
public class IncludeTagTest extends AbstractTagTest{
  private static final String INCLUDE_PAGE_NAME = "IncludePage.jsp";
  @Override
  protected void initTestPage() {
    // Create pge which will be included
    createJspPage(IncludeTagTest.INCLUDE_PAGE_NAME);
    TextEditor includeEditor = new TextEditor(IncludeTagTest.INCLUDE_PAGE_NAME);
    includeEditor.setText("<%@ taglib uri=\"http://richfaces.org/a4j\" prefix=\"a4j\" %>\n" +
      "<a4j:commandButton type=\"Submit\">\n" +
      "</a4j:commandButton>\n");
    includeEditor.save();
    includeEditor.close();
    
    initTestPage(TestPageType.JSP,
      "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
      "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
      "<%@ taglib uri=\"http://richfaces.org/a4j\" prefix=\"a4j\" %>\n" +
      "<html>\n" +
      " <head>\n" +
      " </head>\n" +
      " <body>\n" +
      "   <f:view>\n" +
      "     <a4j:include viewId=\"" + IncludeTagTest.INCLUDE_PAGE_NAME + "\"/>\n" +
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
      "DIV", 
      new String[] {"vpe:include-element"},
      new String[] {"yes"},
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("INPUT",0), 0);
    AbstractWait.sleep(TimePeriod.getCustom(3));
    String selectedText = getSourceEditor().getSelectedText();
    String hasToStartWith = "<a4j:include";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
  }

}
