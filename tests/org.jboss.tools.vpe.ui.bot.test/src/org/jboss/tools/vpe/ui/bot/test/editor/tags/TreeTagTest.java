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
 * Tests Rich Faces Editor Tag behavior 
 * @author vlado pakan
 *
 */
public class TreeTagTest extends AbstractTagTest{
  private static final String typeLibrary = "library";
  private static final String typePathway = "pathway";
  private static final String typeOrganism = "organism";
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.XHTML,
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" + 
      "  xmlns:h=\"http://java.sun.com/jsf/html\"\n" +
      "  xmlns:a4j=\"http://richfaces.org/a4j\">\n" +
      "  <head>\n" +
      "  </head>\n" +
      "  <body>\n" +
      "    <f:view>\n" +
      "      <rich:tree>\n" +
      "        <rich:treeNode type=\"" + TreeTagTest.typeLibrary + "\">\n" +
      "          <h:outputText value=\"" + TreeTagTest.typeLibrary + "\"/>\n" +
      "        </rich:treeNode>\n" +
      "        <rich:treeNode type=\"" + TreeTagTest.typePathway + "\">\n" +
      "          <h:outputText value=\"" + TreeTagTest.typePathway + "\"/>\n" +
      "        </rich:treeNode>\n" +
      "        <rich:treeNode type=\"" + TreeTagTest.typeOrganism + "\">\n" +
      "          <h:outputText value=\"" + TreeTagTest.typeOrganism + "\"/>\n" +
      "        </rich:treeNode>\n" +
      "      </rich:tree>\n" +
      "    </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      TreeTagTest.typePathway, 
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      TreeTagTest.typeLibrary, 
      getTestPageFileName());
    assertVisualEditorContainsNodeWithValue(getVisualEditor(),
      TreeTagTest.typeOrganism, 
      getTestPageFileName());
    assertVisualEditorContains(getVisualEditor(),
      "IMG", 
      new String[]{"class"},
      new String[]{"treePictureStyle"},
      getTestPageFileName());
    // check tag selection
    getVisualEditor().selectDomNode(getVisualEditor().getDomNodeByTagName("DIV",4), 0);
    bot.sleep(Timing.time3S());
    String selectedText = getSourceEditor().getSelection();
    String hasToStartWith = "<rich:tree>";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().startsWith(hasToStartWith));
    String hasEndWith = "</rich:tree>";
    assertTrue("Selected text in Source Pane has to end with '" + hasEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
      selectedText.trim().endsWith(hasEndWith));
  }

}
