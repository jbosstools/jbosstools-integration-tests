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

import org.eclipse.swt.SWT;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

/**
 * Test Saving Changes To File
 * 
 * @author Vladimir Pakan
 *
 */
public class CheckFileChangesSaving {
	/**
	 * Insert changeText to file in editor, close file, save file dependent on
	 * saveFile input parameter reopen file and check if change was saved or not
	 * 
	 * @param editor
	 * @param tree
	 * @param fileTreeItem
	 * @param changeText
	 * @param saveFile
	 */
	public static String checkIt(TextEditor editor, ProjectItem fileProjectItem, String changeText, boolean saveFile) {
		String result = null;
		// Test Saving
		editor.insertText(0, changeText);
		editor.setCursorPosition(0);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		new ShellMenu("File", "Close All").select();
		new DefaultShell("Save Resource");
		new PushButton(saveFile ? "Yes" : "No").click();
		// Reopen Test File
		fileProjectItem.open();
		TextEditor reopenedTextEditor = new TextEditor(fileProjectItem.getText());
		if (saveFile && !reopenedTextEditor.getText().startsWith(changeText)) {
			result = fileProjectItem.getText() + " was not saved properly.";
		} else if (!saveFile && reopenedTextEditor.getText().startsWith(changeText)) {
			result = fileProjectItem.getText() + " was saved even it should not be.";
		}

		return result;

	}
}
