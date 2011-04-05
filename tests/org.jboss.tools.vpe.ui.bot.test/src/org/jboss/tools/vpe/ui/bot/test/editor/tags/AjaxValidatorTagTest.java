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
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Tests Rich Faces ajaxValidator Tag behavior 
 * @author vlado pakan
 *
 */
public class AjaxValidatorTagTest extends RichFacesTagsTest{
  private static final String INPUT_TEXT = "!*- Input Text";
  @Override
  protected void initPageContent() {
    xhtmlEditor.setText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:rich=\"http://richfaces.org/rich\"\n" +
      "  xmlns:h=\"http://java.sun.com/jsf/html\">\n" +
      "<head>\n" +
      "</head>\n" +
      "<body>\n" +
      "  <f:view>\n" +
      "    <h:inputText value=\"" + AjaxValidatorTagTest.INPUT_TEXT + "\">\n" +
      "      <f:validateLength minimum=\"3\" maximum=\"12\"/>\n" +
      "      <rich:ajaxValidator event=\"onblur\"/>\n" +
      "    </h:inputText>\n" +
      "  </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    // check tag selection
    xhtmlEditor.selectLine(11);
    bot.sleep(Timing.time3S());
    nsIDOMNode selectedVisualNode=xhtmlWebBrowser.getSelectedDomNode();
    assertNotNull("Selected node in Visual Editor cannot be null",selectedVisualNode);
    String expectedSelectedNode = "SPAN";
    assertTrue("Selected Node has to be '" + expectedSelectedNode + "' node but is " + selectedVisualNode.getLocalName(),
      selectedVisualNode.getLocalName().equals(expectedSelectedNode));
    String selectedNodeTitle = SWTBotWebBrowser.getNodeAttribute(selectedVisualNode, "title");
    assertNotNull("Selected Node in Visual Editor has to have attribute title but it has not." ,selectedNodeTitle);
    final String expectedTitle = "h:inputText value: " + AjaxValidatorTagTest.INPUT_TEXT;
    assertTrue("Selected Node in Visual Editor has to have attribute title=\"" + expectedTitle + 
        "\" but has \"" + selectedNodeTitle + "\"",
      expectedTitle.equals(selectedNodeTitle));
  }

}
