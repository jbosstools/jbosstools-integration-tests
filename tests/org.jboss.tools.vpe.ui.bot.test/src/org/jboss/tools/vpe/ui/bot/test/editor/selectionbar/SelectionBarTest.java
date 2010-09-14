/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.selectionbar;

import java.awt.event.KeyEvent;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

/**
 * The Class SelectionBarTest.
 */
public class SelectionBarTest extends VPEAutoTestCase {

	private final String SELECTED_TEXT = "<h:inputText value=\"#{user.name}\" required=\"true\">    <f:validateLength maximum=\"30\" minimum=\"3\"/>    </h:inputText>"; //$NON-NLS-1$
	private final String SELECTED_TEXT2 = "<f:validateLength maximum=\"30\" minimum=\"3\"/>"; //$NON-NLS-1$
	
	public SelectionBarTest() {
		super();
	}

	@Override
	protected void closeUnuseDialogs() {
		/*
		 * Nothing to close
		 */
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}

	public void testSelectionBarTagsList() throws Throwable {
		SWTBotEditor editor = SWTTestExt.packageExplorer.openFile(JBT_TEST_PROJECT_NAME,
				"WebContent", "pages", TEST_PAGE); //$NON-NLS-1$ //$NON-NLS-2$
		editor.setFocus();
		/*
		 * Navigate to '<h:inputText value="#{user.name}" required="true">'
		 */
		editor.toTextEditor().navigateTo(18, 10);
		/*
		 * Send key press event to fire VPE listeners
		 */
		KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_LEFT);
		/*
		 * Click on the tag in the selection bar 
		 */
		bot.toolbarDropDownButton("h:inputText").click(); //$NON-NLS-1$
		
		String line = editor.toTextEditor().getSelection();
		line = line.replaceAll("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
		line = line.replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
		line = line.replaceAll("\t", ""); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("<h:inputText> should be selected", SELECTED_TEXT, line); //$NON-NLS-1$
		
		bot.toolbarDropDownButton("h:inputText").menuItem("f:validateLength").click(); //$NON-NLS-1$ //$NON-NLS-2$
		line = editor.toTextEditor().getSelection();
		assertEquals("<f:validateLength> should be selected", SELECTED_TEXT2, line); //$NON-NLS-1$
	}
	
}
