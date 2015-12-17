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
package org.jboss.tools.vpe.ui.bot.test.editor;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNode;

public class SelectionSynchronizationTest extends VPEEditorTestCase {

	private TextEditor jspTextEditor;
	private SWTBotWebBrowser webBrowser;
	private final String SIMPLE_TEXT = "Some Plain Text Here"; //$NON-NLS-1$
	private final int SELECTION_LENGTH = 7;

	@Test
	public void testSelectionSynchronization() {
		/*
		 * Open test page
		 */
		openPage();
		jspTextEditor = new TextEditor(TEST_PAGE);
		setEditor(jspTextEditor);
		webBrowser = new SWTBotWebBrowser(TEST_PAGE);
		setEditorText(getEditor().getText());
		getEditor().activate();

		jspTextEditor.activate();
		jspTextEditor.setCursorPosition(13, 0);
		KeyboardFactory.getKeyboard().type(SIMPLE_TEXT);
		jspTextEditor.setCursorPosition(13, 0);

		SelectionSynchronizationTest.selectTextViaKeyboard(true, SELECTION_LENGTH);
		AbstractWait.sleep(TimePeriod.SHORT);
		String selectionText = webBrowser.getSelectionText();
		assertEquals("Step 1. Selected text in the Visual Part is wrong: ", "Some Pl", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		SelectionSynchronizationTest.selectTextViaKeyboard(false, SELECTION_LENGTH);
		AbstractWait.sleep(TimePeriod.SHORT);
		selectionText = webBrowser.getSelectionText();
		assertEquals("Step 2. Selected text in the Visual Part is wrong: ", "me Plai", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * Select text in Web Browser
		 */
		webBrowser.setFocus();
		nsIDOMNode node = webBrowser.getDomNodeByTagName("SPAN", 1); //$NON-NLS-1$
		webBrowser.selectDomNode(node, 0);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);

		SelectionSynchronizationTest.selectTextViaKeyboard(true, SELECTION_LENGTH);
		AbstractWait.sleep(TimePeriod.SHORT);
		selectionText = jspTextEditor.getSelectedText();
		assertEquals("Step 3. Selected text in the Source Part is wrong: ", "ome Pla", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		SelectionSynchronizationTest.selectTextViaKeyboard(false, SELECTION_LENGTH);
		AbstractWait.sleep(TimePeriod.SHORT);
		selectionText = jspTextEditor.getSelectedText();
		assertEquals("Step 4. Selected text in the Source Part is wrong: ", "e Plain", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static void selectTextViaKeyboard(boolean forward, int selectionLength) {
		int arrowCode = forward ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
		for (int index = 0; index < selectionLength; index++) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, arrowCode);
		}
	}

}
