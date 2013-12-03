/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test;

import java.util.List;
import java.util.Vector;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.gen.INewObject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.junit.After;
import org.junit.Before;

/**
 * Base class for SWTBot tests of Aerogear JBoss Tools plugin.
 * 
 * @author sbunciak
 * 
 */
public class AerogearBotTest extends SWTTestExt {
  protected static final String CORDOVA_PROJECT_NAME = "Cordova_prj";
  protected static final String CORDOVA_APP_NAME = "Cordova Test Application";

  /**
   * Creates a new hybrid mobile project in workspace.
   * 
   * @param projectName
   * @param appName
   * @param appId
   */
  public void createHTMLHybridMobileApplication(String projectName,
      String appName, String appId) {

    open.newObject(new INewObject() {

      @Override
      public String getName() {
        return "Hybrid Mobile (Cordova) Application Project";
      }

      @Override
      public List<String> getGroupPath() {
        List<String> l = new Vector<String>();
        l.add("Mobile");
        return l;
      }

    });

    bot.text(0).typeText(projectName);
    bot.textInGroup("Mobile Application", 0).typeText(appName);
    bot.textInGroup("Mobile Application", 1).typeText(appId);
    bot.button("Finish").click();

    bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
  }

  public void runTreeItemInAndroidEmulator(SWTBotTreeItem treeItem) {
    treeItem.select();
    treeItem.click();

    // TODO: Order/content of context many may change
    // TODO: Need to check presence of Android SDK installation
    bot.menu("Run").menu("Run As").menu("2 Run on Android Emulator").click();

    bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
  }

  public void runTreeItemOnAndroidDevice(SWTBotTreeItem treeItem) {
    treeItem.select();
    treeItem.click();

    // TODO: Order/content of context many may change
    // TODO: Need to check presence of Android SDK installation
    bot.menu("Run").menu("Run As").menu("2 Run on Android Emulator").click();

    bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
  }

  public void runTreeItemWithCordovaSim(SWTBotTreeItem treeItem) {
    treeItem.select();
    treeItem.click();

    bot.menu("Run").menu("Run As").menu("3 Run with CordovaSim").click();

    bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
    bot.sleep(TIME_5S);
  }

  public void openInConfigEditor(String projectName) {
    new DefaultTreeItem(projectName,"www","config.xml").select();
    new ContextMenu("Open With","Cordova Configuration Editor").select();
    bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
  }

  @Before
  public void setUp() {
    eclipse.openPerspective(PerspectiveType.JBOSS);
    createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME,
        AerogearBotTest.CORDOVA_APP_NAME, "org.jboss.example.cordova");

    assertTrue(projectExplorer.existsResource(AerogearBotTest.CORDOVA_PROJECT_NAME));
  }

  @After
  public void tearDown() {
    projectExplorer.deleteProject(AerogearBotTest.CORDOVA_PROJECT_NAME, true);
  }
  /**
   * Sets LogCat Filter properties for projoectName via Run Configurations
   * Currently just adds displaying debug messages to console
   * and runs project on Android Emulator  
   * @param projectName
   */
  public void setLogCatFilterPropsAndRun(String projectName){
    bot.menu("Run").menu("Run Configurations...").click();
    bot.shell("Run Configurations").activate();
    SWTBotTreeItem tiAndroidEmulator = bot.tree().getTreeItem("Android Emulator");
    tiAndroidEmulator.select();
    tiAndroidEmulator.expand();
    try{
      tiAndroidEmulator.getNode(projectName).select();
    } catch (WidgetNotFoundException wnfe){
      bot.toolbarButtonWithTooltip("New launch configuration").click();
      bot.textWithLabel("Name:").setText(projectName);
      bot.textWithLabel("Project:").setText(projectName);
    }
    bot.cTabItem(0).activate();
    SWTBotText txFilter = bot.textWithLabel("Log Filter:");
    String filter = txFilter.getText();
    if (!filter.contains("chromium:V")){
      txFilter.setText("chromium:V " + filter);
      bot.button(IDELabel.Button.APPLY).click();
    }
    bot.button(IDELabel.Button.RUN).click();
    bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
  }
}
