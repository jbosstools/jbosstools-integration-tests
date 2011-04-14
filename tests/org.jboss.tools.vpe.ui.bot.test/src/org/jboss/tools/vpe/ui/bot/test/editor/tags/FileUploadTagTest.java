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
 * Tests Rich Faces FileUpload Tag behavior 
 * @author vlado pakan
 *
 */
public class FileUploadTagTest extends AbstractTagTest{
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
        "      <rich:fileUpload fileUploadListener=\"\"\n"+
        "        maxFilesQuantity=\"1\" id=\"upload\"\n" +
        "        immediateUpload=\"false\"\n" +
        "        acceptedTypes=\"jpg\" style=\"width:500; height:500\">\n" +
        "     </rich:fileUpload>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "B", 
      new String[]{"title"},
      new String[]{"rich:fileUpload fileUploadListener:  maxFilesQuantity: 1 id: upload immediateUpload: false acceptedTypes: jpg style: width:500; height:500"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV", 
      new String[]{"class"},
      new String[]{"rich-fileupload-list-decor"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV", 
      new String[]{"class"},
      new String[]{"rich-fileupload-button-border"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV", 
      new String[]{"class"},
      new String[]{"rich-fileupload-button rich-fileupload-font"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV", 
      new String[]{"class"},
      new String[]{"rich-fileupload-button-content rich-fileupload-font rich-fileupload-ico rich-fileupload-ico-add"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV", 
      new String[]{"class"},
      new String[]{"rich-fileupload-button-content rich-fileupload-font rich-fileupload-ico rich-fileupload-ico-start"},
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "DIV", 
      new String[]{"class"},
      new String[]{"rich-fileupload-button-content rich-fileupload-font rich-fileupload-ico rich-fileupload-ico-clear"},
      getTestPageFileName());
      // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("B",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    final String hasToStartWith = "<rich:fileUpload";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
      "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    final String hasEndWith = "</rich:fileUpload>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
      "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
  }

}
