package org.jboss.tools.jbpm.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.jbpm.ui.bot.test.JBPMDeployTest;
import org.jboss.tools.jbpm.ui.bot.test.JBPMProjectTest;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ JBPMProjectTest.class, JBPMDeployTest.class,  })
@RunWith(RequirementAwareSuite.class)
public class JBPMAllTest extends TestSuite {

	@BeforeClass
	public static void setUpSuite() {
		JBPMTest.prepare();
	}

	@AfterClass
	public static void tearDownSuite() {
		JBPMTest.clean();
	}
}
