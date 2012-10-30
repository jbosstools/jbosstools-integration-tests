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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.drools.ui.bot.test.smoke.DecisionTableTest;
import org.jboss.tools.drools.ui.bot.test.smoke.DomainSpecificLanguageEditorTest;
import org.jboss.tools.drools.ui.bot.test.smoke.DroolsRulesEditorTest;
import org.jboss.tools.drools.ui.bot.test.smoke.DroolsViewsTest;
import org.jboss.tools.drools.ui.bot.test.smoke.GuvnorRepositoriesTest;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsProject;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsRules;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageDroolsRuntime;
import org.jboss.tools.drools.ui.bot.test.smoke.ManageJbpmRuntime;
import org.jboss.tools.drools.ui.bot.test.smoke.OpenDroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.smoke.RuleFlowTest;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.ServerBean;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * This is Drools swtbot test case for JBoss Tools.
 * 
 * @author Vladimir Pakan
 * 
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
    OpenDroolsPerspective.class,
    ManageDroolsRuntime.class,
    ManageJbpmRuntime.class,
    ManageDroolsProject.class,
    ManageDroolsRules.class,
    DroolsRulesEditorTest.class,
    // GuidedDroolsRulesEditorTest.class,
    DomainSpecificLanguageEditorTest.class,
    RuleFlowTest.class,
    DecisionTableTest.class,
    GuvnorRepositoriesTest.class,
    DroolsViewsTest.class
})
public class DroolsAllBotTests extends SWTTestExt {
    public static final String DROOLS_PROJECT_NAME = "droolsTest";
    public static final String DROOLS_RUNTIME_NAME = "Drools Test Runtime";
    public static String DROOLS_RUNTIME_LOCATION = null;
    public static String JBPM_RUNTIME_LOCATION = null;
    public static final String JBPM_RUNTIME_NAME = "jBPM Test Runtime";
    public static String CREATE_DROOLS_RUNTIME_LOCATION = null;
    public static String CREATE_JBPM_RUNTIME_LOCATION = null;
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
    public static final String RULE_FLOW_SAMPLE_FILE_NAME = "sample.bpmn";
    // this variable should be set in ManageDroolsProject class according to used Drools version
    public static String RULE_FLOW_FILE_NAME = RULE_FLOW_SAMPLE_FILE_NAME; // default choice
    public static final String DECISION_TABLE_JAVA_TEST_FILE_NAME = "DecisionTableTest.java";
    public static final String USE_EXTERNAL_DROOLS_RUNTIME_PROPERTY_NAME = "use-external-drools-runtime";
    public static final String EXTERNAL_DROOLS_RUTIME_HOME_PROPERTY_NAME = "external-drools-runtime-home";
    public static final String USE_EXTERNAL_JBPM_RUNTIME_PROPERTY_NAME = "use-external-jbpm-runtime";
    public static final String EXTERNAL_JBPM_RUTIME_HOME_PROPERTY_NAME = "external-jbpm-runtime-home";
    public static final String GUVNOR_REPOSITORY_URL_PROPERTY_NAME = "guvnor-repository-url";
    private static boolean USE_EXTERNAL_DROOLS_RUNTIME;
    private static boolean USE_EXTERNAL_JBPM_RUNTIME;
    private static boolean isFirstRun = true;

    private static String testDroolsRuntimeName = null;
    private static String testDroolsRuntimeLocation = null;
    private static String testJbpmRuntimeName = null;
    private static String testJbpmRuntimeLocation = null;
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

    public static String getTestJbpmRuntimeName() {
        return testJbpmRuntimeName;
    }

    public static void setTestJbpmRuntimeName(String testJbmpRuntimeName) {
        DroolsAllBotTests.testJbpmRuntimeName = testJbmpRuntimeName;
    }

    public static String getTestJbpmRuntimeLocation() {
        return testJbpmRuntimeLocation;
    }

