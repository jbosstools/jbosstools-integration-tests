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

import java.io.File;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.FileRenameHelper;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.junit.Test;
/**
 * Test managing of Drools Project
 * @author Vladimir Pakan
 *
 */
public class ManageDroolsProject extends SWTTestExt{
  /**
   * Test manage Drools project
   */
  private static final String RENAMED_DROOLS_PROJECT = DroolsAllBotTests.DROOLS_PROJECT_NAME + "-renamed";

  @Test
  public void testManageDroolsProject() {
    createDroolsProjectTest (DroolsAllBotTests.DROOLS_PROJECT_NAME);
    runNewDroolsProject (DroolsAllBotTests.DROOLS_PROJECT_NAME);
    renameDroolsProject (DroolsAllBotTests.DROOLS_PROJECT_NAME, ManageDroolsProject.RENAMED_DROOLS_PROJECT);
    deleteDroolsProject (ManageDroolsProject.RENAMED_DROOLS_PROJECT);
    createDroolsProjectTest (DroolsAllBotTests.DROOLS_PROJECT_NAME);
  }
  
  public static void createDroolsProject(String droolsProjectName) {
	    eclipse.showView(ViewType.PACKAGE_EXPLORER);
	    eclipse.createNew(EntityType.DROOLS_PROJECT);
	    bot.textWithLabel(IDELabel.NewDroolsProjectDialog.NAME).setText(droolsProjectName);
	    bot.button(IDELabel.Button.NEXT).click();
	    // check all buttons
	    int index = 0;
	    boolean checkBoxExists = true;
	    while (checkBoxExists){
	      try{
	        SWTBotCheckBox checkBox = bot.checkBox(index);
	        if (!checkBox.isChecked()){
	          checkBox.click();
	        }
	        index++;
	      }catch (WidgetNotFoundException wnfe){
	        checkBoxExists = false;
	      }catch (IndexOutOfBoundsException ioobe){
	        checkBoxExists = false;
	      }
	    }
	    bot.button(IDELabel.Button.NEXT).click();
	    //final String CODE_COMPATIBILITY = IDELabel.NewDroolsProjectDialog.CODE_COMPATIBLE_WITH_50_DROOLS;
	    final String CODE_COMPATIBILITY = IDELabel.NewDroolsProjectDialog.CODE_COMPATIBLE_WITH_51_DROOLS_OR_ABOVE;
	    if (CODE_COMPATIBILITY.equals(IDELabel.NewDroolsProjectDialog.CODE_COMPATIBLE_WITH_51_DROOLS_OR_ABOVE)) {
	    	DroolsAllBotTests.RULE_FLOW_FILE_NAME = DroolsAllBotTests.RULE_FLOW_SAMPLE_FILE_NAME;
	    } else {
	    	DroolsAllBotTests.RULE_FLOW_FILE_NAME = DroolsAllBotTests.RULE_FLOW_RF_FILE_NAME;
	    }
	    bot.comboBoxWithLabel(IDELabel.NewDroolsProjectDialog.GENERATE_CODE_COMPATIBLE_WITH_COMBO_BOX_LABEL)
	      .setSelection(CODE_COMPATIBILITY);
	    bot.button(IDELabel.Button.FINISH).click();
  }
  
  /**
   * Creates new Drools project
   * @param droolsProjectName
   */
  private void createDroolsProjectTest(String droolsProjectName){
    createDroolsProject(droolsProjectName);
    SWTTestExt.util.waitForAll(30*1000L);
    bot.waitForNumberOfShells(1, 60);
    assertTrue("Project "
      + droolsProjectName 
      + " was not created properly.",SWTEclipseExt.isProjectInPackageExplorer(bot,droolsProjectName));
    String projectPath = File.separator + droolsProjectName;
    SWTBotTreeItem[] errors = ProblemsView.getFilteredErrorsTreeItems(bot,null ,projectPath, null,null);
    assertTrue("Project "
        + droolsProjectName 
        + " was not created properly. There are these errors: "
        + SWTEclipseExt.getFormattedTreeNodesText(errors),
        errors == null || errors.length == 0);
    SWTBotTreeItem[] warnings = ProblemsView.getFilteredWarningsTreeItems(bot,null ,projectPath, null,null);
    assertTrue("Project "
        + droolsProjectName 
        + " was not created properly. There are these warnings: "
        + SWTEclipseExt.getFormattedTreeNodesText(warnings),
        warnings == null || warnings.length == 0);
    
  }
  /**
   * Runs newly created Drools project and check result
   * @param droolsProjectName
   */
  private void runNewDroolsProject(String droolsProjectName){
    console.clearConsole();
    bot.sleep(5000L);

    SWTBotTreeItem tiTestFile = packageExplorer.selectTreeItem(DroolsAllBotTests.DROOLS_TEST_JAVA_TREE_NODE, 
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,
        DroolsAllBotTests.SRC_MAIN_JAVA_TREE_NODE,
        DroolsAllBotTests.COM_SAMPLE_TREE_NODE});
    
    eclipse.runTreeItemAsJavaApplication(tiTestFile);
    
    String consoleText = console.getConsoleText(3*1000L,60*1000L,true);
      
    assertTrue("DroolsTest.java class didn't run properly.\n" +
      "Console Text was: " + consoleText + "\n" +
      "Expected console text is: " + "Hello World\nGoodbye cruel world\n",
      "Hello World\nGoodbye cruel world\n".equals(consoleText));
  }
  /**
   * Renames Drools project and check result
   * @param droolsProjectName
   * @param renamedProjectName
   */
  private void renameDroolsProject(String droolsProjectName, String renamedProjectName){
    packageExplorer.show();
    
    bot.sleep(TIME_1S);
    
    SWTBot webProjects = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
    SWTBotTree tree = webProjects.tree();

    tree.setFocus();
    String checkResult = FileRenameHelper.checkProjectRenamingWithinPackageExplorer(bot, 
      DroolsAllBotTests.DROOLS_PROJECT_NAME, 
      ManageDroolsProject.RENAMED_DROOLS_PROJECT,
      IDELabel.Shell.RENAME_JAVA_PROJECT);
    assertNull(checkResult,checkResult);
  }

    /**
     * Deletes Drools project and check result
     * 
     * @param droolsProjectName
     */
    private void deleteDroolsProject(final String droolsProjectName) {
    	bot.sleep(Timing.time10S()); // because of NFS filesystem
        packageExplorer.deleteProject(droolsProjectName, true);
        assertFalse("Drools project: " + droolsProjectName + " was not deleted properly",
        		packageExplorer.existsResource(droolsProjectName));
    }
}

