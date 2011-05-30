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
 * Tests Rich Faces DataTable Tag behavior 
 * @author vlado pakan
 *
 */
public class ActionParamTagTest extends AbstractTagTest{
  @Override
  protected void initTestPage() {
    initTestPage(TestPageType.XHTML,
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "  xmlns:f=\"http://java.sun.com/jsf/core\"\n" +
      "  xmlns:a4j=\"http://richfaces.org/a4j\"\n" +
      "  xmlns:h=\"http://java.sun.com/jsf/html\">\n" +
      "<head>\n" +
      "</head>\n" +
      "<body>\n" +
      "  <f:view>\n" +
      "    <a4j:commandButton value=\"Set Name to Alex\">\n" +
      "      <a4j:actionparam name=\"username\" value=\"Alex\"/>\n" +
      "    </a4j:commandButton>\n" +
      "  </f:view>\n" +
      "  </body>\n" + 
      "</html>");
  }

  @Override
  protected void verifyTag() {
    // check tag selection
    getSourceEditor().selectLine(10);
    bot.sleep(Timing.time3S());
    nsIDOMNode selectedVisualNode=getVisualEditor().getSelectedDomNode();
    assertNotNull("Selected node in Visual Editor cannot be null",selectedVisualNode);
    String expectedSelectedNode = "DIV";
    assertTrue("Selected Node has to be '" + expectedSelectedNode + "' node but is " + selectedVisualNode.getLocalName(),
      selectedVisualNode.getLocalName().equalsIgnoreCase(expectedSelectedNode));
    String selectedNodeTitle = SWTBotWebBrowser.getNodeAttribute(selectedVisualNode, "title");
    assertNotNull("Selected Node in Visual Editor has to have attribute title but it has not." ,selectedNodeTitle);
    final String expectedTitle = "f:view";
    assertTrue("Selected Node in Visual Editor has to have attribute title=\"" + expectedTitle + 
        "\" but has \"" + selectedNodeTitle + "\"",
      expectedTitle.equals(selectedNodeTitle));
  }

}
