/******************************************************************************* 
 * Copyright (c) 2012 - 2016 Red Hat, Inc.
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
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;

public class BlockCommentTest extends VPEEditorTestCase {
	private TextEditor textEditor;
	private String originalEditorText;

	@Test
	public void testBlockComment() throws Throwable {
		// Test open page
		openPage();
		textEditor = new TextEditor(TEST_PAGE);
		originalEditorText = textEditor.getText();
		// Test add block comment from Source menu
		final String commentValue = "<h:commandButton action=\"hello\" value=\"Say Hello!\" />";
		textEditor.selectText(commentValue);
		new ShellMenu("Source", "Add Block Comment").select();
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		Browser browser = new InternalBrowser();
		assertVisualEditorContainsManyComments(browser, 2, TEST_PAGE);
		assertVisualEditorContainsCommentWithValue(browser, commentValue);
		// Test remove block comment from Source menu
		textEditor.selectText(commentValue);
		textEditor.selectLine(textEditor.getCursorPosition().x);
		new ShellMenu("Source", "Remove Block Comment").select();
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		assertVisualEditorContainsManyComments(browser, 1, TEST_PAGE);
		// Test add block comment with CTRL+SHIFT+/ hot keys
		textEditor.selectText(commentValue);
		pressBlockCommentHotKeys();
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		assertVisualEditorContainsManyComments(browser, 2, TEST_PAGE);
		assertVisualEditorContainsCommentWithValue(browser, commentValue);
		// Test remove block comment with CTRL+SHIFT+\ hot keys
		textEditor.selectText(commentValue);
		textEditor.selectLine(textEditor.getCursorPosition().x);
		pressUnBlockCommentHotKeys();
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		assertVisualEditorContainsManyComments(browser, 1, TEST_PAGE);
	}

	private void pressBlockCommentHotKeys() {
		if (RunningPlatform.isOSX()) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.COMMAND, '/');
		} else {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SHIFT, '/');
		}
	}

	private void pressUnBlockCommentHotKeys() {
		if (RunningPlatform.isOSX()) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.COMMAND, '\\');
		} else {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SHIFT, '\\');
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
