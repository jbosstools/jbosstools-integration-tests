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
package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

public class BorderForUnknownTagsTest extends PreferencesTestCase {

	private static String originalEditorText;
	private static TextEditor editor;
	@Test
	public void testBorderForUnknownTags() throws Throwable {
		// Test open page
		openPage();
		editor = new TextEditor(TEST_PAGE);
		originalEditorText = editor.getText();
		// Test insert unknown tag
		final String unknownTag = "tagunknown";
		editor.insertText(editor.getPositionOfText("<f:view>") + "<f:view>".length(),"<" + unknownTag + "></" + unknownTag + ">"); //$NON-NLS-1$
		editor.save();
		// Test default Show Border value
		new DefaultToolItem("Preferences").click();
		new DefaultShell("Preferences (Filtered)");
		VisualPageEditorPreferencePage vpePreferencePage = new VisualPageEditorPreferencePage();
		if (!vpePreferencePage.isShowBorderForUnknownTags()) {
			vpePreferencePage.toggleShowBorderForUnknownTags(true);
		}
		new OkButton().click();
		// Test check VPE content
		SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(TEST_PAGE);
		assertVisualEditorContains(webBrowser, "DIV", new String[] { "style", "title" },
				new String[] { "-moz-user-modify: read-only; border: 1px solid green;", unknownTag }, TEST_PAGE);
		// Test hide border for unknown tag
		setShowBorder(false);
		assertVisualEditorContains(webBrowser, "DIV", new String[] { "style", "title" },
				new String[] { "-moz-user-modify: read-only;", unknownTag }, TEST_PAGE);
		// Test restore previous state
		setShowBorder(true);
		assertVisualEditorContains(webBrowser, "DIV", new String[] { "style", "title" },
				new String[] { "-moz-user-modify: read-only; border: 1px solid green;", unknownTag }, TEST_PAGE);
	}

	private void setShowBorder(boolean show) {
		new DefaultToolItem("Preferences").click();
		new DefaultShell("Preferences (Filtered)");
		VisualPageEditorPreferencePage vpePreferencePage = new VisualPageEditorPreferencePage();
		vpePreferencePage.toggleShowBorderForUnknownTags(show);
		new OkButton().click();
	}
	@Override
	public void tearDown() throws Exception {
		// Restore page state before tests
		editor.setText(originalEditorText);
		editor.save();
		super.tearDown();
	}

}
