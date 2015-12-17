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
package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

public class VpeToolbarTest extends PreferencesTestCase {
	
	@Test
	public void testVpeToolbarVisibility_JBIDE11302() {
		openPage();
		/*
		 * Select some text
		 */
		TextEditor jspEditor = new TextEditor(VPEAutoTestCase.TEST_PAGE);
		jspEditor.setCursorPosition(6, 28);
		/*
		 * Send key press event to fire VPE listeners
		 */
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		/*
		 * Check that tool items is available.
		 */
		try {
			new DefaultToolItem(SCROLL_LOCK_TOOLTIP);
		} catch (CoreLayerException  cle) {
			fail("Cannot find \""+ SCROLL_LOCK_TOOLTIP + "\" button on startup"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		try {
			new DefaultToolItem(I18N_BUTTON_TOOLTIP);
		} catch (CoreLayerException  cle) {
			fail("Cannot find \""+ I18N_BUTTON_TOOLTIP + "\" button on startup"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		closePage();
	}
}
