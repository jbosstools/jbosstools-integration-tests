/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.jbide;

import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class JBIDE9445Test_DuplicateSourceMenu extends VPEAutoTestCase {

	public JBIDE9445Test_DuplicateSourceMenu() {
		super();
	}
	 
	public void testDuplicateMenus() {
		/*
		 * Open the default jsp page
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open(); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * When focus is on the editor --
		 * only one 'Source' menu should be available
		 */
		assertTrue(bot.menu("Source", 0).isVisible()); //$NON-NLS-1$
		try {
			assertFalse("Second 'Source' menu is enabled, but shouldn't be",  //$NON-NLS-1$
					bot.menu("Source", 1).isEnabled()); //$NON-NLS-1$
		} catch (Exception e) { }
		/*
		 * Set focus to the PackageExplorer
		 */
		packageExplorer.open();
		/*
		 * After focus moved to Package Explorer --
		 * still only one menu should be visible
		 */
		assertTrue(bot.menu("Source", 0).isEnabled()); //$NON-NLS-1$
		try {
			assertFalse("Second 'Source' menu is enabled, but shouldn't be",  //$NON-NLS-1$
					bot.menu("Source", 1).isEnabled()); //$NON-NLS-1$
		} catch (Exception e) { }
	}

	@Override
	protected void closeUnuseDialogs() { }

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
}
