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

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.junit.Test;
/**
 * Tests Decision Table
 * @author Vladimir Pakan
 *
 */
public class DecisionTableTest extends SWTTestExt{
  /**
   * Tests Decision Table
   */
  @Test
  public void testDecisionTable() {
    runDecisionTable(DroolsAllBotTests.DECISION_TABLE_JAVA_TEST_FILE_NAME);
  }
  /**
   * Runs newly created Drools project and check result
   * @param decisionTableFileName
   */
  private void runDecisionTable(String decisionTableFileName){
    console.clearConsole();
    bot.sleep(5000L);

    SWTBotTreeItem tiTestFile = packageExplorer.selectTreeItem(decisionTableFileName, 
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,
        DroolsAllBotTests.SRC_MAIN_JAVA_TREE_NODE,
        DroolsAllBotTests.COM_SAMPLE_TREE_NODE});
    
    eclipse.runTreeItemAsJavaApplication(tiTestFile);
    
    String consoleText = console.getConsoleText(3*1000L,60*1000L,true);
      
    assertTrue(decisionTableFileName + " didn't run properly.\n" +
      "Console Text was: " + consoleText + "\n" +
      "Expected console text is: Hello World\nGoodbye cruel world\n",
      "Hello World\nGoodbye cruel world\n".equals(consoleText));
  }
}