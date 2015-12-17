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

public class ShowResourceBundlesUsageasELexpressionsTest extends PreferencesTestCase {

	private static TextEditor editor;

	@Test
	public void testShowResourceBundlesUsageasELexpressions() throws Throwable {
		openPage();
		editor = new TextEditor(TEST_PAGE);
		editor.setText(DEFAULT_TEST_PAGE_TEXT);
		editor.save();
		SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(TEST_PAGE);
		// Test check VPE content with resource bundles
		setShowResourceBundlesUsage(true);
		assertVisualEditorContainsNodeWithValue(webBrowser, "#{Message.prompt_message}", TEST_PAGE);
		assertVisualEditorContainsNodeWithValue(webBrowser, "#{Message.header}", TEST_PAGE);
		assertVisualEditorNotContainNodeWithValue(webBrowser, "Hello Demo Application", TEST_PAGE);
		assertVisualEditorNotContainNodeWithValue(webBrowser, "Name:", TEST_PAGE);
		// Test check VPE content without resource bundles
		setShowResourceBundlesUsage(false);
		assertVisualEditorNotContainNodeWithValue(webBrowser, "#{Message.prompt_message}", TEST_PAGE);
		assertVisualEditorNotContainNodeWithValue(webBrowser, "#{Message.header}", TEST_PAGE);
		assertVisualEditorContainsNodeWithValue(webBrowser, "Hello Demo Application", TEST_PAGE);
		assertVisualEditorContainsNodeWithValue(webBrowser, "Name:", TEST_PAGE);
	}

	@Override
	public void tearDown() throws Exception {
		// Restore page state before tests
		editor.activate();
		editor.setText(DEFAULT_TEST_PAGE_TEXT);
		editor.save();
		super.tearDown();
	}

	private void setShowResourceBundlesUsage(boolean show) {
		new DefaultToolItem("Preferences").click();
		new DefaultShell("Preferences (Filtered)");
		VisualPageEditorPreferencePage vpePreferencePage = new VisualPageEditorPreferencePage();
		vpePreferencePage.toggleShowResourceBundlesAsELExp(show);
		new OkButton().click();
	}

}
