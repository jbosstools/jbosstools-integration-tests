/*******************************************************************************

 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
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
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests Palette Editor  
 * @author vlado pakan
 *
 */
public class PaletteEditorTest extends VPEAutoTestCase {
  private SWTBotExt botExt = null;
  
  public PaletteEditorTest() {
    super();
    botExt = new SWTBotExt();
  }
  
	public void testPaletteEditor(){
	  openPage();
    openPalette();	
    // add First Palette Group
    bot.toolbarButtonWithTooltip(IDELabel.JBossToolsPalette.PALETTE_EDITOR_TOOL_ITEM).click();
    SWTBot palettEditorBot = bot.shell(IDELabel.Shell.PALETTE_EDITOR).activate().bot();
    SWTBotTree tree = palettEditorBot.tree();
    SWTBotTreeItem tiPalette = tree.expandNode(IDELabel.PaletteEditor.XSTUDIO_NODE)
      .getNode(IDELabel.PaletteEditor.PALETTE_NODE)
      .select();
    ContextMenuHelper.treeRightClick(tree.widget, tiPalette.widget);
    Menu menu = ContextMenuHelper.getTreeMenuViaReflections(tree.widget,IDELabel.PaletteEditor.NEW_MENU_ITEM);
    ContextMenuHelper.clickContextMenu(menu,
      IDELabel.PaletteEditor.NEW_MENU_ITEM,
      IDELabel.PaletteEditor.CREATE_GROUP_MENU_ITEM);
    ContextMenuHelper.hideMenuRecursively(menu);
    SWTBot createGroupDialogBot = bot.shell(IDELabel.Shell.CREATE_GROUP).activate().bot();
    final String firstGroup = "First";
    createGroupDialogBot.textWithLabel(IDELabel.CreateGroupDialog.NAME).setText(firstGroup);
    createGroupDialogBot.button(IDELabel.Button.FINISH).click();
    // add Second Inner Palette Group
    SWTBotTreeItem tiFirstGroup = tree.expandNode(IDELabel.PaletteEditor.XSTUDIO_NODE)
      .expandNode(IDELabel.PaletteEditor.PALETTE_NODE)
      .expandNode(firstGroup)
      .select();
    ContextMenuHelper.treeRightClick(tree.widget, tiFirstGroup.widget);
    menu = ContextMenuHelper.getTreeMenuViaReflections(tree.widget,IDELabel.PaletteEditor.CREATE_GROUP_MENU_ITEM);
    ContextMenuHelper.clickContextMenu(menu,
        IDELabel.PaletteEditor.CREATE_GROUP_MENU_ITEM);
    ContextMenuHelper.hideMenuRecursively(menu);
    SWTBot addPaletteGroupBot = bot.shell(IDELabel.Shell.ADD_PALETTE_GROUP).activate().bot();
    final String secondGroup = "Second";
    addPaletteGroupBot.textWithLabel(IDELabel.AddPaletteGroupDialog.NAME).setText(secondGroup);
    addPaletteGroupBot.button(IDELabel.Button.FINISH).click();
    // add Macro
    SWTBotTreeItem tiSecondGroup = tree.expandNode(IDELabel.PaletteEditor.XSTUDIO_NODE)
      .expandNode(IDELabel.PaletteEditor.PALETTE_NODE)
      .expandNode(firstGroup)
      .expandNode(secondGroup)
      .select();
    ContextMenuHelper.treeRightClick(tree.widget, tiSecondGroup.widget);
    menu = ContextMenuHelper.getTreeMenuViaReflections(tree.widget,IDELabel.PaletteEditor.NEW_MENU_ITEM);
    ContextMenuHelper.clickContextMenu(menu,
      IDELabel.PaletteEditor.NEW_MENU_ITEM,        
      IDELabel.PaletteEditor.CREATE_MACRO_MENU_ITEM);
    ContextMenuHelper.hideMenuRecursively(menu);
    SWTBot addPaletteMacroBot = bot.shell(IDELabel.Shell.ADD_PALETTE_MACRO).activate().bot();
    final String macroName = "Test Macro";
    addPaletteMacroBot.textWithLabel(IDELabel.AddPaletteMacroDialog.NAME).setText(macroName);
    final String startText = "<HTML>";
    addPaletteMacroBot.textWithLabel(IDELabel.AddPaletteMacroDialog.START_TEXT).setText(startText);
    final String endText = "</HTML>";
    addPaletteMacroBot.textWithLabel(IDELabel.AddPaletteMacroDialog.END_TEXT).setText(endText);
    addPaletteMacroBot.button(IDELabel.Button.FINISH).click();
    palettEditorBot.button(IDELabel.Button.OK).click();
    //add Test Macro to Page Source
    final SWTBotEclipseEditor jspTextEditor = botExt.editorByTitle(TEST_PAGE)
      .toTextEditor();
    final String originalText = jspTextEditor.getText();
    jspTextEditor.setFocus();
    jspTextEditor.insertText(0, 0, "\n");
    jspTextEditor.insertText(0, 0, "");
    SWTBotWebBrowser.activatePaletteTool(botExt,macroName);
    // Check if Macro was added to Source Editor
    String insertedText = jspTextEditor.getTextOnCurrentLine().trim(); 
    assertTrue("Inserted text has to be '" + startText + endText +
      "' and was '" + insertedText + "'",
      insertedText.equals(startText + endText));
    jspTextEditor.setText(originalText);
    jspTextEditor.save();
    // Delete New Group From Palette
    bot.toolbarButtonWithTooltip(IDELabel.JBossToolsPalette.PALETTE_EDITOR_TOOL_ITEM).click();
    palettEditorBot = bot.shell(IDELabel.Shell.PALETTE_EDITOR).activate().bot();
    tree = palettEditorBot.tree();
    tiFirstGroup = tree.expandNode(IDELabel.PaletteEditor.XSTUDIO_NODE)
      .expandNode(IDELabel.PaletteEditor.PALETTE_NODE)
      .expandNode(firstGroup)
      .select();
    ContextMenuHelper.treeRightClick(tree.widget, tiFirstGroup.widget);
    menu = ContextMenuHelper.getTreeMenuViaReflections(tree.widget,IDELabel.PaletteEditor.CREATE_GROUP_MENU_ITEM);
    ContextMenuHelper.clickContextMenu(menu,
      IDELabel.PaletteEditor.DELETE_MENU_ITEM);
    SWTBot deleteDialogBot = bot.shell(IDELabel.Shell.CONFIRMATION).activate().bot();
    deleteDialogBot.button(IDELabel.Button.OK).click();
    palettEditorBot.button(IDELabel.Button.OK).click();
    jspTextEditor.close();
	}
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
}
