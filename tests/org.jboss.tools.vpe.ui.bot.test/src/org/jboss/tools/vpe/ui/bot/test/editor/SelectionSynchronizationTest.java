/*******************************************************************************
 * Copyright (c) 2007-2012 Exadel, Inc. and Red Hat, Inc.
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
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMNode;

public class SelectionSynchronizationTest extends VPEEditorTestCase {

	private SWTBotExt botExt = null;
	private SWTBotEditorExt jspTextEditor;
	private SWTBotWebBrowser webBrowser;
	private final String SIMPLE_TEXT = "Some Plain Text Here"; //$NON-NLS-1$
	private final int SELECTION_LENGTH = 7;

	public SelectionSynchronizationTest() {
		super();
		botExt = new SWTBotExt();
	}

	public void testSelectionSynchronization() throws Throwable {
		/*
		 * Open test page
		 */
		openPage();
		jspTextEditor = botExt.swtBotEditorExtByTitle(TEST_PAGE); 
		setEditor(jspTextEditor);
		webBrowser = new SWTBotWebBrowser(TEST_PAGE, botExt);
		setEditorText(getEditor().getText());
		getEditor().setFocus();
		Display d = bot.getDisplay();
		
		jspTextEditor.setFocus();
		jspTextEditor.navigateTo(13,0);
		jspTextEditor.typeText(SIMPLE_TEXT);
		jspTextEditor.navigateTo(13,0);

		KeyboardHelper.selectTextUsingSWTEvents(d, true, SELECTION_LENGTH);
		util.sleep(TIME_1S);
		String selectionText = webBrowser.getSelectionText();
		assertEquals("Step 1. Selected text in the Visual Part is wrong: ", "Some Pl", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
		KeyboardHelper.pressKeyCode(d, SWT.ARROW_RIGHT);
		KeyboardHelper.pressKeyCode(d, SWT.ARROW_RIGHT);
		KeyboardHelper.selectTextUsingSWTEvents(d, false, SELECTION_LENGTH);
		util.sleep(TIME_1S);
		selectionText = webBrowser.getSelectionText();
		assertEquals("Step 2. Selected text in the Visual Part is wrong: ", "me Plai", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * Select text in Web Browser
		 */
		webBrowser.setFocus();
		nsIDOMNode node = webBrowser.getDomNodeByTagName("SPAN", 1); //$NON-NLS-1$
		webBrowser.setFocus();
		webBrowser.selectDomNode(node, 0);
		KeyboardHelper.pressKeyCode(d, SWT.ARROW_RIGHT);
		KeyboardHelper.pressKeyCode(d, SWT.ARROW_RIGHT);

		KeyboardHelper.selectTextUsingSWTEvents(d, true, SELECTION_LENGTH);
		util.sleep(TIME_1S);
		selectionText = jspTextEditor.getSelection();
		assertEquals("Step 3. Selected text in the Source Part is wrong: ", "ome Pla", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
		KeyboardHelper.pressKeyCode(d, SWT.ARROW_RIGHT);
		KeyboardHelper.pressKeyCode(d, SWT.ARROW_RIGHT);
		KeyboardHelper.selectTextUsingSWTEvents(d, false, SELECTION_LENGTH);
		util.sleep(TIME_1S);
		selectionText = jspTextEditor.getSelection();
		assertEquals("Step 4. Selected text in the Source Part is wrong: ", "e Plain", selectionText); //$NON-NLS-1$ //$NON-NLS-2$

		/*
		 * Navigate to the text
		 */
		jspTextEditor.setFocus();
		jspTextEditor.navigateTo(11, 38);
		util.sleep(TIME_1S);
		jspTextEditor.navigateTo(11, 39);
		util.sleep(TIME_1S);
		/*
		 * Select some text to the right
		 */
		KeyboardHelper.selectTextUsingSWTEvents(d, true, SELECTION_LENGTH);
		util.sleep(TIME_1S);
		selectionText = webBrowser.getSelectionText();
		assertEquals("Step 5. Selected text in the Visual Part is wrong: ", "llo Dem", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
		jspTextEditor.navigateTo(11, 46);
		util.sleep(TIME_1S);
		jspTextEditor.navigateTo(11, 51);
		util.sleep(TIME_1S);
		KeyboardHelper.selectTextUsingSWTEvents(d, false, SELECTION_LENGTH);
		util.sleep(TIME_1S);
		selectionText = webBrowser.getSelectionText();
		assertEquals("Step 6. Selected text in the Visual Part is wrong: ", "emo App", selectionText); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
