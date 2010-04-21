/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.drools.ui.bot.test;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsRuntime;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsProject;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsRules;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * This is struts swtbot testcase for JBoss Tools.
 * 
 * @author Vladimir Pakan
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ManageDroolsRuntime.class,
  ManageDroolsProject.class,
  ManageDroolsRules.class})  
public class DroolsAllBotTests extends SWTTestExt {
  public static final String DROOLS_PROJECT_NAME = "droolsTest";
  public static final String DROOLS_RUNTIME_NAME = "Drools Test Runtime";
  public static String DROOLS_RUNTIME_LOCATION = null;
  public static String CREATE_DROOLS_RUNTIME_LOCATION = null;
  public static String SRC_MAIN_JAVA_TREE_NODE = "src/main/java";
  public static String SRC_MAIN_RULES_TREE_NODE = "src/main/rules";
  public static String COM_SAMPLE_TREE_NODE = "com.sample";
  public static String DROOLS_TEST_JAVA_TREE_NODE = "DroolsTest.java";
  public static final String TEST_DROOLS_RULE_NAME = "TestRule.drl";
  public static final String SAMPLE_DROOLS_RULE_NAME = "Sample.drl";
  private static String testDroolsRuntimeName = null;
  public static String getTestDroolsRuntimeName() {
    return testDroolsRuntimeName;
  }

  public static void setTestDroolsRuntimeName(String testDroolsRuntimeName) {
    DroolsAllBotTests.testDroolsRuntimeName = testDroolsRuntimeName;
  }

  public static String getTestDroolsRuntimeLocation() {
    return testDroolsRuntimeLocation;
  }

  public static void setTestDroolsRuntimeLocation(String testDroolsRuntimeLocation) {
    DroolsAllBotTests.testDroolsRuntimeLocation = testDroolsRuntimeLocation;
  }

  private static String testDroolsRuntimeLocation = null;
  @BeforeClass
  public static void setUpTest() {
    DroolsAllBotTests.DROOLS_RUNTIME_LOCATION = System.getProperty("java.io.tmpdir");
    DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION = DroolsAllBotTests.DROOLS_RUNTIME_LOCATION + File.separator + "drools";
    // Create directory for Drools Runtime which will be created as a part of test
    new File(DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION).mkdir();
    properties = util.loadProperties(Activator.PLUGIN_ID);
    try{
      SWTBotView welcomeView = eclipse.getBot().viewByTitle(IDELabel.View.WELCOME);
      welcomeView.close();
    } catch (WidgetNotFoundException wnfe){
      // Do nothing ignore this error
    }
    eclipse.openPerspective(PerspectiveType.JAVA);	
  }

  @AfterClass
  public static void tearDownTest() {
    // delete created drools runtime
    SWTUtilExt.deleteDirectory(DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION);
  }   
}