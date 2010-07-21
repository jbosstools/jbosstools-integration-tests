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

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.awt.event.KeyEvent;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Matcher;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.jboss.tools.ui.bot.ext.config.requirement.PrepareViews;
import org.jboss.tools.ui.bot.ext.config.requirement.RequirementNotFulfilledException;
import org.jboss.tools.ui.bot.ext.config.requirement.StartServer;
import org.jboss.tools.ui.bot.ext.config.requirement.StopServer;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.GuvnorGuvnorResourceHistory;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.DragAndDropHelper;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.JobName;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.jboss.tools.ui.bot.ext.view.GuvnorRepositories;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.junit.Test;
/**
 * Tests Guvnor Repositories
 * @author Vladimir Pakan
 *
 */
public class GuvnorRepositoriesTest extends SWTTestExt{
  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(GuvnorRepositoriesTest.class);
  private static final String GUVNOR_TEST_FILE = "Dummy rule.drl";
  private static final String GUVNOR_REPOSITORY_IMPORT_TEST_FILE = "Underage.brl";
  private static final String GUVNOR_REPOSITORY_HISTORY_TEST_FILE = "MortgageModel.model.drl";
  private GuvnorRepositories guvnorRepositories = new GuvnorRepositories(); 
  /**
   * Tests Guvnor Repositories
   */
  @Test
  public void testGuvnorRepositories() {
    setGuvnorTemplate();
    startGuvnor();
    addGuvnorRepository();
    deleteGuvnorRepository();
    addGuvnorRepository();
    openGuvnorConsole();
    drillIntoFunctionalityCheck();
    browseGuvnorRepository(GuvnorRepositoriesTest.GUVNOR_TEST_FILE);
    guvnorFunctionalityCheck(GuvnorRepositoriesTest.GUVNOR_TEST_FILE,
      DroolsAllBotTests.SAMPLE_DROOLS_RULE_NAME,
      GuvnorRepositoriesTest.GUVNOR_REPOSITORY_IMPORT_TEST_FILE);
    repositoryHistoryCheck(GuvnorRepositoriesTest.GUVNOR_REPOSITORY_HISTORY_TEST_FILE);
    stopGuvnor();
  }

