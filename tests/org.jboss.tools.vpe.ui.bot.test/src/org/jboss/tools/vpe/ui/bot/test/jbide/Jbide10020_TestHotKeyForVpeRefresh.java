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

import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Ignore;
import org.junit.Test;

public class Jbide10020_TestHotKeyForVpeRefresh extends VPEAutoTestCase {

	private final String TOOL_TIP = RunningPlatform.isOSX() ? "Refresh (⌘R)" : "Refresh";
	private final String ERROR_MESSAGE = "Could not find tool bar button with tooltip '" + TOOL_TIP //$NON-NLS-1$
			+ "' on the toolbar. Hot key 'F5' for VPE refresh is broken."; //$NON-NLS-1$

	public Jbide10020_TestHotKeyForVpeRefresh() {
		super();
	}

	@Ignore @Test
	public void testHotKeyForVpeRefresh() {
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		/*
		 * Case 1: When focus is on in the VPE -- Find "Refresh (Ctrl+5)"
		 * toolbar button
		 */
		try {
			new DefaultToolItem(TOOL_TIP);
		} catch (CoreLayerException cle) {
			fail(ERROR_MESSAGE);
		}
		/*
		 * Case 2: When focus is on in Package Explorer -- Find "Refresh"
		 * toolbar button without HotKey defined.
		 */
		packageExplorer.activate();
		assertFalse("Tool Item with tooltip " + TOOL_TIP + " is not disabled",
				new DefaultToolItem(TOOL_TIP).isEnabled());
	}
}
