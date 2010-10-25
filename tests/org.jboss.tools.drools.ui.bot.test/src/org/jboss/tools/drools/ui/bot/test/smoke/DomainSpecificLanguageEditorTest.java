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

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.JobName;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.junit.Test;
/**
 * Tests Domain Specific Language Editor
 * @author Vladimir Pakan
 *
 */
public class DomainSpecificLanguageEditorTest extends SWTTestExt{
  /**
   * Tests Domain Specific Language Editor
   */
  private static final String LANGUAGE_EXRESSION = "Message {msg} of type {t} contains {what}";
  @Test
  public void testDomainSpecificLanguageEditor() {
    createDslFile(DroolsAllBotTests.DOMAIN_SPECIFIC_LANGUAGE_FILE_NAME);
    addDslExpression(DroolsAllBotTests.DOMAIN_SPECIFIC_LANGUAGE_FILE_NAME);
    useDslExpression(DroolsAllBotTests.DOMAIN_SPECIFIC_LANGUAGE_FILE_NAME,
      DroolsAllBotTests.SAMPLE_DROOLS_RULE_NAME);
    runDslExpressionCheck(DroolsAllBotTests.DROOLS_TEST_JAVA_TREE_NODE,
      DroolsAllBotTests.DOMAIN_SPECIFIC_LANGUAGE_FILE_NAME);
  }
  /**
   * Creates DSL File
   * @param dslFileName
   */
  private void createDslFile(String dslFileName){
    packageExplorer.show();
    SWTBotTreeItem tiRules = packageExplorer.selectTreeItem(DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE, 
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME});
    
