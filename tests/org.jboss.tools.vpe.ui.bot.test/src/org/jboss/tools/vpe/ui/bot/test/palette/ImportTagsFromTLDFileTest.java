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
package org.jboss.tools.vpe.ui.bot.test.palette;

import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.eclipse.swt.SWT;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.tools.vpe.reddeer.view.JBTPaletteView;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

public class ImportTagsFromTLDFileTest extends VPEAutoTestCase {

	private static final String GROUP_NAME = "NewGroup";

	@Test
	public void testImportTagsFromTLDFile() {
		// Test clear group
		openPage();
		JBTPaletteView paletteView = new JBTPaletteView();
		paletteView.open();
		paletteView.clickPaletteEditorToolItem();
		new DefaultShell("Palette Editor");
		try {
			new DefaultTreeItem("XStudio", "Palette", GROUP_NAME).select();
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.DEL);
			new DefaultShell("Confirmation");
			new OkButton().click();
			new DefaultShell("Palette Editor");
		} catch (CoreLayerException cle) {
			// do nothing
		}
		new OkButton().click();
		// Test open import dialog
		paletteView.activate();
		paletteView.clickImportToolItem();
		new DefaultShell("Import Tags from TLD File");
		// Test set tag lib
		new PushButton("Browse...").click();
		new DefaultShell("Edit TLD");
		new DefaultTreeItem(JBT_TEST_PROJECT_NAME, "html_basic.tld [h]").select();
		new OkButton().click();
		// Test set group
		new DefaultShell("Import Tags from TLD File");
		new RadioButton(1).click();
		new DefaultText(4).setText(GROUP_NAME);
		new OkButton().click();
		// Test if group is created
		paletteView.clickPaletteEditorToolItem();
		new DefaultShell("Palette Editor");
		// group is defined
		new DefaultTreeItem("XStudio", "Palette", GROUP_NAME).select();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.DEL);
		new DefaultShell("Confirmation");
		new OkButton().click();
		new DefaultShell("Palette Editor");
		new OkButton().click();
	}
	@Override
	protected void createJSFProject(String jsfProjectName) {
		super.createJSFProject(jsfProjectName);
	}
}
