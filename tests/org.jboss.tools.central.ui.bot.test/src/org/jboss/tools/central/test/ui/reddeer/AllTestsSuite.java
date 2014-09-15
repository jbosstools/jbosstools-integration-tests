package org.jboss.tools.central.test.ui.reddeer;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.central.test.ui.reddeer.examples.tests.BackEndApplicationsTest;
import org.jboss.tools.central.test.ui.reddeer.examples.tests.MobileApplicationsTest;
import org.jboss.tools.central.test.ui.reddeer.examples.tests.PortalApplicationsTest;
import org.jboss.tools.central.test.ui.reddeer.examples.tests.WebApplicationExamplesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(RedDeerSuite.class)
@SuiteClasses({
//	ArchetypesTest.class,
//	WebApplicationExamplesTest.class,
	MobileApplicationsTest.class,
	BackEndApplicationsTest.class,
	PortalApplicationsTest.class,
//	BasicTests.class
	})
public class AllTestsSuite {


}