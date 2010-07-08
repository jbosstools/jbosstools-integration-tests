 /*******************************************************************************
  * Copyright (c) 2007-2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.drools.ui.bot.test.smoke;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.junit.Test;
/**
 * Test managing of Drools Rules
 * @author Vladimir Pakan
 *
 */
public class ManageDroolsRules extends SWTTestExt{
  /**
   * Test manage Drools Rules
   */
  @Test
  public void testManageDroolsRules() {
    createDroolsRule (DroolsAllBotTests.TEST_DROOLS_RULE_NAME);
    debugDroolsRule (DroolsAllBotTests.SAMPLE_DROOLS_RULE_NAME);
  }
  /**
   * Creates Drools Rule and checks result
   * @param droolsRuletName
   */
  private void createDroolsRule(String droolsRuleName){
    
    packageExplorer.show();
    SWTBotTreeItem tiDroolsRules = packageExplorer.selectTreeItem(DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE, 
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME});
    
    tiDroolsRules.select();
    eclipse.createNew(EntityType.DROOLS_RULE);
    
    bot.textWithLabel(IDELabel.NewDroolsRuleDialog.FILE_NAME).setText(droolsRuleName);
    bot.textWithLabel(IDELabel.NewDroolsRuleDialog.RULE_PACKAGE_NAME).setText(DroolsAllBotTests.COM_SAMPLE_TREE_NODE);
    bot.button(IDELabel.Button.FINISH).click();
    bot.sleep(Timing.time1S());
    tiDroolsRules.expand();
    // Test if new Drools Rule is within package tree view
    boolean isRuleCreated = true;
    try{
      tiDroolsRules.getNode(droolsRuleName);
    } catch (WidgetNotFoundException wnfe){
      isRuleCreated = false;  
    }
    assertTrue("New Drools Rule was not created properly. It's not present within Package Explorer",isRuleCreated);
    // Test if new Drools Rule is opened in editor
    isRuleCreated = true;
    try{
      bot.editorByTitle(droolsRuleName);
    } catch (WidgetNotFoundException wnfe){
      isRuleCreated = false;  
    }
    assertTrue("New Drools Rule was not created properly. File " + droolsRuleName + " is not opened in editor",isRuleCreated);
  }
  /**
   * Debug Drools Rule and checks result
   * @param droolsRuletName
   */
  private void debugDroolsRule(String droolsRuleName){
    packageExplorer.show();
    SWTBotTreeItem tiDroolsRule = packageExplorer.selectTreeItem(DroolsAllBotTests.SAMPLE_DROOLS_RULE_NAME, 
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE});
    SWTBot packageExplorerBot = bot.viewByTitle(ViewType.PACKAGE_EXPLORER.getViewLabel()).bot();
    SWTBotTree tree = packageExplorerBot.tree();
    // Select and Open Rule File
    ContextMenuHelper.prepareTreeItemForContextMenu(tree , tiDroolsRule);
    new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.OPEN, true)).click();
    SWTBotEclipseEditor ruleEditor = bot.editorByTitle(droolsRuleName).toTextEditor();
    ruleEditor.selectRange(8, 0, 0);
    bot.menu(IDELabel.Menu.RUN).menu(IDELabel.Menu.TOGGLE_BREAKPOINT).click();
    SWTBotTreeItem tiDroolsTest = packageExplorer.selectTreeItem(DroolsAllBotTests.DROOLS_TEST_JAVA_TREE_NODE, 
        new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,
          DroolsAllBotTests.SRC_MAIN_JAVA_TREE_NODE,
          DroolsAllBotTests.COM_SAMPLE_TREE_NODE});
    console.clearConsole();
    eclipse.debugTreeItemAsDroolsApplication(tiDroolsTest);
    bot.sleep(Timing.time3S());
    eclipse.closeConfirmPerspectiveSwitchShellIfOpened(false);
    String consoleText = console.getConsoleText(3*1000L,3*1000L,true);
    assertTrue("Drools Rule was not debuged properly.\nConsole content should have been empty but is:\n" + consoleText,
      consoleText.length() == 0);
    SWTBotView debugView = bot.viewByTitle(ViewType.DEBUG.getViewLabel());
    debugView.toolbarButton(IDELabel.DebugView.BUTTON_STEP_OVER_TOOLTIP).click();
    consoleText = console.getConsoleText(3*1000L,60*1000L,true);
    assertTrue("Drools Rule was not debuged properly.\nConsole content should be:\n'Hello World\n' but is:\n" + consoleText,
        consoleText.equals("Hello World\n"));
    debugView.toolbarButton(IDELabel.DebugView.BUTTON_RESUME_TOOLTIP).click();
    consoleText = console.getConsoleText(3*1000L,60*1000L,true);
    assertTrue("Drools Rule was not debuged properly.\nConsole content should be:Hello World\nGoodbye cruel world\n" + consoleText,
        consoleText.equals("Hello World\nGoodbye cruel world\n"));
  }

}

