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

import java.awt.event.KeyEvent;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ImageHyperlinkHelper;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.junit.Test;
/**
 * Tests Guided Drools Rule Editor
 * @author Vladimir Pakan
 *
 */
public class GuidedDroolsRulesEditorTest extends SWTTestExt{
  private static final String DROOLS_PACKAGE_FILE = "drools.package";
  /**
   * Tests Guided Drools Rule Editor
   */
  @Test
  public void testGuidedDroolsRulesEditorTest() {
    createGuidedDroolsRule(DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME);
    editDroolsPackageFile();
    addGuidedDroolsRuleCondition(DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME);
    removeGuidedDroolsRuleCondition(DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME);
  }
  /**
   * Creates Guided Drools Rule
   * @param guidedDroolsRuleName
   */
  private void createGuidedDroolsRule(String guidedDroolsRuleName){
    packageExplorer.show();
    SWTBotTreeItem tiRules = packageExplorer.selectTreeItem(DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE, 
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME});
    
    tiRules.select();
    eclipse.createNew(EntityType.GUIDED_DROOLS_RULE);
    bot.textWithLabel(IDELabel.NewGuidedDroolsRuleDialog.FILE_NAME).setText(guidedDroolsRuleName);
    eclipse.selectTreeLocation(DroolsAllBotTests.DROOLS_PROJECT_NAME,
      "src",
      "main",
      "rules");
    bot.button(IDELabel.Button.FINISH).click();
    bot.sleep(Timing.time1S());
    tiRules.expand();
    // Test if new Drools Rule is within package tree view
    assertTrue("New Guided Drools Rule was not created properly. It's not present within Package Explorer",
      SWTEclipseExt.containsTreeItemWithLabel(tiRules, guidedDroolsRuleName));
    // Test if new Drools Rule is opened in editor
    assertTrue("New Guided Drools Rule was not created properly. File " + guidedDroolsRuleName + " is not opened in editor",
      SWTEclipseExt.existEditorWithLabel(bot,guidedDroolsRuleName));
    // Test if drools.package file is within package tree view
    assertTrue("New Guided Drools Rule was not created properly. It's not present within Package Explorer",
      SWTEclipseExt.containsTreeItemWithLabel(tiRules, 
        GuidedDroolsRulesEditorTest.DROOLS_PACKAGE_FILE));
  }
  /**
   * Edits drools.package file.
   * Actually only adds import java.util.List to file
   */
  private void editDroolsPackageFile(){
    packageExplorer.show();
    SWTBotEclipseEditor droolsPackageEditor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
      DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
      GuidedDroolsRulesEditorTest.DROOLS_PACKAGE_FILE).toTextEditor();
    droolsPackageEditor.setText(droolsPackageEditor.getText() +
      "\nimport java.util.List;");
    droolsPackageEditor.save();
    droolsPackageEditor.close();
  }
  
  private void addGuidedDroolsRuleCondition(String guidedDroolsRuleName){
    packageExplorer.show();
    SWTBotEclipseEditor droolsEditor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
      DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
      DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME).toTextEditor();
    SWTBot droolsEditorBot = droolsEditor.bot();
    droolsEditorBot.toolbarButton().click();
    SWTBotShell dialogShell = droolsEditorBot.shell(IDELabel.GuidedDroolsRuleEditor.WHEN_ADD_DIALOG_TITLE);
    dialogShell.activate();
    dialogShell.bot().comboBoxWithLabel(IDELabel.GuidedDroolsRuleEditor.WHEN_ADD_FACT_COMBO)
      .setSelection("List");
    ImageHyperlinkHelper
      .imageHyperlinkWithTooltip(droolsEditorBot,
        IDELabel.GuidedDroolsRuleEditor.ADD_FIELD_TO_THIS_CONDITION_TOOLTIP)
      .click();
    dialogShell = droolsEditorBot.shell(IDELabel.GuidedDroolsRuleEditor.UPDATE_CONSTRAINTS_DIALOG_TITLE);
    dialogShell.activate();
    dialogShell.bot().comboBox()
      .setSelection(IDELabel.GuidedDroolsRuleEditor.ADD_RESTRICTION_ON_A_FIELD_COMBO_VALUE);
    droolsEditorBot.comboBox()
      .setSelection(IDELabel.GuidedDroolsRuleEditor.WHEN_COMBO_CONSTRAINTS_VALUE);
    ImageHyperlinkHelper
      .imageHyperlinkWithTooltip(droolsEditorBot,
        IDELabel.GuidedDroolsRuleEditor.CHOOSE_VALUE_EDITOR_TYPE_TOOLTIP)
      .click();
    dialogShell = droolsEditorBot.shell(IDELabel.GuidedDroolsRuleEditor.SELECT_VALUE_EDITOR_TYPE_DIALOG_TITLE);
    dialogShell.activate();
    dialogShell.bot().comboBoxWithLabel(IDELabel.GuidedDroolsRuleEditor.SELECT_VALUE_EDITOR_TYPE_COMBO_LABEL)
      .setSelection(IDELabel.GuidedDroolsRuleEditor.SELECT_VALUE_EDITOR_TYPE_COMBO_VALUE);
    droolsEditorBot.comboBox(1).setSelection(IDELabel.GuidedDroolsRuleEditor.FIELD_VALUE_COMBO_VALUE);
    droolsEditor.save();
    droolsEditor.close();
    droolsEditor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
        DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME).toTextEditor();
    SWTBotEditorExt ruleEditor = bot.swtBotEditorExtByTitle(DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME);
    ruleEditor.selectPage(2);
    String editorContent = droolsEditor.getText();
    assertTrue(DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME +
      " has to contain text: List( empty == true )\n" +
      "but it doesn't.\n" +
      "It contains this text: " + editorContent, 
      editorContent.replaceAll(" ","").indexOf("List(empty==true)") > 0);
  }
  /**
   * Removes Drools Rule Condition from Guided Drools Rule
   * @param guidedDroolsRuleName
   */
  private void removeGuidedDroolsRuleCondition(String guidedDroolsRuleName){
    packageExplorer.show();
    SWTBotEclipseEditor droolsEditor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
      DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
      DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME).toTextEditor();
    SWTBot droolsEditorBot = droolsEditor.bot();
    SWTBotEditorExt ruleEditor = bot.swtBotEditorExtByTitle(DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME);
    ruleEditor.selectPage(0);
    ImageHyperlinkHelper
      .imageHyperlinkWithTooltip(droolsEditorBot,
        IDELabel.GuidedDroolsRuleEditor.REMOVE_THIS_CONDITION_TOOLTIP)
      .click();
    bot.sleep(Timing.time1S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
    bot.sleep(Timing.time1S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    bot.sleep(Timing.time1S());
    droolsEditor.save();
    droolsEditor.close();
    droolsEditor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
        DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME).toTextEditor();
    ruleEditor = bot.swtBotEditorExtByTitle(DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME);
    ruleEditor.selectPage(2);
    String editorContent = droolsEditor.getText();
    assertTrue(DroolsAllBotTests.GUIDED_DROOLS_RULE_NAME +
      " has to contain textjak e:\nwhen\nthen\n" +
      "but it doesn't.\n" +
      "It contains this text: " + editorContent, 
      ruleEditor.getTextOnLine(2).trim().equals("when") &&
      ruleEditor.getTextOnLine(3).trim().equals("then"));
  }
  
}