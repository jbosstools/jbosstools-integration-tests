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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class Jbide10020_TestHotKeyForVpeRefresh extends VPEAutoTestCase {

	private final String TOOL_TIP = "Refresh (F5)";  //$NON-NLS-1$
	private final String TOOL_TIP2 = "Refresh";  //$NON-NLS-1$
	private final String ERROR_MESSAGE = 
			"Could not find tool bar button with tooltip '" + TOOL_TIP  //$NON-NLS-1$
			+ "' on the toolbar. Hot key 'F5' for VPE refresh is broken.";  //$NON-NLS-1$
	
	public Jbide10020_TestHotKeyForVpeRefresh() {
		super();
	}

	@Override
	protected void closeUnuseDialogs() {
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}

	public void testHotKeyForVpeRefresh() {
		SWTBotEditor editor = SWTTestExt.packageExplorer.openFile(
				JBT_TEST_PROJECT_NAME, "WebContent", "pages", TEST_PAGE); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * Case 1:
		 * When focus is on in the VPE --
		 * Find "Refresh (F5)" toolbar button
		 */
		editor.setFocus();
		try {
			bot.toolbarButtonWithTooltip(TOOL_TIP);
		} catch (Exception e) {
			fail(ERROR_MESSAGE);
		}
		/*
		 * Case 2:
		 * When focus is on in Package Explorer --
		 * Find "Refresh" toolbar button without HotKey defined.
		 */
		packageExplorer.show();
		try {
			bot.toolbarButtonWithTooltip(TOOL_TIP2);
		} catch (Exception e) {
			fail("Hot key for VPE refresh is *NOT* disabled in Package Explorer"); //$NON-NLS-1$
		}
	}
}
