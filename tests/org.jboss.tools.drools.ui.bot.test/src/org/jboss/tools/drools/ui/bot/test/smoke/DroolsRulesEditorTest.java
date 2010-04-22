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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.utils.Position;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.parts.ContentAssistBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.junit.Test;
// import org.eclipse.ui.forms.editor.FormEditor;
/**
 * Tests Drools Rule Editor
 * @author Vladimir Pakan
 *
 */
public class DroolsRulesEditorTest extends SWTTestExt{
  /**
   * Tests Drools Rule Editor
   */
  private static final String CONTENT_ASSIST_IMPORT = "import";
  private static final String CONTENT_ASSIST_MESSAGE = "Message";
  @Test
  public void testManageDroolsProject() {
    codeCompletionCheck(DroolsAllBotTests.SAMPLE_DROOLS_RULE_NAME);
    reteViewCheck(DroolsAllBotTests.SAMPLE_DROOLS_RULE_NAME);
  }
  /**
   * Check code completion for Drools Rule
   * @param droolsRuleName
   */
  private void codeCompletionCheck(String droolsRuleName){
    
    packageExplorer.show();
    packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,droolsRuleName);
    SWTBotEditorExt ruleEditor = bot.swtBotEditorExtByTitle(droolsRuleName);
    ruleEditor.selectPage(IDELabel.DroolsEditor.TEXT_EDITOR_TAB);
    ruleEditor.typeText(3, 0, "i");
    ContentAssistBot contentAssist = ruleEditor.contentAssist();
    contentAssist.checkContentAssist(DroolsRulesEditorTest.CONTENT_ASSIST_IMPORT, true);
    ruleEditor.typeText(6, 0, "m");
    contentAssist.checkContentAssist(DroolsRulesEditorTest.CONTENT_ASSIST_MESSAGE, true);
    
    SWTBotEclipseEditor ruleTextEditor = ruleEditor.toTextEditor();
    String lineText = ruleTextEditor.getTextOnLine(3).trim();
    assertTrue("Content Assist for " + DroolsRulesEditorTest.CONTENT_ASSIST_IMPORT +
        " was not inserted properly.\n" +
        "Inserted text is: " + lineText + "\n" +
        "Expected text is: " + DroolsRulesEditorTest.CONTENT_ASSIST_IMPORT,
      lineText.equals(DroolsRulesEditorTest.CONTENT_ASSIST_IMPORT));
    
    lineText = ruleTextEditor.getTextOnLine(6).trim();
    String messageContentAssistText = DroolsRulesEditorTest.CONTENT_ASSIST_MESSAGE + "(  )";
    assertTrue("Content Assist for " + DroolsRulesEditorTest.CONTENT_ASSIST_MESSAGE +
        " was not inserted properly.\n" +
        "Inserted text is: " + lineText + "\n" +
        "Expected text has to stard with: " + messageContentAssistText,
      lineText.startsWith(messageContentAssistText));
    
    Position cursorPosition = ruleTextEditor.cursorPosition();
    assertTrue("Content Assist for " + DroolsRulesEditorTest.CONTENT_ASSIST_MESSAGE +
        " was not inserted properly.\n" +
        "Position of cursor is wrong: " + cursorPosition + "\n" +
        "Expected X cursor position is: " + 9,
      cursorPosition.column == 9);
    
    ruleEditor.close();
    
  }
  /**
   * Check Rete View of Drools Rule
   * @param droolsRuleName
   */
  private void reteViewCheck(String droolsRuleName){
    
    packageExplorer.show();
    packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,droolsRuleName);
    SWTBotEditorExt ruleEditor = bot.swtBotEditorExtByTitle(droolsRuleName);
    ruleEditor.selectPage(IDELabel.DroolsEditor.RETE_TREE_TAB);
    
  }
}

