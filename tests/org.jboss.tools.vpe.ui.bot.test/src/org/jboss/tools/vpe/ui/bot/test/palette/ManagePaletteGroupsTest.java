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

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests Showing/hiding JBoss Tools Palette Groups  
 * @author vlado pakan
 *
 */
public class ManagePaletteGroupsTest extends VPEAutoTestCase {
  
  private static final String TEST_PALETTE_GROUP_LABEL = "JSF HTML";
  private static final String TEST_PALETTE_TREE_GROUP_LABEL = "JSF";
  
  private SWTBotExt botExt = null;
  
  public ManagePaletteGroupsTest() {
    super();
    botExt = new SWTBotExt();
  }
	public void testManagePaletteGroups(){
	  
	  openPage();
    openPalette();	 
    hideShowPaletteGroup();
    // Put palette changes back
    hideShowPaletteGroup();    
    
	}
	/**
	 * Hide or Show Pallete Group dependent on current Palette Group visibility
	 */
	private void hideShowPaletteGroup(){
    bot.toolbarButtonWithTooltip(IDELabel.JBossToolsPalette.SHOW_HIDE_TOOL_ITEM).click();
    SWTBot dialogBot = bot.shell(IDELabel.Shell.SHOW_HIDE_DRAWERS).activate().bot();
    SWTBotTreeItem tiTestPaletteGroup = dialogBot.tree().getTreeItem(ManagePaletteGroupsTest.TEST_PALETTE_TREE_GROUP_LABEL);
    if (tiTestPaletteGroup.isChecked()){
      // Check Palette Group hiding
      tiTestPaletteGroup.uncheck();
      dialogBot.button("Ok").click();
      assertTrue("Palette Group " + ManagePaletteGroupsTest.TEST_PALETTE_GROUP_LABEL +
        " has to be hidden but is visible.", 
        !SWTBotWebBrowser.paletteContainsRootPaletteCotnainer(botExt, ManagePaletteGroupsTest.TEST_PALETTE_GROUP_LABEL));
    }
    else{
      // Check Palette Group showing
      tiTestPaletteGroup.check();
      dialogBot.button("Ok").click();
      assertTrue("Palette Group " + ManagePaletteGroupsTest.TEST_PALETTE_GROUP_LABEL +
        " has to be visible but is hidden.", 
        SWTBotWebBrowser.paletteContainsRootPaletteCotnainer(botExt, ManagePaletteGroupsTest.TEST_PALETTE_GROUP_LABEL));
    }	  
	}
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  
}
