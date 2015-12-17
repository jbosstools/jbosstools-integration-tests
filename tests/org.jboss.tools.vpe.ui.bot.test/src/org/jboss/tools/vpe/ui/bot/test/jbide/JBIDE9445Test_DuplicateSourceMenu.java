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
package org.jboss.tools.vpe.ui.bot.test.jbide;

import org.eclipse.swt.widgets.MenuItem;
import org.jboss.reddeer.core.handler.MenuHandler;
import org.jboss.reddeer.core.lookup.MenuLookup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

public class JBIDE9445Test_DuplicateSourceMenu extends VPEAutoTestCase {
	@Test
	public void testDuplicateMenus() {
		/*
		 * Open the default jsp page
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		/*
		 * When focus is on the editor -- only one 'Source' menu should be
		 * available
		 */
		assertTrue(new ShellMenu("Source").isEnabled());
		assertFalse("Second 'Source' menu is enabled, but shouldn't be", hasSecondSourceMenu());
		/*
		 * Set focus to the PackageExplorer
		 */
		packageExplorer.open();
		/*
		 * After focus moved to Package Explorer -- still only one menu should
		 * be visible
		 */
		assertTrue(new ShellMenu("Source").isEnabled());
		assertFalse("Second 'Source' menu is enabled, but shouldn't be", hasSecondSourceMenu());

	}

	private boolean hasSecondSourceMenu() {
		MenuItem[] topMenuItems = MenuLookup.getInstance().getActiveShellTopMenuItems();
		int sourceMenuOccurences = 0;
		int index = 0;
		while (index < topMenuItems.length && sourceMenuOccurences < 2) {
			if (MenuHandler.getInstance().getMenuItemText(topMenuItems[index]).equalsIgnoreCase("&Source")) {
				sourceMenuOccurences++;
			}
			index++;
		}

		return sourceMenuOccurences == 2;
	}
}