  /**
   * Adds Guvnor Repository
   */
  private void addGuvnorRepository(){
    eclipse.openPerspective(PerspectiveType.GUVNOR_REPOSITORY_EXPLORING);
    SWTBot guvnorRepositoriesBot = guvnorRepositories.show().bot();
    SWTUtilExt.getViewToolbarButtonWithTooltip(
      guvnorRepositories.show(),
      IDELabel.GuvnorRepositories.ADD_GUVNOR_REPOSITORY_TOOLTIP)
        .click();
    eclipse.waitForShell("");
    guvnorRepositoriesBot.activeShell().bot().button(IDELabel.Button.FINISH).click();
    assertTrue("Guvnor repository was not created properly",
      guvnorRepositoriesBot.tree().rowCount() == 1);
  }
  /**
   * Deletes Guvnor Repostiry
   */
  private void deleteGuvnorRepository(){
    SWTBot guvnorRepositoriesBot = guvnorRepositories.show().bot();;
    SWTBotTree guvnorRepositoryTree = guvnorRepositoriesBot.tree();
    guvnorRepositoryTree.select(0);
    SWTUtilExt.getViewToolbarButtonWithTooltip(
      guvnorRepositories.show(),
      IDELabel.GuvnorRepositories.REMOVE_GUVNOR_REPOSITORY_TOOLTIP)
        .click();
    guvnorRepositoriesBot.shell(IDELabel.GuvnorRepositories.REMOVE_GUVNOR_REPOSITORY_DIALOG_TITLE)
      .activate();
    bot.button(IDELabel.Button.OK).click();
    assertTrue("Guvnor repository was not deleted properly",
      guvnorRepositoriesBot.tree().rowCount() == 0);
  }
  /**
   * Opens Guvnor Console
   */
  private void openGuvnorConsole(){
    SWTBot guvnorRepositoriesBot = guvnorRepositories.show().bot();
    SWTBotTree guvnorRepositoryTree = guvnorRepositoriesBot.tree();
    SWTBotTreeItem tiGuvnorRepository = guvnorRepositoryTree.getAllItems()[0];
    ContextMenuHelper.prepareTreeItemForContextMenu(guvnorRepositoryTree, tiGuvnorRepository);
    new SWTBotMenu(ContextMenuHelper.getContextMenu(guvnorRepositoryTree, 
      IDELabel.Menu.OPEN_GUVNOR_CONSOLE, false)).click();
    bot.sleep(Timing.time5S());
    SWTBotBrowserExt browser = bot.browserByTitle(IDELabel.GuvnorConsole.GUVNOR_CONSOLE_TITLE);
    browser.clickOnButtonViaJavaScript(0, bot);
    browser.clickOnButtonViaJavaScript(IDELabel.GuvnorConsole.BUTTON_YES_INSTALL_SAMPLES, bot);
    bot.sleep(Timing.time1S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_RIGHT);
    bot.sleep(Timing.time1S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    bot.sleep(Timing.time10S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_ENTER);
  }
  /**
   * Browse Guvnor Repository and open fileToOpenFile
   * @param fileToOpen
   */
  private void browseGuvnorRepository(String fileToOpen){
    
    guvnorRepositories.show();

    guvnorRepositories.openFile(Timing.time3S(),IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
      IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM,
      IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM,
      fileToOpen);
    
    assertTrue("File from Guvnor Repository was not opened properly. File " + fileToOpen + " is not opened in editor",
      SWTEclipseExt.existEditorWithLabel(bot,fileToOpen + " (Read only)"));
    
  }
  /**
   * Starts Guvnor AS
   */
  private void startGuvnor(){
    try {
      new StartServer().fulfill();
      new PrepareViews().fulfill();
    } catch (RequirementNotFulfilledException e) {
      throw new RuntimeException(e);
    } 
  }
  /**
   * Stops Guvnor AS
   */
  private void stopGuvnor(){
    try {
      new StopServer().fulfill();
    } catch (RequirementNotFulfilledException e) {
      throw new RuntimeException(e);
    } 
  }
  /**
   * Imports file with fileName to Drools project and check update and commit Guvnor functionality
   * @param fileName
   * @param sampleFileName
   * @param importFileName
   */
  private void guvnorFunctionalityCheck(String fileName, String sampleFileName, String importFileName){
    eclipse.openPerspective(PerspectiveType.JAVA);
    guvnorRepositories.show().bot();
    SWTBotTreeItem tiGuvnorFile = guvnorRepositories.selectTreeItem(Timing.time3S(),fileName,
      new String[]{IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
        IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM,
        IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM});
    tiGuvnorFile.select();
    SWTBot packageExplorerBot = packageExplorer.show().bot();
    SWTBotTreeItem tiDroolRuleDir = packageExplorer.selectTreeItem(DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME});
    DragAndDropHelper.dragAndDropOnTo(tiGuvnorFile.widget,tiDroolRuleDir.widget);
    bot.sleep(Timing.time5S());
    bot.shell(IDELabel.Shell.COPY_FILE_FROM_GUVNOR_TO_PACKAGE_EXPLORER).activate();
    bot.button(IDELabel.Button.OK).click();
    SWTBotTree packageExplorerTree = packageExplorerBot.tree();
    // File is renamed because there is appended Guvnor info to Tree Item Label
    // So we need to get real label of Tree Item and use it later
    SWTBotTreeItem tiDroolRuleFile = SWTEclipseExt.getTreeItemOnPathStartsWith(packageExplorerBot, 
      packageExplorerTree, 
      Timing.time1S(),
      fileName, 
      new String[]{DroolsAllBotTests.DROOLS_PROJECT_NAME,
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE});
    SWTBotEditor editor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME, 
      DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
      tiDroolRuleFile.getText());
    
