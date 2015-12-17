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
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

public class ShowNonVisualTagsTest extends PreferencesTestCase {
	@Test
	public void testShowNonVisualTags() throws Throwable {
		openPage();
		// Test Show Non-Visual Tags
		setShowNonVisual(true);
		closePage();
		openPage();
		SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(TEST_PAGE);
		assertVisualEditorContainsNodeWithValue(webBrowser, "jsp:directive.taglib", TEST_PAGE);
		assertVisualEditorContainsNodeWithValue(webBrowser, "f:loadBundle", TEST_PAGE);
		// Test Hide Non-Visual Tags
		setShowNonVisual(false);
		closePage();
		openPage();
		webBrowser = new SWTBotWebBrowser(TEST_PAGE);
		assertVisualEditorNotContainNodeWithValue(webBrowser, "jsp:directive.taglib", TEST_PAGE);
		assertVisualEditorNotContainNodeWithValue(webBrowser, "f:loadBundle", TEST_PAGE);
	}

	private void setShowNonVisual(boolean show) {
		new DefaultToolItem("Preferences").click();
		new DefaultShell("Preferences (Filtered)");
		new VisualPageEditorPreferencePage().toggleShowNonVisualTag(show);
		new OkButton().click();
	}

}
