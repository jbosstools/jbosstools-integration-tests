package org.jboss.tools.drools.reddeer.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.test.functional.DroolsRuntimeManagementTest;
import org.jboss.tools.drools.reddeer.test.functional.NewResourcesTest;
import org.jboss.tools.drools.reddeer.test.functional.PerspectiveTest;
import org.jboss.tools.drools.reddeer.test.functional.ProjectManagementTest;
import org.jboss.tools.drools.reddeer.test.functional.RulesManagementTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
    PerspectiveTest.class,
    DroolsRuntimeManagementTest.class,
    ProjectManagementTest.class,
    NewResourcesTest.class,
    RulesManagementTest.class
})
public class BrmsTestSuite {

}
