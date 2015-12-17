/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;

public class ToggleCommentTest extends VPEEditorTestCase {
	private TextEditor textEditor;
	private String originalEditorText;
	@Test
	public void testToggleComment() throws Throwable {
		// Test open page
		openPage();
		textEditor = new TextEditor(TEST_PAGE);
		originalEditorText = DEFAULT_TEST_PAGE_TEXT;
		textEditor.setText(DEFAULT_TEST_PAGE_TEXT);
		textEditor.save();
		// Test toggle comment from Source menu
		textEditor.setCursorPosition(16, 20);
		new ShellMenu("Source", "Toggle Comment").select();
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		Browser browser = new InternalBrowser();
		assertVisualEditorContainsManyComments(browser, 2,TEST_PAGE);
		final String commentValue = "<h:commandButton action=\"hello\" value=\"Say Hello!\" />";
		assertVisualEditorContainsCommentWithValue(browser,commentValue);
		// Test untoggle comment from Source menu
		new ShellMenu("Source", "Toggle Comment").select();
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		assertVisualEditorContainsManyComments(browser, 1 , TEST_PAGE);
		// Test toggle comment with CTRL+SHIFT+C hot keys
		textEditor.activate();
		textEditor.setCursorPosition(16, 20);
		pressToggleCommentHotKeys();
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		assertVisualEditorContainsManyComments(browser, 2, TEST_PAGE);
		assertVisualEditorContainsCommentWithValue(browser,commentValue);
		// Test untoggle comment with CTRL+SHIFT hot keys
		textEditor.setCursorPosition(16, 20);
		pressToggleCommentHotKeys();
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		assertVisualEditorContainsManyComments(browser, 1, TEST_PAGE);
	}

	private void pressToggleCommentHotKeys() {
		if (RunningPlatform.isOSX()) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, SWT.COMMAND, 'c');
		} else {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.SHIFT, SWT.CTRL, 'c');
		}
	}
	@Override
	public void tearDown() throws Exception {
		if (textEditor != null) {
			textEditor.setText(originalEditorText);
			textEditor.save();
			textEditor.close();
		}
		super.tearDown();
	}
}