    tiRules.select();
    eclipse.createNew(EntityType.DSL_DROOLS_FILE);
    bot.textWithLabel(IDELabel.NewDslDroolsFileDialog.FILE_NAME).setText(dslFileName);
    eclipse.selectTreeLocation(DroolsAllBotTests.DROOLS_PROJECT_NAME,
      "src",
      "main",
      "rules");
    bot.button(IDELabel.Button.FINISH).click();
    bot.sleep(Timing.time1S());
    tiRules.expand();
    // Test if new DSL File is within package tree view
    assertTrue("New DSL File was not created properly. It's not present within Package Explorer",
      SWTEclipseExt.containsTreeItemWithLabel(tiRules, dslFileName));
    // Test if new DSL File is opened in editor
    assertTrue("New DSL File was not created properly. File " + dslFileName + " is not opened in editor",
      SWTEclipseExt.existEditorWithLabel(bot,dslFileName));

  }
  /**
   * Adds DSL Expression to DSL File
   * @param dslFileName
   */
  private void addDslExpression(String dslFileName){
    SWTBotEditor dslRuleEditor = bot.editorByTitle(dslFileName);
    SWTBot dslRuleEditorBot = dslRuleEditor.bot();
    dslRuleEditorBot
      .button(IDELabel.Button.ADD_WITHOUT_DOTS).click();
    SWTBot dialogBot = dslRuleEditorBot
      .shell(IDELabel.DslDroolsFileEditor.ADD_LANGUAGE_MAPPING_DIALOG_TITLE)
      .activate()
      .bot();
    
    dialogBot.textWithLabel(IDELabel.DslDroolsFileEditor.LANGUAGE_EXPRESSION_TEXT_LABEL)
      .setText(DomainSpecificLanguageEditorTest.LANGUAGE_EXRESSION);
    dialogBot.textWithLabel(IDELabel.DslDroolsFileEditor.RULE_MAPPING_TEXT_LABEL)
      .setText("{msg} : Message(status == {t}, {what} : message)");
    dialogBot.comboBoxWithLabel(IDELabel.DslDroolsFileEditor.SCOPE_COMBO_LABEL)
      .setSelection(IDELabel.DslDroolsFileEditor.SCOPE_COMBO_VALUE);
    dialogBot.button(IDELabel.Button.OK).click();
    dslRuleEditor.save();
    
    assertTrue("DSL table has to containt this Language Expression:\n" +
        DomainSpecificLanguageEditorTest.LANGUAGE_EXRESSION,
      SWTEclipseExt.isItemInTableColumn(dslRuleEditorBot.table(), 
        DomainSpecificLanguageEditorTest.LANGUAGE_EXRESSION, 
        0));
    
  }
  /**
   * Use defined language expression in dslFileName file within sampleDrlFileName file
   * @param dslFileName
   * @param sampleDrlFileName
   */
  private void useDslExpression (String dslFileName,String sampleDrlFileName){
    packageExplorer.show();
    SWTBotEclipseEditor drlDroolsEditor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
      DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
      DroolsAllBotTests.SAMPLE_DROOLS_RULE_NAME).toTextEditor();
    SWTBotEditorExt ruleEditor = bot.swtBotEditorExtByTitle(DroolsAllBotTests.SAMPLE_DROOLS_RULE_NAME);
    ruleEditor.selectPage(IDELabel.DroolsEditor.TEXT_EDITOR_TAB);    
    // update drl file
    drlDroolsEditor.insertText(3,0,"\nexpander " +
      dslFileName +
      ";\n");
    int[] linesToIgnoreExpander = new int[]{8,10,11,12,13,20};
    for (int lineNumber : linesToIgnoreExpander){
      drlDroolsEditor.insertText(lineNumber,0,">");
    }
    drlDroolsEditor.selectLine(18);
    bot.sleep(Timing.time1S());
    KeyboardHelper.pressKeyCode(bot.getDisplay(),(int)SWT.DEL);
    bot.sleep(Timing.time1S());
    drlDroolsEditor.insertText(18, 0, "        Message m of type Message.GOODBYE contains myMessage");
    drlDroolsEditor.save();
    util.waitForJobs(Timing.time10S(), JobName.BUILDING_WS);
    SWTBotTreeItem[] errors = ProblemsView
      .getFilteredErrorsTreeItems(bot,
        null, 
        null,
        sampleDrlFileName,
        null); 
    assertTrue("File "
      + sampleDrlFileName 
      + " was not udpated properly. There are these errors: "
      + SWTEclipseExt.getFormattedTreeNodesText(bot.tree(), errors),
      errors == null || errors.length == 0);
    
    SWTBotTreeItem[] warnings = ProblemsView
      .getFilteredWarningsTreeItems(bot,
        null, 
        null,
        sampleDrlFileName,
        null); 
    assertTrue("File "
      + sampleDrlFileName 
      + " was not udpated properly. There are these warnings: "
      + SWTEclipseExt.getFormattedTreeNodesText(bot.tree(), warnings),
      warnings == null || warnings.length == 0);
  }
  /**
   * Runs javaFileName testing defined DSL
   * @param javaFileName
   * @param dslFileName
   */
  private void runDslExpressionCheck(String javaFileName,
    String dslFileName){
    
    packageExplorer.show();
    SWTBotEclipseEditor drlDroolsEditor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
      DroolsAllBotTests.SRC_MAIN_JAVA_TREE_NODE,
      DroolsAllBotTests.COM_SAMPLE_TREE_NODE,
      javaFileName).toTextEditor();
    // Change java file content to support new DSL
    updateJavaTestFile(drlDroolsEditor,dslFileName);
    
    console.clearConsole();
    bot.sleep(Timing.time5S());

    SWTBotTreeItem tiTestFile = packageExplorer.selectTreeItem(javaFileName, 
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,
        DroolsAllBotTests.SRC_MAIN_JAVA_TREE_NODE,
        DroolsAllBotTests.COM_SAMPLE_TREE_NODE});
    
    eclipse.runTreeItemAsJavaApplication(tiTestFile);
    
    String consoleText = console.getConsoleText(3*1000L,60*1000L,true);
    
    assertTrue(javaFileName + " didn't run properly.\n" +
      "Console Text was: " + consoleText + "\n" +
      "Expected console text is: " + "Hello World\nGoodbye cruel world\n",
      "Hello World\nGoodbye cruel world\n".equals(consoleText));
  }
  /**
   * Update properly Java Test file in drlDroolsEditor to be able to run
   * with new DSL definition
   * @param drlDroolsEditor
   * @param dslFileName
   */
  private void updateJavaTestFile(SWTBotEclipseEditor drlDroolsEditor,
    String dslFileName){
    int lineIndex = 0;
    String foundLineText = null;
    while (lineIndex < drlDroolsEditor.getLineCount() && foundLineText == null){
      String lineText = drlDroolsEditor.getTextOnLine(lineIndex);
      if(lineText.trim().startsWith("kbuilder.add")){
        foundLineText = lineText;
      }
      else{
        lineIndex++;
      }  
    }
    if (foundLineText != null){
      drlDroolsEditor.insertText(lineIndex,0,
        "kbuilder.add(ResourceFactory.newClassPathResource(\"" +
        dslFileName + 
        "\"), ResourceType.DSL);\n");
      lineIndex++;
      drlDroolsEditor.selectLine(lineIndex);
      KeyboardHelper.pressKeyCode(bot.getDisplay(),(int)SWT.DEL);
      drlDroolsEditor.insertText(foundLineText
        .replace("ResourceType.DRL","ResourceType.DSLR") + "\n");
      drlDroolsEditor.save();
      util.waitForJobs(Timing.time10S(), JobName.BUILDING_WS);
    }
    else{
      throw new RuntimeException("File " +
        drlDroolsEditor.getTitle() +
        " has wrong content. It doesn't contain 'kbuilder.add' string.");
    }
  }
  
}