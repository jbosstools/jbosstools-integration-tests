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
 * Tests ajax invisible tags behavior 
 * @author vlado pakan
 *
 */
public class AjaxInvisibleTagsTest extends AbstractTagTest{
  private static final String QUEUE_VALUE = "*! Queue Value";
  private static final String JS_FUNCTION_VALUE = "*! JsFunction Value";
  private static final String KEEP_ALIVE_VALUE = "*! KeepAlive Value";
  private static final String SUPPORT_VALUE = "*! Support Value";
  private static final String STATUS_VALUE = "*! Status Value";
  private static final String LOAD_SCRIPT_VALUE = "*! loadScript Value";
  private static final String LOAD_STYLE_VALUE = "*! loadStyle Value";
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.JSP,
      "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
      "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
      "<%@ taglib uri=\"http://richfaces.org/a4j\" prefix=\"a4j\" %>\n" +
      "<%@ taglib uri=\"http://richfaces.org/rich\" prefix=\"rich\" %>\n" +
      "<html>\n" +
      " <head>\n" +
      " </head>\n" +
      " <body>\n" +
      "   <f:view>\n" +
      "     <a4j:jsFunction>" + AjaxInvisibleTagsTest.JS_FUNCTION_VALUE + "</a4j:jsFunction>\n" +
      "     <a4j:queue>" + AjaxInvisibleTagsTest.QUEUE_VALUE + "</a4j:queue>\n" +
      "     <a4j:keepAlive beanName=\"\">" + AjaxInvisibleTagsTest.KEEP_ALIVE_VALUE + "</a4j:keepAlive>\n" +
      "     <a4j:support>" + AjaxInvisibleTagsTest.SUPPORT_VALUE + "</a4j:support>\n" +
      "     <a4j:status>" + AjaxInvisibleTagsTest.STATUS_VALUE + "</a4j:status>\n" +
      "     <a4j:loadScript src=\"\">" + AjaxInvisibleTagsTest.LOAD_SCRIPT_VALUE + "</a4j:loadScript>\n" +
      "     <a4j:loadStyle src=\"\">" + AjaxInvisibleTagsTest.LOAD_STYLE_VALUE + "</a4j:loadStyle>\n" +
      "   </f:view>\n" +
      " </body> \n" +
      "</html>");
  }

  @Override
  protected void verifyTag() {
    // check Problems View for Errors
    assertProbelmsViewNoErrors(botExt);
    // check values which should not be displayed in Visual Editor 
    assertVisualEditorNotContainNodeWithValue(getVisualEditor(),
      AjaxInvisibleTagsTest.JS_FUNCTION_VALUE, 
      getTestPageFileName());
    assertVisualEditorNotContainNodeWithValue(getVisualEditor(),
      AjaxInvisibleTagsTest.QUEUE_VALUE, 
      getTestPageFileName());
    assertVisualEditorNotContainNodeWithValue(getVisualEditor(),
      AjaxInvisibleTagsTest.KEEP_ALIVE_VALUE, 
      getTestPageFileName());
    assertVisualEditorNotContainNodeWithValue(getVisualEditor(),
      AjaxInvisibleTagsTest.SUPPORT_VALUE, 
      getTestPageFileName());
    assertVisualEditorNotContainNodeWithValue(getVisualEditor(),
      AjaxInvisibleTagsTest.STATUS_VALUE, 
      getTestPageFileName());
    // check empty Visual Editor
    assertVisualEditorContainsManyNodes(getVisualEditor(),
      "TABLE",
      1,
      getTestPageFileName());
    assertVisualEditorContainsManyNodes(getVisualEditor(),
      "TR",
      1,
      getTestPageFileName());
    assertVisualEditorContainsManyNodes(getVisualEditor(),
      "TD",
      1,
      getTestPageFileName());
    assertVisualEditorContainsManyNodes(getVisualEditor(),
      "DIV",
      4,
      getTestPageFileName());    
  }

}