    public static void setTestJbpmRuntimeLocation(String testJbpmRuntimeLocation) {
        DroolsAllBotTests.testJbpmRuntimeLocation = testJbpmRuntimeLocation; 
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

    private static void setGuvnorRepositoryRootTreeItem(String guvnorRepositoryRootTreeItem) {
        DroolsAllBotTests.guvnorRepositoryRootTreeItem = guvnorRepositoryRootTreeItem;
    }

    @BeforeClass
    public static void setUpTest() {
        if (isFirstRun) {
            isFirstRun = false;
        } else {
            return;
        }
        props = util.loadProperties(Activator.PLUGIN_ID);
        String guvnorRepositoryUrl = props.getProperty(DroolsAllBotTests.GUVNOR_REPOSITORY_URL_PROPERTY_NAME);
        if (guvnorRepositoryUrl != null) {
            DroolsAllBotTests.setGuvnorRepositoryUrl(guvnorRepositoryUrl);
            DroolsAllBotTests.setGuvnorRepositoryRootTreeItem("http://localhost:8080" + guvnorRepositoryUrl);
        }
        String useExternalDroolRuntime = props.getProperty(DroolsAllBotTests.USE_EXTERNAL_DROOLS_RUNTIME_PROPERTY_NAME);
        DroolsAllBotTests.USE_EXTERNAL_DROOLS_RUNTIME = useExternalDroolRuntime != null && useExternalDroolRuntime.equalsIgnoreCase("true");
        String droolsRuntimeLocation = props.getProperty(DroolsAllBotTests.EXTERNAL_DROOLS_RUTIME_HOME_PROPERTY_NAME);
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (droolsRuntimeLocation == null || droolsRuntimeLocation.trim().length() == 0) {
            DroolsAllBotTests.DROOLS_RUNTIME_LOCATION = tmpDir;
        } else {
            DroolsAllBotTests.DROOLS_RUNTIME_LOCATION = droolsRuntimeLocation;
        }
        DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION = tmpDir + File.separator + "drools";
        // Create directory for Drools Runtime which will be created as a part of test
        new File(DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION).mkdir();

        String useExternalJbpmRuntime = props.getProperty(DroolsAllBotTests.USE_EXTERNAL_JBPM_RUNTIME_PROPERTY_NAME);
        DroolsAllBotTests.USE_EXTERNAL_JBPM_RUNTIME = useExternalJbpmRuntime != null && useExternalJbpmRuntime.equalsIgnoreCase("true");
        String jbpmRuntimeLocation = props.getProperty(DroolsAllBotTests.EXTERNAL_JBPM_RUTIME_HOME_PROPERTY_NAME);
        if (jbpmRuntimeLocation == null || jbpmRuntimeLocation.trim().length() == 0) {
            DroolsAllBotTests.JBPM_RUNTIME_LOCATION = tmpDir;
        } else {
            DroolsAllBotTests.JBPM_RUNTIME_LOCATION = jbpmRuntimeLocation;
        }
        DroolsAllBotTests.CREATE_JBPM_RUNTIME_LOCATION = tmpDir + File.separator + "jBPM";
        // Create directory for jBPM Runtime which will be created as a part of test
        new File(DroolsAllBotTests.CREATE_JBPM_RUNTIME_LOCATION).mkdir();

        try {
            bot.button(IDELabel.Button.NO).click();
        } catch (WidgetNotFoundException wnfe) {
            // Do nothing ignore this error
        }
        try {
            SWTBotView welcomeView = eclipse.getBot().viewByTitle(IDELabel.View.WELCOME);
            welcomeView.close();
        } catch (WidgetNotFoundException wnfe) {
            // Do nothing ignore this error
        }
        // Close JBoss Central editor
        for (SWTBotEditor editor : bot.editors()) {
            if (IDELabel.View.JBOSS_CENTRAL.equals(editor.getTitle())) {
                editor.close();
                break;
            }
        }
        for (SWTBotShell shell : bot.shells()) {
            if (IDELabel.Shell.SUBCLIPSE_USAGE.equals(shell.getText())) {
                shell.bot().checkBox().deselect();
                shell.bot().button(IDELabel.Button.OK).click();
                break;
            }
        }
        eclipse.openPerspective(PerspectiveType.JAVA);
        eclipse.maximizeActiveShell();

        // Removes legacy files after previous run
        ServerBean server = TestConfigurator.currentConfig.getServer();
        if (server != null) {
            final String serverHome = server.runtimeHome;
            if (serverHome != null) {
                deleteGuvnorRepositoryIfExists(serverHome + "/bin/");
            }
        }
    }

    private static void deleteGuvnorRepositoryIfExists(final String pathToDirectoryWithRepository) {
        delete(new File(pathToDirectoryWithRepository + "repository.xml"));
        delete(new File(pathToDirectoryWithRepository + "repository"));
    }

    public static boolean delete(final File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            for (final String s : file.list()) {
                delete(new File(file, s));
            }
        }
        return file.delete();
    }

    public static boolean useExternalDroolsRuntime() {
        return USE_EXTERNAL_DROOLS_RUNTIME;
    }
    
    public static boolean useExternalJbpmRuntime() {
        return USE_EXTERNAL_JBPM_RUNTIME;
    }

    @AfterClass
    public static void tearDownTest() {
        // delete created drools runtime
        SWTUtilExt.deleteDirectory(DroolsAllBotTests.CREATE_DROOLS_RUNTIME_LOCATION);
    }
}