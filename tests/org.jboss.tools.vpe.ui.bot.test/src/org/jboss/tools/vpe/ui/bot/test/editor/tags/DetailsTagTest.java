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
 * Tests Details Tag behavior 
 * @author vlado pakan
 *
 */
public class DetailsTagTest extends AbstractTagTest{
  private static String DETAILS_TEXT = "!@#$ Details text $#@!";
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
        "    <details>" + DetailsTagTest.DETAILS_TEXT + "</details>\n" +
        "  </body>\n" +
        "</html>\n");
  }

  @Override
  protected void verifyTag() {
    // check Problems View for Errors
    assertProbelmsViewNoErrors(botExt);
    // visual representation contains DETAILS tag
    assertVisualEditorContains(getVisualEditor(), "DETAILS", null, null, getTestPageFileName());
    // visual representation contains DETAILS_TEXT text
    assertVisualEditorContainsNodeWithValue(getVisualEditor(), 
        DetailsTagTest.DETAILS_TEXT, getTestPageFileName());
  }

}
