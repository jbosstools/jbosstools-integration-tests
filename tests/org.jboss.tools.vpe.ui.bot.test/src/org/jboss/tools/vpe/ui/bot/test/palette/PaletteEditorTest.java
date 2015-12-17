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
package org.jboss.tools.vpe.ui.bot.test.palette;

import org.eclipse.swt.widgets.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.vpe.reddeer.view.JBTPaletteView;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests Palette Editor
 * 
 * @author vlado pakan
 *
 */
public class PaletteEditorTest extends VPEAutoTestCase {

	public PaletteEditorTest() {
		super();
	}
	@Test
	public void testPaletteEditor() {
		openPage();
		openPalette();
		// add First Palette Group
		JBTPaletteView paletteView = new JBTPaletteView();
		paletteView.clickPaletteEditorToolItem();
		new DefaultShell("Palette Editor");
		TreeItem tiPalette = new DefaultTreeItem("XStudio", "Palette");
		tiPalette.select();
		ContextMenuHelper.treeRightClick(tiPalette.getParent().getSWTWidget(), tiPalette.getSWTWidget());
		Menu menu = ContextMenuHelper.getTreeMenuViaReflections(tiPalette.getParent().getSWTWidget(), "New");
		ContextMenuHelper.clickContextMenu(menu, "New", "Create Group...");
		new DefaultShell("Create Group");
		final String firstGroup = "First";
		new LabeledText("Name:*").setText(firstGroup);
		new FinishButton().click();
		// add Second Inner Palette Group
		new DefaultShell("Palette Editor");
		TreeItem tiFirstGroup = new DefaultTreeItem("XStudio", "Palette", firstGroup);
		tiFirstGroup.select();
		ContextMenuHelper.treeRightClick(tiFirstGroup.getParent().getSWTWidget(), tiFirstGroup.getSWTWidget());
		menu = ContextMenuHelper.getTreeMenuViaReflections(tiFirstGroup.getParent().getSWTWidget(), "Create Group...");
		ContextMenuHelper.clickContextMenu(menu, "Create Group...");
		new DefaultShell("Add Palette Group");
		final String secondGroup = "Second";
		new LabeledText("Name:*").setText(secondGroup);
		new FinishButton().click();
		// add Macro
		new DefaultShell("Palette Editor");
		TreeItem tiSecondGroup = new DefaultTreeItem("XStudio", "Palette", firstGroup, secondGroup);
		tiSecondGroup.select();
		ContextMenuHelper.treeRightClick(tiSecondGroup.getParent().getSWTWidget(), tiSecondGroup.getSWTWidget());
		menu = ContextMenuHelper.getTreeMenuViaReflections(tiSecondGroup.getParent().getSWTWidget(), "New");
		ContextMenuHelper.clickContextMenu(menu, "New", "Create Macro...");
		new DefaultShell("Add Palette Macro");
		final String macroName = "Test Macro";
		new LabeledText("Name:*").setText(macroName);
		final String startText = "<HTML>";
		new LabeledText("Start Text:").setText(startText);
		final String endText = "</HTML>";
		new LabeledText("End Text:").setText(endText);
		new FinishButton().click();
		new DefaultShell("Palette Editor");
		new OkButton().click();
		// add Test Macro to Page Source
		final TextEditor jspTextEditor = new TextEditor(TEST_PAGE);
		final String originalText = jspTextEditor.getText();
		jspTextEditor.setCursorPosition(0);
		jspTextEditor.insertText(0, 0, "\n");
		jspTextEditor.insertText(0, 0, "");
		paletteView.activate();
		SWTBotWebBrowser.activatePaletteTool(macroName);
		// Check if Macro was added to Source Editor
		String insertedText = jspTextEditor.getTextAtLine(0).trim();
		assertTrue("Inserted text has to be '" + startText + endText + "' and was '" + insertedText + "'",
				insertedText.equals(startText + endText));
		jspTextEditor.setText(originalText);
		jspTextEditor.save();
		// Delete New Group From Palette
		paletteView.clickPaletteEditorToolItem();
		new DefaultShell("Palette Editor");
		tiFirstGroup = new DefaultTreeItem("XStudio", "Palette", firstGroup);
		tiFirstGroup.select();
		ContextMenuHelper.treeRightClick(tiFirstGroup.getParent().getSWTWidget(), tiFirstGroup.getSWTWidget());
		menu = ContextMenuHelper.getTreeMenuViaReflections(tiFirstGroup.getParent().getSWTWidget(), "Create Group...");
		ContextMenuHelper.clickContextMenu(menu, "Delete");
		new DefaultShell("Confirmation");
		new OkButton().click();
		new DefaultShell("Palette Editor");
		new OkButton().click();
		jspTextEditor.close();
	}
}
