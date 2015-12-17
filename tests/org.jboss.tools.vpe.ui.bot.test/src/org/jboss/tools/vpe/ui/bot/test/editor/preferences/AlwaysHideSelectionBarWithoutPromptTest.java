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
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.junit.Test;

public class AlwaysHideSelectionBarWithoutPromptTest extends PreferencesTestCase {

	private static final String HID_SEL_BAR = "Hide selection bar"; //$NON-NLS-1$
	@Test
	public void testAlwaysHideSelectionBarWithoutPrompt() {
		// Test Hide Selection Bar
		openPage();
		setToggleSelectionBar(false);
		checkIsHide();
		// Test Hide selection after reopen
		closePage();
		openPage();
		checkIsHide();
		// Test Show Selection Bar
		setToggleSelectionBar(true);
		checkIsShow();
		// Test Show Selection Bar after reopen
		closePage();
		openPage();
		checkIsShow();
		// Test Hide Selection Bar button without confirm
		new DefaultToolItem(HID_SEL_BAR).click();
		checkIsHide();
		// Test Show selection after reopen
		closePage();
		openPage();
		checkIsHide();
	}

	private void checkIsHide() {
		checkIsHide("Toolbar button " + HID_SEL_BAR + " is not hidden");
	}

	private void setToggleSelectionBar(boolean show) {
		new DefaultToolItem(TOGGLE_SELECTION_BAR_TOOLTIP).toggle(show);
	}

	private void checkIsHide(String message) {
		CoreLayerException exception = null;
		try {
			new DefaultToolItem(HID_SEL_BAR);
		} catch (CoreLayerException e) {
			exception = e;
		}
		assertNotNull(message, exception);
	}

	private void checkIsShow() {
		CoreLayerException exception = null;
		try {
			new DefaultToolItem(HID_SEL_BAR);
		} catch (CoreLayerException e) {
			exception = e;
		}
		assertNull(exception);
	}

}
