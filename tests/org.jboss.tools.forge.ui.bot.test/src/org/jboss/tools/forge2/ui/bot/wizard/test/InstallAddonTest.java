/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.AbstractTableItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for connection profile creation tests
 * 
 * @author jkopriva@redhat.com
 *
 */

public class InstallAddonTest extends WizardTestBase {
	private static String INSTALL_ADDON_DIALOG_NAME = "Install an Addon from the catalog";
	private static String REMOVE_ADDON_DIALOG_NAME = "Remove an Addon";
	private static String ADDON_NAME = "RichFaces";
	private static String ADDON_PACKAGE_NAME = "org.richfaces.forge:richfaces";

	@Test
	public void testAddonInstall() {
		WizardDialog dialog = getWizardDialog(INSTALL_ADDON_DIALOG_NAME, "(" + INSTALL_ADDON_DIALOG_NAME + ").*");
		new DefaultCombo().setSelection(ADDON_NAME);
		new WaitWhile(new JobIsRunning());
		dialog.finish(TimePeriod.getCustom(1000)); //could take a very long time
		WizardDialog dialogRemove = getWizardDialog(REMOVE_ADDON_DIALOG_NAME, "(" + REMOVE_ADDON_DIALOG_NAME + ").*");
		Table table = new DefaultTable();
		String addonFullName = "";
		for (TableItem item : table.getItems()) {
			System.out.println("neconeco" + item.getText());
			if (item.getText().toLowerCase().contains(ADDON_NAME.toLowerCase())) {
				addonFullName = item.getText();
			}
		}
		assertTrue("Addon is not installed!", !"".equals(addonFullName));
		table.select(addonFullName);
		table.getItem(addonFullName).setChecked(true);
		dialogRemove.finish();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(600));
		dialogRemove = getWizardDialog(REMOVE_ADDON_DIALOG_NAME, "(" + REMOVE_ADDON_DIALOG_NAME + ").*");
		table = new DefaultTable();
		assertTrue("Addon has not been removed!", !table.containsItem(addonFullName));
		dialogRemove.cancel();
	}

	@Before
	public void prepare() {
		newProject(PROJECT_NAME);
	}

	public class AllTableMatcher extends BaseMatcher<TableItem> {
		private String text;

		public AllTableMatcher(String text) {
			this.text = text;
		}

		public void describeTo(Description description) {
			description.appendText("Table contains at least one cell with text: " + text);
		}

		@Override
		public boolean matches(Object item) {
			AbstractTableItem ti = (AbstractTableItem) item;
			if (ti.getText().contains(text)) {
				return true;
			}
			return false;
		}
	}
}
