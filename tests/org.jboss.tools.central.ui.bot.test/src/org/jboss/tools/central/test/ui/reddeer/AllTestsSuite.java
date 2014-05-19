package org.jboss.tools.central.test.ui.reddeer;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(RedDeerSuite.class)
@SuiteClasses({
	ArchetypesTest.class,
	ExamplesTest.class
	})
public class AllTestsSuite {


}
