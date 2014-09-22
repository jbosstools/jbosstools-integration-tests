package org.jboss.tools.perf.test;

import org.jboss.tools.perf.test.core.reddeer.PerformanceRedDeerSuite;
import org.jboss.tools.perf.test.maven.validation.CDIMavenValidation;
import org.jboss.tools.perf.test.maven.validation.HibernateMavenValidation;
import org.jboss.tools.perf.test.maven.validation.FullMavenValidationTest;
import org.jboss.tools.perf.test.maven.validation.JAXRSMavenValidation;
import org.jboss.tools.perf.test.maven.validation.JPAValidationMavenTest;
import org.jboss.tools.perf.test.maven.validation.JSFMavenValidation;
import org.jboss.tools.perf.test.maven.validation.PortletMavenValidation;
import org.jboss.tools.perf.test.maven.validation.SeamMavenValidation;
import org.jboss.tools.perf.test.maven.validation.WTPMavenValidation;
import org.jboss.tools.perf.test.maven.wildfly.ImportMavenWildfly;
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
	/*
    NoValidationTest.class,
    JDTValidationTest.class,
    WSTValidation.class,
    HibernateValidation.class,
    CDIValidation.class,
    JSTValidation.class,
    
    JPAValidationMavenTest.class,
    FullMavenValidationTest.class,
    JSFMavenValidation.class,
    HibernateMavenValidation.class,
    CDIMavenValidation.class,
    JAXRSMavenValidation.class,
    PortletMavenValidation.class,
    SeamMavenValidation.class,
    WTPMavenValidation.class,
    
    ImportWildfly.class
    */
	ImportMavenWildfly.class
})
public class PerfAllBotTests {

}
