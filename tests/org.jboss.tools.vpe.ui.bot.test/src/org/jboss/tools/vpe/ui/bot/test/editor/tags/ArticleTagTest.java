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

/**
 * Tests Rich Faces Calendar Tag behavior 
 * @author vlado pakan
 *
 */
public class ArticleTagTest extends AbstractTagTest{
  private static String ARTICLE_TEXT = "!@#$ ARTICLE TEXT $#@!";
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
        "    <article>\n" +
        "    " + ArticleTagTest.ARTICLE_TEXT + "\n" +
        "    </article>\n" +
        "  </body>\n" +
        "</html>\n");
  }

  @Override
  protected void verifyTag() {
    // check Problems View for Errors
    assertProbelmsViewNoErrors(botExt);
    // visual representation contains ARTICLE tag
    assertVisualEditorContains(getVisualEditor(), "ARTICLE", null, null, getTestPageFileName());
    // visual representation contains ARTICLE_TEXT text
    assertVisualEditorContainsNodeWithValue(getVisualEditor(), 
        ArticleTagTest.ARTICLE_TEXT, getTestPageFileName());
  }

}
