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
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.matcher.ColumnTableItemMatcher;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;

public abstract class PageDesignTestCase extends VPEEditorTestCase {

	final static String PAGE_DESIGN = "Page Design Options"; //$NON-NLS-1$

	void closePage() {
		new TextEditor(TEST_PAGE).close();
	}

	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile() //$NON-NLS-1$
				+ "resources/pagedesign/" + testPage; //$NON-NLS-1$
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile() //$NON-NLS-1$
					+ "pagedesign/" + testPage; //$NON-NLS-1$
		}
		return filePath;
	}

	/**
	 * Deletes all defined EL Substitutions. VPE has to be opened when called
	 * this method
	 */
	public void deleteAllELSubstitutions() {
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Substituted EL expressions").activate();
		Table elVariablesTable = new DefaultTable();
		while (elVariablesTable.rowCount() > 0) {
			elVariablesTable.select(0);
			new PushButton("Remove").click();
		}
		new OkButton().click();
	}

	/**
	 * Adds EL Definition
	 * 
	 * @param elName
	 * @param value
	 * @param scope
	 */
	public void addELSubstitution(String elName, String value, String scope) {
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Substituted EL expressions").activate();
		new PushButton("Add").click();
		new DefaultShell("Add EL Reference");
		new LabeledText("El Name*").setText(elName);
		new LabeledText("Value").setText(value);
		new RadioButton(scope).click();
		new FinishButton().click();
		new DefaultShell("Page Design Options");
		new OkButton().click();
	}

	/**
	 * Edits EL Variable elName Definition
	 * 
	 * @param elName
	 * @param oldScope
	 * @param newValue
	 * @param scopeRadioLabel
	 */
	public void editELSubstitution(String elName, String oldScope, String newValue, String scopeRadioLabel) {
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Substituted EL expressions").activate();
		new DefaultTable().getItems(new ColumnTableItemMatcher(1, elName), new ColumnTableItemMatcher(0, oldScope))
				.get(0).select();
		new PushButton("Edit").click();
		new DefaultShell("Add EL Reference");
		new LabeledText("Value").setText(newValue);
		new RadioButton(scopeRadioLabel).click();
		new FinishButton().click();
		new DefaultShell("Page Design Options");
		new OkButton().click();
	}

	/**
	 * Deletes EL Variable elName Definition
	 * 
	 * @param elName
	 * @param scope
	 */
	public void deleteELSubstitution(String elName, String scope) {
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Substituted EL expressions").activate();
		new DefaultTable().getItems(new ColumnTableItemMatcher(1, elName), new ColumnTableItemMatcher(0, scope)).get(0)
				.select();
		new PushButton("Remove").click();
		new OkButton().click();
	}
}
