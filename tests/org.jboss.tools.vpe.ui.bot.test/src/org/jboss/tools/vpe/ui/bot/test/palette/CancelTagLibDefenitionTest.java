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

import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.vpe.reddeer.view.JBTPaletteView;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

public class CancelTagLibDefenitionTest extends VPEAutoTestCase{
	@Test
	public void testCancelTagLibDefenition() {
		openPage();
		openPalette();
		// Test open import dialog
		new JBTPaletteView().clickImportToolItem();
		new DefaultShell("Import Tags from TLD File");
		// Test open edit TLD dialog
		new PushButton("Browse...").click();
		new DefaultShell("Edit TLD");
		// Test cancel TLD
		new DefaultTreeItem(JBT_TEST_PROJECT_NAME,"html_basic.tld [h]").select();
		new CancelButton().click();
		new DefaultShell("Import Tags from TLD File");
		assertEquals("", new LabeledText("TLD File:*").getText());
		assertEquals("", new LabeledText("Name:*").getText());
		assertEquals("", new LabeledText("Default Prefix:").getText());
		assertEquals("", new LabeledText("Library URI:").getText());
		new CancelButton().click();
	}
	
}
