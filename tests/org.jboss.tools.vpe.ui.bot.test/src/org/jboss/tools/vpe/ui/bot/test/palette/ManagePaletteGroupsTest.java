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
package org.jboss.tools.vpe.ui.bot.test.palette;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.vpe.reddeer.view.JBTPaletteView;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests Showing/hiding JBoss Tools Palette Groups
 * 
 * @author vlado pakan
 *
 */
public class ManagePaletteGroupsTest extends VPEAutoTestCase {

	private static final String TEST_PALETTE_GROUP_LABEL = "JSF HTML";
	private static final String TEST_PALETTE_TREE_GROUP_LABEL = "JSF";

	public ManagePaletteGroupsTest() {
		super();
	}
	@Test
	public void testManagePaletteGroups() {

		openPage();
		openPalette();
		hideShowPaletteGroup();
		// Put palette changes back
		hideShowPaletteGroup();

	}

	/**
	 * Hide or Show Pallete Group dependent on current Palette Group visibility
	 */
	private void hideShowPaletteGroup() {
		new JBTPaletteView().clickShowHideToolItem();;
		new DefaultShell("Show/Hide Drawers");
		TreeItem tiTestPaletteGroup = new DefaultTreeItem(ManagePaletteGroupsTest.TEST_PALETTE_TREE_GROUP_LABEL);
		if (tiTestPaletteGroup.isChecked()) {
			// Check Palette Group hiding
			tiTestPaletteGroup.setChecked(false);
			new OkButton().click();
			assertTrue(
					"Palette Group " + ManagePaletteGroupsTest.TEST_PALETTE_GROUP_LABEL
							+ " has to be hidden but is visible.",
					!SWTBotWebBrowser.paletteContainsRootPaletteCotnainer(ManagePaletteGroupsTest.TEST_PALETTE_GROUP_LABEL));
		} else {
			// Check Palette Group showing
			tiTestPaletteGroup.setChecked(true);
			new OkButton().click();;
			assertTrue(
					"Palette Group " + ManagePaletteGroupsTest.TEST_PALETTE_GROUP_LABEL
							+ " has to be visible but is hidden.",
					SWTBotWebBrowser.paletteContainsRootPaletteCotnainer(ManagePaletteGroupsTest.TEST_PALETTE_GROUP_LABEL));
		}
	}
}
