/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test open on functionality
 * 
 * @author Vladimir Pakan
 *
 */
public class OpenOnTest extends VPEEditorTestCase {
	/**
	 * Test Open On functionality for jsp page
	 */
	@Test
	public void testOpenOn() {
		EditorHandler.getInstance().closeAll(true);
		openPage();
		// Check open on for uri="http://java.sun.com/jsf/html"
		String expectedOpenedFileName = "html_basic.tld";
		TextEditor jspPageEditor = new TextEditor(TEST_PAGE); 
		Editor openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(TEST_PAGE, 1, 24, expectedOpenedFileName);
		String selectedTreeItemLabel = new DefaultTree().getSelectedItems().get(0).getText();
		assertTrue("Selected tree item has to have label " + expectedOpenedFileName + " but it has "
				+ selectedTreeItemLabel, selectedTreeItemLabel.equalsIgnoreCase(expectedOpenedFileName));
		openedEditor.close();
		jspPageEditor.activate();
		// Check open on for uri="http://java.sun.com/jsf/core"
		expectedOpenedFileName = "jsf_core.tld";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(TEST_PAGE,0, 24, expectedOpenedFileName);
		selectedTreeItemLabel = new DefaultTree().getSelectedItems().get(0).getText();
		assertTrue("Selected tree item has to have label " + expectedOpenedFileName + " but it has "
				+ selectedTreeItemLabel, selectedTreeItemLabel.equalsIgnoreCase(expectedOpenedFileName));
		openedEditor.close();
		jspPageEditor.activate();
		// Check open on for h:outputText
		expectedOpenedFileName = "html_basic.tld";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(TEST_PAGE, 12, 16, expectedOpenedFileName);
		openedEditor.close();
		jspPageEditor.activate();
		// Check open on for f:view
		expectedOpenedFileName = "jsf_core.tld";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(TEST_PAGE, 11, 4,expectedOpenedFileName);
		openedEditor.close();
	}

}