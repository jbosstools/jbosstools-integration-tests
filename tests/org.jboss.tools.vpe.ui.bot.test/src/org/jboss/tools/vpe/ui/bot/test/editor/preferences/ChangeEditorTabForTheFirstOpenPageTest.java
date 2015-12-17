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

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardDialog;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardJSPPage;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.junit.Test;

public class ChangeEditorTabForTheFirstOpenPageTest extends PreferencesTestCase {
	@Test
	public void testChangeEditorTabForTheFirstOpenPage() {
		// Test set default source tab
		openPage();
		new TextEditor(TEST_PAGE);
		new DefaultToolItem("Preferences").click();
		new DefaultShell("Preferences (Filtered)");
		new VisualPageEditorPreferencePage().setDefaultActiveEditorTab("Source");
		new OkButton().click();
		// Create and open new page
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages").select();
		NewJSPFileWizardDialog newJSPFileWizardDialog = new NewJSPFileWizardDialog();
		newJSPFileWizardDialog.open();
		new NewJSPFileWizardJSPPage().setFileName("testPage");
		newJSPFileWizardDialog.finish();
		packageExplorer.activate();
		// Check if the tab changed trying to find Refresh toolbar button of
		// visual editor
		Exception exception = null;
		try {
			new DefaultToolItem("Refresh").click();
		} catch (CoreLayerException cle) {
			exception = cle;
		}
		assertNotNull("CoreLayerException was expected", exception);
	}

	@Override
	public void tearDown() throws Exception {
		// Delete test page if it has been created
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", "testPage.jsp")
				.delete();
		new TextEditor(TEST_PAGE);
		new DefaultToolItem("Preferences").click();
		new DefaultShell("Preferences (Filtered)");
		new VisualPageEditorPreferencePage().setDefaultActiveEditorTab("Visual/Source");
		new OkButton().click();
		super.tearDown();
	}

}
