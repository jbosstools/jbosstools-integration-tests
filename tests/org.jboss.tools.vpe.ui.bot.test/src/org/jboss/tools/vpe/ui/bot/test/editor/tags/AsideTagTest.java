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

/**
 * Tests Aside Tag behavior 
 * @author vlado pakan
 *
 */
public class AsideTagTest extends AbstractTagTest{
  private static String ASIDE_TEXT = "!@#$ ASIDE TEXT $#@!";
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
        "    <aside>\n" +
        "    " + AsideTagTest.ASIDE_TEXT + "\n" +
        "    </aside>\n" +
        "  </body>\n" +
        "</html>\n");
  }

  @Override
  protected void verifyTag() {
    // check Problems View for Errors
    assertProbelmsViewNoErrorsForPage();
    // visual representation contains ASIDE tag
    assertVisualEditorContains(getVisualEditor(), "ASIDE", null, null, getTestPageFileName());
    // visual representation contains ASIDE_TEXT text
    assertVisualEditorContainsNodeWithValue(getVisualEditor(), 
        AsideTagTest.ASIDE_TEXT, getTestPageFileName());
  }

}
