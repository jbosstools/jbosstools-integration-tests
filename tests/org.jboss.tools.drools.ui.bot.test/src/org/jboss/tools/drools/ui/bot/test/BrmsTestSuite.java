package org.jboss.tools.drools.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.functional.DroolsRuntimeManagementTest;
import org.jboss.tools.drools.ui.bot.test.functional.NewResourcesTest;
import org.jboss.tools.drools.ui.bot.test.functional.PerspectiveTest;
import org.jboss.tools.drools.ui.bot.test.functional.ProjectManagementTest;
import org.jboss.tools.drools.ui.bot.test.functional.RulesEditorTest;
import org.jboss.tools.drools.ui.bot.test.functional.RulesManagementTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
    PerspectiveTest.class,
    DroolsRuntimeManagementTest.class,
    ProjectManagementTest.class,
    NewResourcesTest.class,
    RulesManagementTest.class,
    RulesEditorTest.class
})
public class BrmsTestSuite {

}