    assertTrue("File moved from Guvnor Repository to Drools project was not opened properly. File " + fileName + " is not opened in editor",
      SWTEclipseExt.existEditorWithLabel(bot,fileName));
    // Test Update from Guvnor Repository
    final String changeText = "#$%SWTBot Change#$%";    
    final String originalEditorText = editor.toTextEditor().getText();
    editor.toTextEditor().insertText(0, 0, changeText);
    editor.save();
    bot.sleep(Timing.time1S());
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiDroolRuleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_UPDATE);
    bot.sleep(Timing.time5S());
    assertTrue("Update from Guvnor Repository was not successful. File " + fileName + " has not updated content.",
      editor.toTextEditor().getText().equals(originalEditorText));
    // Test commit to Guvnor Repository
    editor.toTextEditor().insertText(0, 0, changeText);
    editor.save();
    bot.sleep(Timing.time1S());
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiDroolRuleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_COMMIT);
    bot.sleep(Timing.time5S());
    editor.close();
    editor = guvnorRepositories.openFile(Timing.time2S(),IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
        IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM,
        IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM,
        fileName);
    assertTrue("Commit to Guvnor Repository was not successful. File " + fileName + " was not commited properly." +
      "\nIt has content: " + editor.toTextEditor().getText() +
      "\nExpected content: " + changeText + originalEditorText,
      editor.toTextEditor().getText().equals(changeText + originalEditorText));
    // Test Add To Repository
    SWTBotTreeItem tiSampleFile = packageExplorer.selectTreeItem(sampleFileName,
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE});
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiSampleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_ADD);
    eclipse.waitForShell("");
    SWTBotShell addToGuvnorShell = packageExplorerBot.activeShell();
    SWTBot addToGuvnorDialogBot = addToGuvnorShell.bot();
    addToGuvnorDialogBot.button(IDELabel.Button.NEXT).click();
    SWTEclipseExt.getTreeItemOnPath(addToGuvnorDialogBot, 
      addToGuvnorDialogBot.tree(),
      Timing.time3S(),
      IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM, 
      new String[]{IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
      IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM})
      .select();
    bot.sleep(Timing.time5S());
    addToGuvnorDialogBot.button(IDELabel.Button.FINISH).click();
    eclipse.waitForClosedShell(addToGuvnorShell);
    boolean isAddedToGuvnorRepository = false;
    try{
      guvnorRepositories.selectTreeItem(Timing.time2S(),sampleFileName,
          new String[]{IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
            IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM,
            IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM});
      isAddedToGuvnorRepository = true;
    } catch (WidgetNotFoundException wnfe){
      isAddedToGuvnorRepository = false;
    }
    
    assertTrue("File " + sampleFileName + " was not added to Guvnor Repository.",
      isAddedToGuvnorRepository);
    // Test Deleting from Guvnor Repository file is already selected in Guvnor Repository Tree
    packageExplorerBot = packageExplorer.show().bot();
    packageExplorerTree = packageExplorerBot.tree();
    packageExplorerBot.sleep(Timing.time2S());
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiSampleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_DELETE);
    SWTBot dialogBot = packageExplorerBot.shell(IDELabel.Shell.CONFIRM_DELETE).activate().bot();
    dialogBot.button(IDELabel.Button.OK).click();
    packageExplorerBot.sleep(Timing.time2S());
    boolean isRemovedFromGuvnorRepository = false;
    try{
      guvnorRepositories.selectTreeItem(Timing.time2S(),sampleFileName,
          new String[]{IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
            IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM,
            IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM});
      isRemovedFromGuvnorRepository = false;
    } catch (WidgetNotFoundException wnfe){
      isRemovedFromGuvnorRepository = true;
    }
    assertTrue("File " + sampleFileName + " was not removed from Guvnor Repository.",
        isRemovedFromGuvnorRepository);
    // Import File From Repository
    eclipse.createNew(EntityType.RESOURCES_FROM_GUVNOR);
    bot.button(IDELabel.Button.NEXT).click();
    SWTEclipseExt.getTreeItemOnPath(
      bot,
      bot.tree(),
      Timing.time3S(),
      importFileName,
      new String[] {
        IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
        IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM,
        IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM }).select();
    bot.button(IDELabel.Button.NEXT).click();
    SWTEclipseExt.getTreeItemOnPath(bot,
      bot.tree(),
      Timing.time1S(),
      "rules",
        new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,"src","main"}).select();
    bot.button(IDELabel.Button.FINISH).click();
    util.waitForJobs(Timing.time5S(),JobName.BUILDING_WS);
    bot.sleep(Timing.time1S());
    packageExplorerBot = packageExplorer.show().bot();
    packageExplorerTree = packageExplorerBot.tree();
    boolean isAddedFromGuvnorRepository = false;
    SWTBotTreeItem tiImportRuleFile = null;
    try{
      tiImportRuleFile = SWTEclipseExt.getTreeItemOnPathStartsWith(packageExplorerBot, 
          packageExplorerTree, 
          Timing.time1S(),
          importFileName, 
          new String[]{DroolsAllBotTests.DROOLS_PROJECT_NAME,
            DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE});
      isAddedFromGuvnorRepository = true;
    } catch (WidgetNotFoundException wnfe){
      isAddedFromGuvnorRepository = false;
    }
    assertTrue("File " + importFileName + " was not added from Guvnor Repository.",
      isAddedFromGuvnorRepository);
    
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiImportRuleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_DISCONNECT);
    bot.sleep(Timing.time1S());
    // name of the file has to be without Guvnor information appended to end of file name
    // when imported from Guvnor repository
    assertTrue("File " + importFileName + " was not disconnected from Guvnor Repository.",
        tiImportRuleFile.getText().trim().equals(importFileName));    
  }
  private void drillIntoFunctionalityCheck(){
    SWTBotView guvnorReposioryView = guvnorRepositories.show();
    guvnorRepositories.selectTreeItem(Timing.time2S(), IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM, 
      new String[]{IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM});
    SWTUtilExt.getViewToolbarButtonWithTooltip(guvnorReposioryView,
      IDELabel.GuvnorRepositories.GO_INTO_GUVNOR_REPOSITORY_TOOLTIP)
        .click();
    SWTBot guvnorRepositoryBot = guvnorReposioryView.bot();
    SWTBotTree guvnorRepositoryTree = guvnorRepositoryBot.tree();
    guvnorRepositoryBot.sleep(Timing.time3S());
    assertTrue("Guvnor repository Go Into functionality doesn't work properly.\n" +
      "Expected First Tree Item in Guvnor Repository is " + IDELabel.GuvnorRepositories.DEFAULT_PACKAGE_TREE_ITEM +
      "\nBut it was " + guvnorRepositoryTree.getAllItems()[0].getText(),
      IDELabel.GuvnorRepositories.DEFAULT_PACKAGE_TREE_ITEM.equals(guvnorRepositoryTree.getAllItems()[0].getText()));
    
    guvnorRepositories.selectTreeItem(Timing.time2S(), IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM, 
      null);
    SWTUtilExt.getViewToolbarButtonWithTooltip(guvnorReposioryView,
        IDELabel.GuvnorRepositories.GO_INTO_GUVNOR_REPOSITORY_TOOLTIP)
          .click();
    guvnorRepositoryBot.sleep(Timing.time3S());
    assertTrue("Guvnor repository Go Into functionality doesn't work properly.\n" +
      "Expected First Tree Item in Guvnor Repository is " + IDELabel.GuvnorRepositories.APPLICANTDSL_DSL_TREE_ITEM +
      "\nBut it was " + guvnorRepositoryTree.getAllItems()[0].getText(),
      IDELabel.GuvnorRepositories.APPLICANTDSL_DSL_TREE_ITEM.equals(guvnorRepositoryTree.getAllItems()[0].getText()));

    SWTUtilExt.getViewToolbarButtonWithTooltip(guvnorReposioryView,
        IDELabel.GuvnorRepositories.BACK_GUVNOR_REPOSITORY_TOOLTIP)
          .click();
    guvnorRepositoryBot.sleep(Timing.time3S());
    assertTrue("Guvnor repository Back functionality doesn't work properly.\n" +
      "Expected First Tree Item in Guvnor Repository is " + IDELabel.GuvnorRepositories.DEFAULT_PACKAGE_TREE_ITEM +
      "\nBut it was " + guvnorRepositoryTree.getAllItems()[0].getText(),
      IDELabel.GuvnorRepositories.DEFAULT_PACKAGE_TREE_ITEM.equals(guvnorRepositoryTree.getAllItems()[0].getText()));

    SWTUtilExt.getViewToolbarButtonWithTooltip(guvnorReposioryView,
        IDELabel.GuvnorRepositories.HOME_GUVNOR_REPOSITORY_TOOLTIP)
          .click();
    guvnorRepositoryBot.sleep(Timing.time3S());
    assertTrue("Guvnor repository Home functionality doesn't work properly.\n" +
      "Expected First Tree Item in Guvnor Repository is " + IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM +
      "\nBut it was " + guvnorRepositoryTree.getAllItems()[0].getText(),
      IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM.equals(guvnorRepositoryTree.getAllItems()[0].getText()));    
  }
  /**
   * Check Repository History Functionality
   * @param testFileName
   */
  private void repositoryHistoryCheck (String testFileName){
    // Import File From Repository
    eclipse.createNew(EntityType.RESOURCES_FROM_GUVNOR);
    bot.button(IDELabel.Button.NEXT).click();
    SWTEclipseExt.getTreeItemOnPath(
      bot,
      bot.tree(),
      Timing.time3S(),
      testFileName,
      new String[] {
        IDELabel.GuvnorRepositories.GUVNOR_REPOSITORY_ROOT_TREE_ITEM,
        IDELabel.GuvnorRepositories.PACKAGES_TREE_ITEM,
        IDELabel.GuvnorRepositories.MORTGAGE_TREE_ITEM }).select();
    bot.button(IDELabel.Button.NEXT).click();
    SWTEclipseExt.getTreeItemOnPath(bot,
      bot.tree(),
      Timing.time1S(),
      "rules",
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,"src","main"}).select();
    bot.button(IDELabel.Button.FINISH).click();
    util.waitForJobs(Timing.time5S(),JobName.BUILDING_WS);
    bot.sleep(Timing.time1S());
    SWTBot packageExplorerBot = packageExplorer.show().bot();
    SWTBotTree packageExplorerTree = packageExplorerBot.tree();
    // File is renamed because there is appended Guvnor info to Tree Item Label
    // So we need to get real label of Tree Item and use it later
    SWTBotTreeItem tiTestRuleFile = SWTEclipseExt.getTreeItemOnPathStartsWith(packageExplorerBot, 
      packageExplorerTree, 
      Timing.time1S(),
      testFileName, 
      new String[]{DroolsAllBotTests.DROOLS_PROJECT_NAME,
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE});
    SWTBotEditor editor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME, 
      DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
      tiTestRuleFile.getText());
    // change test file
    String addedChange = "SWTBOT Change!@#$asdfghjkl)(*&^";
    editor.toTextEditor().insertText(0,0,addedChange);
    editor.save();
    bot.sleep(Timing.time1S());
    // commit changes
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiTestRuleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_COMMIT);
    bot.sleep(Timing.time5S());
    // check history
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiTestRuleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_SHOW_HISTORY);
    bot.sleep(Timing.time5S());
    SWTBotView guvnorResourceHistoryView = open.viewOpen(GuvnorGuvnorResourceHistory.LABEL);
    SWTBot guvnorResourceHistoryBot = guvnorResourceHistoryView.bot();
    SWTBotTable guvnorResourceHistoryTable = guvnorResourceHistoryView.bot().table();
    assertTrue("Guvnor Resource History table for file " + testFileName +
        " has to contain at least one record but is empty.",
      guvnorResourceHistoryTable.rowCount() > 0);
    // Compare Revisions
    String secondAddedChange = "222222" + addedChange;
    editor.toTextEditor().insertText(0,0,secondAddedChange);
    editor.saveAndClose();
    bot.sleep(Timing.time1S());
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiTestRuleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_COMPARE_WITH_VERSION);
    eclipse.waitForShell("");
    guvnorResourceHistoryBot.activeShell().bot().button(IDELabel.Button.OK).click();
    SWTBotEditor compareEditor = bot.editorByTitle("Compare");
    Matcher<StyledText> widgetOfTypeMatcher = WidgetMatcherFactory.widgetOfType(StyledText.class);
    final List<?> styledTexts = compareEditor.bot().widgets(widgetOfTypeMatcher,compareEditor.getWidget());
    String newVersionEditorText = syncExec(new StringResult() {
      public String run() {
        return ((StyledText)styledTexts.get(0)).getText();
      }
    });
    String revisionEditorText = syncExec(new StringResult() {
      public String run() {
        return ((StyledText)styledTexts.get(1)).getText();
      }
    });
    compareEditor.close();
    assertTrue("Actual version of file opened within compare editor has wrong content.\n" +
      "Content should start with " + secondAddedChange +
      "\n but is " + newVersionEditorText,newVersionEditorText.startsWith(secondAddedChange));
    assertTrue("File stored in Guvnor Repository opened within compare editor has wrong content.\n" +
      "Content should start with " + addedChange +
      "\n but is " + revisionEditorText,revisionEditorText.startsWith(addedChange));
    // Open Revision
    guvnorResourceHistoryView.show();
    guvnorResourceHistoryTable.setFocus();
    bot.sleep(Timing.time1S());
    guvnorResourceHistoryTable.select(0);
    bot.sleep(Timing.time1S());
    KeyboardHelper.pressKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    KeyboardHelper.releaseKeyCodeUsingAWT(KeyEvent.VK_ENTER);
    bot.sleep(Timing.time1S());
    SWTBotEditor revisonFileEditor = eclipse.editorStartsWith(testFileName);
    String revisionFileText = revisonFileEditor.toTextEditor().getText();
    revisonFileEditor.close();
    assertTrue("File stored in Guvnor Repository has wrong content.\n" +
        "Content should start with " + addedChange +
        "\n but is " + revisionFileText,revisionFileText.startsWith(addedChange));
    // Switch to version
    editor = packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME, 
        DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
        tiTestRuleFile.getText());
    ContextMenuHelper.prepareTreeItemForContextMenu(packageExplorerTree, tiTestRuleFile);
    ContextMenuHelper.clickContextMenu(packageExplorerTree,
      IDELabel.Menu.GUVNOR,IDELabel.Menu.GUVNOR_SWITCH_TO_VERSION);
    eclipse.waitForShell("");
    guvnorResourceHistoryBot.activeShell().bot().button(IDELabel.Button.OK).click();
    bot.sleep(Timing.time3S());
    String editorText = editor.toTextEditor().getText();    
    assertTrue("Switched version of file has wrong content.\n" +
      "Content should start with " + addedChange +
      "\n but is " + editorText,editorText.startsWith(addedChange));
  }
  /** 
   * Sets properly Guvnor Template
   */
  private void setGuvnorTemplate(){
    SWTBot dialogBot = open.preferenceOpen(ActionItem.Preference.Guvnor.LABEL);
    SWTBotText guvnorTemplateText = dialogBot.textWithLabel(IDELabel.GuvnorPropertiesDialog.GUVNOR_URL_TEMPLATE);
    guvnorTemplateText.setText(guvnorTemplateText.getText().replaceFirst("jboss-brms", "drools-guvnor"));
    dialogBot.button(IDELabel.Button.OK).click();
    SWTEclipseExt.hideWarningIfDisplayed(bot);
  }
}