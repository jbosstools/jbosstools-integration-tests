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
 * Tests Rich Faces PanelMenu Tag behavior 
 * @author vlado pakan
 *
 */
public class PanelMenuTagTest extends AbstractTagTest{
  
  private static String getSubGroupLabel (final int groupIndex , final int subGroupIndex){
    StringBuffer sb = new StringBuffer();
    sb.append(groupIndex);
    sb.append(":");
    sb.append(subGroupIndex);
    sb.append(":");
    sb.append("Subgroup");
    return sb.toString();  
  }
  
  private static String getGroupLabel (final int groupIndex){
    StringBuffer sb = new StringBuffer();
    sb.append(groupIndex);
    sb.append(":");
    sb.append("Group");
    return sb.toString();  
  }
  
  private static String getGroupItemLabel (final int groupIndex ,final int itemIndex){
    StringBuffer sb = new StringBuffer();
    sb.append(groupIndex);
    sb.append(":");
    sb.append(itemIndex);
    sb.append(":");
    sb.append("Item");
    return sb.toString();  
  }
  
  private static String getSubGroupItemLabel (final int groupIndex , final int subGroupIndex, final int itemIndex){
    StringBuffer sb = new StringBuffer();
    sb.append(groupIndex);
    sb.append(":");
    sb.append(subGroupIndex);
    sb.append(":");
    sb.append(itemIndex);
    sb.append(":");
    sb.append("Item");
    return sb.toString();  
  }

  @Override
  protected void initPageContent() {
    xhtmlEditor.setText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n" +
      "  xmlns:h=\"http://java.sun.com/jsf/html\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" + 
      "  xmlns:a4j=\"http://richfaces.org/a4j\">\n" +
      "  <head>\n" +
      "  </head>\n" +
      "  <body>\n" +
      "    <f:view>\n" +
      "      <rich:panelMenu style=\"text-align:center;color:Black;font-style:italic;font-size:large;width:200px;border-style:dashed;background-color:Orchid;border-color:Cornsilk;text-decoration:overline;font-family:Arial Rounded MT Bold;font-weight:bolder;\" mode=\"ajax\" >\n" +
      "        <rich:panelMenuGroup label=\"" + PanelMenuTagTest.getGroupLabel(0) +"\">\n" +
      "          <rich:panelMenuItem label=\"" + PanelMenuTagTest.getGroupItemLabel(0,0) + "\">\n" +
      "          </rich:panelMenuItem>\n" +
      "        </rich:panelMenuGroup>\n" +
      "        <rich:panelMenuGroup label=\"" + PanelMenuTagTest.getGroupLabel(1) +"\">\n" +
      "          <rich:panelMenuItem label=\"" + PanelMenuTagTest.getGroupItemLabel(1, 0) + "\">\n" +
      "          </rich:panelMenuItem>\n" +
      "          <rich:panelMenuGroup label=\"" + PanelMenuTagTest.getSubGroupLabel(1,0) +"\">\n" +
      "            <rich:panelMenuItem label=\"" + PanelMenuTagTest.getSubGroupItemLabel(1, 0 , 0) + "\">\n" +
      "            </rich:panelMenuItem>\n" +      "" +
      "          </rich:panelMenuGroup>\n" +
      "          <rich:panelMenuItem label=\"" + PanelMenuTagTest.getGroupItemLabel(1, 1) + "\">\n" +
      "          </rich:panelMenuItem>\n" +
      "        </rich:panelMenuGroup>\n" +
      "        <rich:panelMenuGroup label=\"" + PanelMenuTagTest.getGroupLabel(2) +"\">\n" +
      "          <rich:panelMenuItem label=\"" + PanelMenuTagTest.getGroupItemLabel(2, 0) + "\">\n" +
      "          </rich:panelMenuItem>\n" +
      "        </rich:panelMenuGroup>\n" +
      "      </rich:panelMenu>\n" +
      "    </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    assertVisualEditorContains(xhtmlWebBrowser,
      "TD", 
      new String[]{"class"},
      new String[]{"rich-pmenu-top-group-self-label"},
      AbstractTagTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContains(xhtmlWebBrowser,
      "TD", 
      new String[]{"class"},
      new String[]{"rich-pmenu-group-self-icon rich-pmenu-top-group-self-icon"},
      AbstractTagTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContains(xhtmlWebBrowser,
      "TABLE", 
      new String[]{"class"},
      new String[]{"dr-pmenu-top-group rich-pmenu-group"},
      AbstractTagTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      PanelMenuTagTest.getGroupLabel(0),
      AbstractTagTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      PanelMenuTagTest.getGroupLabel(1),
      AbstractTagTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
      PanelMenuTagTest.getGroupLabel(2),
      AbstractTagTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorNotContainNodeWithValue(xhtmlWebBrowser,
        PanelMenuTagTest.getGroupItemLabel(0, 0),
        AbstractTagTest.TEST_PAGE_NAME_XHTML);
    // check tag selection
    xhtmlWebBrowser.selectDomNode(xhtmlWebBrowser.getDomNodeByTagName("DIV",4), 0);
    bot.sleep(Timing.time3S());
    String selectedText = xhtmlEditor.getSelection();
    String hasToStartWith = "<rich:panelMenu ";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    String hasToEndWith = "</rich:panelMenu>";
    assertTrue("Selected text in Source Pane has to end with '" + hasToEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().endsWith(hasToEndWith));
    // select and expand second group
    xhtmlWebBrowser.selectDomNode(xhtmlWebBrowser.getDomNodeByTagName("DIV",6), 0);
    bot.sleep(Timing.time1S());
    xhtmlWebBrowser.mouseClickOnNode(xhtmlWebBrowser.getDomNodeByTagName("DIV",6));
    bot.sleep(Timing.time3S());
    selectedText = xhtmlEditor.getSelection();
    hasToStartWith = "<rich:panelMenuGroup label=\"" + PanelMenuTagTest.getGroupLabel(1);
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    hasToEndWith = "</rich:panelMenuGroup>";
    assertTrue("Selected text in Source Pane has to end with '" + hasToEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().endsWith(hasToEndWith));
    // select and expand subgroup
    xhtmlWebBrowser.selectDomNode(xhtmlWebBrowser.getDomNodeByTagName("DIV",8), 0);
    bot.sleep(Timing.time1S());
    xhtmlWebBrowser.mouseClickOnNode(xhtmlWebBrowser.getDomNodeByTagName("DIV",8));
    bot.sleep(Timing.time3S());
    // select subgroup item
    xhtmlWebBrowser.selectDomNode(xhtmlWebBrowser.getDomNodeByTagName("DIV",9), 0);
    bot.sleep(Timing.time3S());
    bot.sleep(Timing.time3S());
    selectedText = xhtmlEditor.getSelection();
    hasToStartWith = "<rich:panelMenuItem label=\"" + PanelMenuTagTest.getSubGroupItemLabel(1, 0, 0) + "\"";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    hasToEndWith = "</rich:panelMenuItem>";
    assertTrue("Selected text in Source Pane has to end with '" + hasToEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().endsWith(hasToEndWith));
    assertVisualEditorContainsNodeWithValue(xhtmlWebBrowser,
        PanelMenuTagTest.getSubGroupItemLabel(1, 0, 0),
        AbstractTagTest.TEST_PAGE_NAME_XHTML);
    assertVisualEditorContains(xhtmlWebBrowser,
        "TD", 
        new String[]{"class"},
        new String[]{"rich-pmenu-item-label"},
        AbstractTagTest.TEST_PAGE_NAME_XHTML);
  }

}
