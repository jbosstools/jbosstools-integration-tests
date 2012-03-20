/*******************************************************************************

 * Copyright (c) 2007-2012 Exadel, Inc. and Red Hat, Inc.
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
 * Tests Rich Faces Select Tag behavior 
 * @author vlado pakan
 *
 */
public class SelectTagTest extends AbstractTagTest{
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.XHTML,
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
            "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n" +
            "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
            "  xmlns:rich=\"http://richfaces.org/rich\">\n" + 
            "  <head>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <f:view>\n" +
        "          <rich:select/>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(getVisualEditor(),
      "SELECT", 
      null,
      null,
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("SELECT",0), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    final String expectedSelectedText = "<rich:select/>";
    assertTrue("Selected text in Source Pane has to be '" + expectedSelectedText + "'" +
      "\nbut it is '" + selectedText + "'",
      selectedText.trim().equals(expectedSelectedText));
  }

}
