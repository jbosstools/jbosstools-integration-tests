/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.utils;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;

public class EclipseCDIHelper {

	private EclipseCDIHelper() {
		
	}

	/**
	 * Method disables folding used in editor in eclipse
	 * @param bot
	 * @param util
	 */
	public static void disableFolding() {
		editFolding(false);
	}

	/**
	 * Method enable folding used in editor in eclipse
	 * @param bot
	 * @param util
	 */
	public static void enableFolding() {
		editFolding(true);
	}

	private static void editFolding(boolean select) {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.select("Java", "Editor", "Folding");

		new CheckBox("Enable folding").toggle(select);

		dialog.ok();
	}
}
