/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.palette;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.view.PaletteView;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class CancelTagLibDefenitionTest extends VPEAutoTestCase{
	
	public void testCancelTagLibDefenition(){
    openPalette();
	  openPage();		
		//Test open import dialog
		bot.viewByTitle("JBoss Tools Palette").setFocus(); //$NON-NLS-1$
		new PaletteView()
		  .getToolbarButtonWitTooltip("Import")
		  .click();
		bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
		
		//Test open edit TLD dialog
		
		bot.button("Browse...").click(); //$NON-NLS-1$
		bot.shell("Edit TLD").activate(); //$NON-NLS-1$
		delay();
		//Test cancel TLD
		SWTBotTree tree = bot.tree();
		delay();
		SWTBotTreeItem item = tree.expandNode(JBT_TEST_PROJECT_NAME);
		delay();
		item.getNode("html_basic.tld [h]").select(); //$NON-NLS-1$ //$NON-NLS-2$
		delay();
		bot.button("Cancel").click(); //$NON-NLS-1$
		
		//Test check fields
		
		bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
		assertEquals("", bot.textWithLabel("TLD File:*").getText()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", bot.textWithLabel("Name:*").getText()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", bot.textWithLabel("Default Prefix:").getText()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", bot.textWithLabel("Library URI:").getText()); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("Cancel").click(); //$NON-NLS-1$
	}
	
	protected void closeUnuseDialogs(){
		try {
			bot.shell("Edit TLD").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
	}
	
	protected boolean isUnuseDialogOpened(){
		boolean isOpened = false;
		try {
			bot.shell("Edit TLD").activate(); //$NON-NLS-1$
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
			isOpened = true;
		}catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
}
