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

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.ToolItem;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIDOMWindowInternal;

public class ScrollingSynchronizationTest extends VPEEditorTestCase {

	private final String TOOL_TIP = "Synchronize scrolling between source and visual panes";
	private static final String FACELETS_JSP = "facets.jsp";
	private TextEditor jspEditor;
	private SWTBotWebBrowser webBrowser;

	@Test
	public void testScrollingSynchronization() throws Throwable {
		/*
		 * Copy big file
		 */
		try {
			FileHelper.copyFilesBinary(new File(getPathToRootResources("WebContent/" + FACELETS_JSP)),
					new File(FileHelper.getProjectLocation(JBT_TEST_PROJECT_NAME), "WebContent/pages"));
		} catch (IOException ioe) {
			throw new RuntimeException("Unable to copy necessary files from plugin's resources directory: ", //$NON-NLS-1$
					ioe);
		}
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).refresh();
		new WaitWhile(new JobIsRunning());
		new WorkbenchShell().maximize();
		/*
		 * Open big file
		 */
		openPage(FACELETS_JSP);
		jspEditor = new TextEditor(FACELETS_JSP);
		setEditor(jspEditor);
		setEditorText(jspEditor.getText());
		webBrowser = new SWTBotWebBrowser(FACELETS_JSP);
		/*
		 * Synchronize scrolling button
		 */
		ToolItem tiSynchronize = new DefaultToolItem(TOOL_TIP);
		if (!tiSynchronize.isEnabled()) {
			tiSynchronize.click();
		}
		assertTrue("Toolbar button should be enabled", tiSynchronize.isEnabled());
		/*
		 * Test initial position
		 */
		jspEditor.setCursorPosition(0);
		Point cursorPosition = jspEditor.getCursorPosition();
		assertEquals("Source line position is wrong", 0, cursorPosition.x); //$NON-NLS-1$

		nsIDOMWindow domWindow = webBrowser.getContentDOMWindow();
		nsIDOMWindowInternal windowInternal = org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface(domWindow,
				nsIDOMWindowInternal.class);
		/*
		 * Set source position -- visual part should be scrolled.
		 */
		int scrollY = windowInternal.getScrollY();
		int halfHeight = windowInternal.getScrollMaxY() / 2;
		assertEquals("Step 1. Initital visual position is wrong", 0, scrollY); //$NON-NLS-1$
		/*
		 * Test the bottom position. Press CTRL+END to get to the end of the
		 * page.
		 */
		KeyboardFactory.getKeyboard().invokeKeyCombination( SWT.CTRL ,SWT.END );
		cursorPosition = jspEditor.getCursorPosition();
		assertEquals("Source line position is wrong", 1307, cursorPosition.x); //$NON-NLS-1$
		/*
		 * Press ARROW_UP several times to select element at the bottom
		 */
		for (int i = 0; i < 5; i++) {
			KeyboardFactory.getKeyboard().invokeKeyCombination( SWT.ARROW_UP);
		}
		cursorPosition = jspEditor.getCursorPosition();
		assertEquals("Source line position is wrong", 1302, cursorPosition.x); //$NON-NLS-1$
		scrollY = windowInternal.getScrollY();
		assertTrue("Step 2. Visual scrolling should be at the bottom of the page,\ncurrent scrolling opstion is " //$NON-NLS-1$
				+ scrollY + ", but should be more than " + halfHeight, scrollY > halfHeight); //$NON-NLS-1$
		/*
		 * Test custom scroll position in Visual Part
		 */
		jspEditor.activate();
		jspEditor.setCursorPosition(1260, 20);
		KeyboardFactory.getKeyboard().invokeKeyCombination( SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination( SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination( SWT.ARROW_RIGHT);
	
		cursorPosition = jspEditor.getCursorPosition();
		assertEquals("Step 3. Source line position is wrong", 1260, cursorPosition.x); //$NON-NLS-1$

		webBrowser.setFocus();

		for (int i = 0; i < 14; i++) {
			KeyboardFactory.getKeyboard().invokeKeyCombination( SWT.ARROW_UP);
		}
		KeyboardFactory.getKeyboard().invokeKeyCombination( SWT.ARROW_LEFT);
		cursorPosition = jspEditor.getCursorPosition();
		assertEquals("Step 4. Source line position is wrong", 996, cursorPosition.x); //$NON-NLS-1$
	}
}
