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

/**
 * Tests ASIDE Tag behavior 
 * @author vlado pakan
 *
 */
public class CanvasTagTest extends AbstractTagTest{
  private static final String CANVAS_TAG_TEXT = "<canvas id=\"myCanvas\"></canvas>";
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.HTML,
        "<!DOCTYPE html>\n" +
        "<html>\n" +
        "  <head>\n" +
        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
        "    <title>Insert title here</title>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    " + CanvasTagTest.CANVAS_TAG_TEXT + "\n" +
        "  </body>\n" +
        "</html>\n");
  }

  @Override
  protected void verifyTag() {
    // check Problems View for Errors
    assertProbelmsViewNoErrors(botExt);
    // visual representation contains CANVAS tag
    assertVisualEditorContains(getVisualEditor(), "CANVAS", null, null, getTestPageFileName());
    // test tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("CANVAS",0), 0);
    final String selectedText = getSourceEditor().getSelection();
    assertTrue("Selected text in Source Pane has to be '" + CanvasTagTest.CANVAS_TAG_TEXT + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().equals(CanvasTagTest.CANVAS_TAG_TEXT));
  }

}
