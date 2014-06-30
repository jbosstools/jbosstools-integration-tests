package org.jboss.tools.perf.test;

import org.jboss.tools.perf.test.core.reddeer.PerformanceRedDeerSuite;
import org.jboss.tools.perf.test.validation.CDIValidation;
import org.jboss.tools.perf.test.validation.HibernateValidation;
import org.jboss.tools.perf.test.validation.JDTValidationTest;
import org.jboss.tools.perf.test.validation.JSTValidation;
import org.jboss.tools.perf.test.validation.NoValidationTest;
import org.jboss.tools.perf.test.validation.WSTValidation;
import org.jboss.tools.perf.test.wildfly.ImportWildfly;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(PerformanceRedDeerSuite.class)
@Suite.SuiteClasses({
    NoValidationTest.class,
    JDTValidationTest.class,
    WSTValidation.class,
    HibernateValidation.class,
    CDIValidation.class,
    JSTValidation.class,
    ImportWildfly.class
})
public class PerfAllBotTests {

}
