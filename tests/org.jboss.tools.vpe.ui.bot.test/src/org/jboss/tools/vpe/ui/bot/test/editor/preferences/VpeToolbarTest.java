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
package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import java.awt.event.KeyEvent;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class VpeToolbarTest extends PreferencesTestCase {
	
	private SWTBotEclipseEditor jspEditor;
	private SWTBotExt botExt = null;
	
	public VpeToolbarTest() {
		super();
		botExt = new SWTBotExt();
	}

	public void testVpeToolbarVisibility_JBIDE11302() {
		openPage();
		/*
		 * Select some text
		 */
		jspEditor = botExt.editorByTitle(VPEAutoTestCase.TEST_PAGE).toTextEditor();
		jspEditor.navigateTo(6, 28);
		bot.sleep(Timing.time1S());
		/*
		 * Send key press event to fire VPE listeners
		 */
		KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
		bot.sleep(Timing.time1S());
		/*
		 * Check that tool items is available.
		 */
		try {
			bot.toolbarToggleButtonWithTooltip(SCROLL_LOCK_TOOLTIP);
		} catch (WidgetNotFoundException  e) {
			fail("Cannot find \""+ SCROLL_LOCK_TOOLTIP + "\" button on startup"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		try {
			bot.toolbarButtonWithTooltip(I18N_BUTTON_TOOLTIP);
		} catch (WidgetNotFoundException  e) {
			fail("Cannot find \""+ I18N_BUTTON_TOOLTIP + "\" button on startup"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		/*
		 * Change preferences.
		 */
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		SWTBotCheckBox chbShowVpeToolbar = bot.checkBox(SHOW_VISUAL_TOOLBAR_PREFS_CHECKBOX_NAME);
		/*
		 * Toggle the selection
		 */
		if (!chbShowVpeToolbar.isChecked()) {
			chbShowVpeToolbar.click();
		}
		bot.button("OK").click(); //$NON-NLS-1$
		/*
		 * Check that tool items is available.
		 */
		bot.sleep(Timing.time1S());
		try {
			bot.toolbarToggleButtonWithTooltip(SCROLL_LOCK_TOOLTIP);
		} catch (WidgetNotFoundException  e) {
			fail("Cannot find \""+ SCROLL_LOCK_TOOLTIP + "\" button after VPE toolbar visibility has been changed"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		try {
			bot.toolbarButtonWithTooltip(I18N_BUTTON_TOOLTIP1);
		} catch (WidgetNotFoundException  e) {
			fail("Cannot find \""+ I18N_BUTTON_TOOLTIP1 + "\" button after VPE toolbar visibility has been changed"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		closePage();
	}
}
