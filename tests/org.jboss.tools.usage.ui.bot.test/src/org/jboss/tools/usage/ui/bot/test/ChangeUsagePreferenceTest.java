/*******************************************************************************
 * Copyright (c) 2022 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.usage.ui.bot.test;

import org.eclipse.reddeer.jface.preference.PreferenceDialog;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.junit.Test;

/** Testing changing usage preferences.
 * @author Tamara Babalova */

public class ChangeUsagePreferenceTest {
	
	@Test
	public void changePreferenceTest() {		
		PreferenceDialog dialog = new WorkbenchPreferenceDialog();
		PreferencePage page = new PreferencePage(dialog, new String[] {"JBoss Tools", "Usage Reporting"});
		dialog.open();
		dialog.select(page);
		
		CheckBox checkBox = new CheckBox();
		boolean isChecked = checkBox.isChecked();
		checkBox.toggle(!isChecked);
		
		dialog.ok();
		
		dialog.open();
		dialog.select(page);
		checkBox = new CheckBox();
		assert(checkBox.isChecked() == !isChecked);
		dialog.ok();
	}
}
