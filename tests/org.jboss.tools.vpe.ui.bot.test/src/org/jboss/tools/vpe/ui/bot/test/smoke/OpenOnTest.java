/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
/**
 * Test open on functionality
 * @author Vladimir Pakan
 *
 */
public class OpenOnTest extends VPEEditorTestCase{
	/**
	 * Test Open On functionality for jsp page
	 */
  public void testOpenOn() {
    eclipse.closeAllEditors();
    openPage();
    // Check open on for uri="http://java.sun.com/jsf/html"
    String expectedOpenedFileName = "html_basic.tld";
    SWTBotEditor openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(
        SWTTestExt.bot, TEST_PAGE, "uri=\"http://java.sun.com/jsf/html\"", 5,
        0, 0, expectedOpenedFileName);
    String selectedTreeItemLabel = openedEditor.bot().tree().selection()
        .get(0, 0);
    assertTrue("Selected tree item has to have label " + expectedOpenedFileName
        + " but it has " + selectedTreeItemLabel,
        selectedTreeItemLabel.equalsIgnoreCase(expectedOpenedFileName));
    openedEditor.close();
    // Check open on for uri="http://java.sun.com/jsf/core"
    expectedOpenedFileName = "jsf_core.tld";
    openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(SWTTestExt.bot,
        TEST_PAGE, "uri=\"http://java.sun.com/jsf/core\"", 5, 0, 0,
        expectedOpenedFileName);
    selectedTreeItemLabel = openedEditor.bot().tree().selection().get(0, 0);
    assertTrue("Selected tree item has to have label " + expectedOpenedFileName
        + " but it has " + selectedTreeItemLabel,
        selectedTreeItemLabel.equalsIgnoreCase(expectedOpenedFileName));
    openedEditor.close();
    // Check open on for h:outputText
    String tagToCheck = "outputText";
    expectedOpenedFileName = "html_basic.tld";
    openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(SWTTestExt.bot,
        TEST_PAGE, "h:" + tagToCheck, 5, 0, 0,
        expectedOpenedFileName);
    openedEditor.close();
    // Check open on for f:view
    tagToCheck = "view";
    expectedOpenedFileName = "jsf_core.tld";
    openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(SWTTestExt.bot,
        TEST_PAGE, "f:" + tagToCheck, 5, 0, 0,
        expectedOpenedFileName);
    openedEditor.close();
  }

}