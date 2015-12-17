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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;

public abstract class PreferencesTestCase extends VPEEditorTestCase {

	protected static final String TOGGLE_SELECTION_BAR_TOOLTIP = "Toggle selection tag bar"; //$NON-NLS-1$
	protected static final String PREF_TOOLTIP = "Preferences"; //$NON-NLS-1$
	protected static final String PREF_FILTER_SHELL_TITLE = "Preferences (Filtered)"; //$NON-NLS-1$
	protected static final String SCROLL_LOCK_TOOLTIP = "Synchronize scrolling between source and visual panes"; //$NON-NLS-1$
	protected static final String I18N_BUTTON_TOOLTIP1 = "Externalize selected string..."; //$NON-NLS-1$
	protected static final String I18N_BUTTON_TOOLTIP = "Externalize selected string..."; //$NON-NLS-1$
	protected static final String SHOW_VISUAL_TOOLBAR_PREFS_CHECKBOX_NAME = "Show toolbar within the editor (otherwise in Eclipse's toolbar)"; //$NON-NLS-1$
	protected static final String SHOW_SELECTION_TAG_BAR = "Show selection tag bar"; //$NON-NLS-1$

	void closePage() {
		new TextEditor(TEST_PAGE).close();
	}

	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile() //$NON-NLS-1$
				+ "resources/preferences/" + testPage; //$NON-NLS-1$
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile() //$NON-NLS-1$
					+ "preferences/" + testPage; //$NON-NLS-1$
		}
		return filePath;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		openPage();
		setPreferencesToDefault(true);
	}

	@Override
	public void tearDown() throws Exception {
		openPage();
		setPreferencesToDefault(true);
		super.tearDown();
	}

	void setPreferencesToDefault(boolean fromEditor) throws CoreLayerException {
		VisualPageEditorPreferencePage vpePreferencePage = new VisualPageEditorPreferencePage();
		WorkbenchPreferenceDialog preferenceDialog = null;
		if (fromEditor) {
			new DefaultToolItem("Preferences").click();
			new DefaultShell("Preferences (Filtered)");
		} else {
			preferenceDialog = new WorkbenchPreferenceDialog();
			preferenceDialog.open();
			preferenceDialog.select(vpePreferencePage);
		}
		if (!vpePreferencePage.isShowSelectionTagBar()) {
			vpePreferencePage.toggleShowSelectionTagBar(true);
		}
		if (!vpePreferencePage.isShowBorderForUnknownTags()) {
			vpePreferencePage.toggleShowBorderForUnknownTags(true);
		}
		if (vpePreferencePage.isShowNonVisualTag()) {
			vpePreferencePage.toggleShowNonVisualTag(false);
		}
		if (vpePreferencePage.isShowResourceBundlesAsELExp()) {
			vpePreferencePage.toggleShowResourceBundlesAsELExp(false);
		}
		if (!vpePreferencePage.isAskForAttrsDuringTagInsert()) {
			vpePreferencePage.toggleAskForAttrsDuringTagInsert(true);
		}
		if (!vpePreferencePage.isShowTextFormattingBar()) {
			vpePreferencePage.toggleShowTextFormattingBar(false);
		}
		vpePreferencePage.setDefaultActiveEditorTab("Visual/Source");
		vpePreferencePage.seVisualSourceEditorsSplitting("Vertical splitting with Source Editor on the top");
		if (preferenceDialog != null) {
			preferenceDialog.ok();
		}
		else{
			new OkButton().click();
		}
	}

}
