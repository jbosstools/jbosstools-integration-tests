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

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.utils.Position;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarToggleButton;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIDOMWindowInternal;

public class ScrollingSynchronizationTest extends VPEEditorTestCase {

	private final String TOOL_TIP = "Synchronize scrolling between source and visual panes"; //$NON-NLS-1$
	private static final String FACELETS_JSP = "facets.jsp"; //$NON-NLS-1$
	private SWTBotExt botExt = null;
	private SWTBotEditorExt jspEditor;
	private SWTBotWebBrowser webBrowser;

	public ScrollingSynchronizationTest() {
		super();
		botExt = new SWTBotExt();
	}

	public void testScrollingSynchronization() throws Throwable {
		/*
		 * Copy big file
		 */
		try {
			FileHelper.copyFilesBinary(
					new File(getPathToRootResources(IDELabel.JsfProjectTree.WEB_CONTENT + "/" + FACELETS_JSP)), //$NON-NLS-1$
					new File(FileHelper.getProjectLocation(JBT_TEST_PROJECT_NAME, bot),
							IDELabel.JsfProjectTree.WEB_CONTENT + "/" + IDELabel.JsfProjectTree.PAGES)); //$NON-NLS-1$
		} catch (IOException ioe) {
			throw new RuntimeException(
					"Unable to copy necessary files from plugin's resources directory: ", //$NON-NLS-1$
					ioe);
		}
		bot.menu(IDELabel.Menu.FILE).menu(IDELabel.Menu.REFRESH).click();
		util.waitForAll();
		eclipse.maximizeActiveShell();
		util.sleep(TIME_1S);
		/*
		 * Open big file
		 */
		openPage(FACELETS_JSP);
		util.waitForAll();
		jspEditor = botExt.swtBotEditorExtByTitle(FACELETS_JSP); 
		setEditor(jspEditor);
		setEditorText(jspEditor.getText());
		webBrowser = new SWTBotWebBrowser(FACELETS_JSP, botExt);
		/*
		 * Synchronize scrolling button
		 */
		SWTBotToolbarToggleButton button = botExt.toolbarToggleButtonWithTooltip(TOOL_TIP);
		if (!button.isEnabled()) {
			button.click();
			util.sleep(TIME_1S);
		}
		assertTrue("Toolbar button should be enabled", button.isEnabled()); //$NON-NLS-1$
		Display d = bot.getDisplay();

		/*
		 * Test initial position
		 */
		jspEditor.deselectAndSetCursorPosition(0, 0);
		util.sleep(TIME_1S);
		Position cursorPosition = jspEditor.cursorPosition();
		assertEquals("Source line position is wrong", 0, cursorPosition.line); //$NON-NLS-1$

		nsIDOMWindow domWindow = webBrowser.getContentDOMWindow();
		nsIDOMWindowInternal windowInternal = org.jboss.tools.vpe.xulrunner.util.XPCOM
				.queryInterface(domWindow, nsIDOMWindowInternal.class);
		/*
		 * Set source position -- visual part should be scrolled.
		 */
		int scrollY = windowInternal.getScrollY();
		int halfHeight = windowInternal.getScrollMaxY()/2;
		assertEquals("Step 1. Initital visual position is wrong", 0, scrollY); //$NON-NLS-1$
		/*
		 * Test the bottom position.
		 * Press CTRL+END to get to the end of the page.
		 */
		jspEditor.setFocus();
		KeyboardHelper.typeKeyCodeUsingSWT(d, SWT.END, SWT.CTRL);
		util.sleep(TIME_1S);
		cursorPosition = jspEditor.cursorPosition();
		assertEquals("Source line position is wrong", 1307, cursorPosition.line); //$NON-NLS-1$
		/*
		 * Press ARROW_UP several times to select element at the bottom
		 */
		for (int i = 0; i < 5; i++) {
			KeyboardHelper.pressKeyCode(d, SWT.ARROW_UP);
			util.sleep(TIME_1S);
		}
		cursorPosition = jspEditor.cursorPosition();
		assertEquals("Source line position is wrong", 1302, cursorPosition.line); //$NON-NLS-1$
		scrollY = windowInternal.getScrollY();
		assertTrue("Step 2. Visual scrolling should be at the bottom of the page,\ncurrent scrolling opstion is "  //$NON-NLS-1$
				+ scrollY + ", but should be more than " + halfHeight, scrollY > halfHeight); //$NON-NLS-1$
		/*
		 * Test custom scroll position in Visual Part
		 */
		jspEditor.navigateTo(1260, 20);
		KeyboardHelper.selectTextUsingSWTEvents(d, true, 3);
		util.sleep(TIME_1S);
		cursorPosition = jspEditor.cursorPosition();
		assertEquals("Step 3. Source line position is wrong", 1260, cursorPosition.line); //$NON-NLS-1$

		webBrowser.setFocus();
		util.sleep(TIME_1S);
		for (int i = 0; i < 14; i++) {
			KeyboardHelper.pressKeyCode(d, SWT.ARROW_UP);
			util.sleep(TIME_1S);
		}
		KeyboardHelper.pressKeyCode(d, SWT.ARROW_LEFT);
		util.sleep(TIME_1S);
		cursorPosition = jspEditor.cursorPosition();
		assertEquals("Step 4. Source line position is wrong", 996, cursorPosition.line); //$NON-NLS-1$
	}
}
