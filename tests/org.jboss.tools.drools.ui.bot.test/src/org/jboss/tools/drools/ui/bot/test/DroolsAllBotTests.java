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
import org.jboss.tools.drools.ui.bot.test.smoke.DecisionTableTest;
import org.jboss.tools.drools.ui.bot.test.smoke.DomainSpecificLanguageEditorTest;
//import org.jboss.tools.drools.ui.bot.test.smoke.GuidedDroolsRulesEditorTest;
import org.jboss.tools.drools.ui.bot.test.smoke.GuvnorRepositoriesTest;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsRuntime;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsProject;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsRules;
import org.jboss.tools.drools.ui.bot.test.smoke.DroolsRulesEditorTest;
import org.jboss.tools.drools.ui.bot.test.smoke.OpenDroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.smoke.RuleFlowTest;
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
 * This is Drools swtbot test case for JBoss Tools.
 * 
 * @author Vladimir Pakan
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({OpenDroolsPerspective.class,
  ManageDroolsRuntime.class,
  ManageDroolsProject.class,
  ManageDroolsRules.class,
  DroolsRulesEditorTest.class,
  //GuidedDroolsRulesEditorTest.class,
  DomainSpecificLanguageEditorTest.class,
  RuleFlowTest.class,
  DecisionTableTest.class,
  GuvnorRepositoriesTest.class})
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
  public static final String GUIDED_DROOLS_RULE_NAME = "GuidedRule.brl";
  public static final String DOMAIN_SPECIFIC_LANGUAGE_FILE_NAME = "DslTest.dsl";
  public static final String RULE_FLOW_JAVA_TEST_FILE_NAME = "ProcessTest.java";
  public static final String RULE_FLOW_RF_FILE_NAME = "ruleflow.rf";
  public static final String DECISION_TABLE_JAVA_TEST_FILE_NAME = "DecisionTableTest.java";
  public static final String USE_EXTERNAL_DROOLS_RUNTIME_PROPERTY_NAME = "use-external-drools-runtime";
  public static final String EXTERNAL_DROOLS_RUTIME_HOME_PROPERTY_NAME = "external-drools-runtime-home";
  public static final String GUVNOR_REPOSITORY_URL_PROPERTY_NAME = "guvnor-repository-url";
  private static boolean USE_EXTERNAL_DROOLS_RUNTIME;

  private static String testDroolsRuntimeName = null;
  private static String testDroolsRuntimeLocation = null;
  private static String guvnorRepositoryUrl = null;
  private static String guvnorRepositoryRootTreeItem = "http://localhost:8080/jboss-brms/org.drools.guvnor.Guvnor/webdav/";
  
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
  
  public static String getGuvnorRepositoryUrl() {
    return guvnorRepositoryUrl;
  }

  private static void setGuvnorRepositoryUrl(String guvnorRepositoryUrl) {
    DroolsAllBotTests.guvnorRepositoryUrl = guvnorRepositoryUrl;
  }

  public static String getGuvnorRepositoryRootTreeItem() {
    return guvnorRepositoryRootTreeItem;
  }

  private static void setGuvnorRepositoryRootTreeItem(
      String guvnorRepositoryRootTreeItem) {
    DroolsAllBotTests.guvnorRepositoryRootTreeItem = guvnorRepositoryRootTreeItem;
  }

  @BeforeClass
  public static void setUpTest() {
    jbt.closeReportUsageWindowIfOpened(false);
    props = util.loadProperties(Activator.PLUGIN_ID);
    String guvnorRepositoryUrl = props.getProperty(DroolsAllBotTests.GUVNOR_REPOSITORY_URL_PROPERTY_NAME);
    if (guvnorRepositoryUrl != null){
      DroolsAllBotTests.setGuvnorRepositoryUrl(guvnorRepositoryUrl);
      DroolsAllBotTests.setGuvnorRepositoryRootTreeItem("http://localhost:8080" + guvnorRepositoryUrl);
    }
    String useExternalDroolRuntime = props.getProperty(DroolsAllBotTests.USE_EXTERNAL_DROOLS_RUNTIME_PROPERTY_NAME);
    DroolsAllBotTests.USE_EXTERNAL_DROOLS_RUNTIME = useExternalDroolRuntime != null && useExternalDroolRuntime.equalsIgnoreCase("true");
    String droolsRuntimeLocation = props.getProperty(DroolsAllBotTests.EXTERNAL_DROOLS_RUTIME_HOME_PROPERTY_NAME);
    String tmpDir = System.getProperty("java.io.tmpdir");
    if (droolsRuntimeLocation == null || droolsRuntimeLocation.length() ==0){
      DroolsAllBotTests.DROOLS_RUNTIME_LOCATION = tmpDir;
    }
    else{
      DroolsAllBotTests.DROOLS_RUNTIME_LOCATION = droolsRuntimeLocation;
    }
    DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION = tmpDir + File.separator + "drools";
    // Create directory for Drools Runtime which will be created as a part of test
    new File(DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION).mkdir();
    try{
      SWTBotView welcomeView = eclipse.getBot().viewByTitle(IDELabel.View.WELCOME);
      welcomeView.close();
    } catch (WidgetNotFoundException wnfe){
      // Do nothing ignore this error
    }
    eclipse.openPerspective(PerspectiveType.JAVA);
    eclipse.maximizeActiveShell();
  }

  public static boolean useExternalDroolsRuntime() {
    return USE_EXTERNAL_DROOLS_RUNTIME;
  }

  @AfterClass
  public static void tearDownTest() {
    // delete created drools runtime
    SWTUtilExt.deleteDirectory(DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION);
  }   
}