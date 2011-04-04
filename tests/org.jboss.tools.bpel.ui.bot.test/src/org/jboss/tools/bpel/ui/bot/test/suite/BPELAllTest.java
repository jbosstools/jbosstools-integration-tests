package org.jboss.tools.bpel.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.bpel.ui.bot.test.ActivityModelingTest;
import org.jboss.tools.bpel.ui.bot.test.AssignActivityTest;
import org.jboss.tools.bpel.ui.bot.test.FaultModelingTest;
import org.jboss.tools.bpel.ui.bot.test.OdeDeployTest;
import org.jboss.tools.bpel.ui.bot.test.SimpleModelingTest;
import org.jboss.tools.bpel.ui.bot.test.WizardTest;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({OdeDeployTest.class  /*WizardTest.class, ActivityModelingTest.class,*/ /* AssignActivityTest.class,*/ /*SimpleModelingTest.class,  FaultModelingTest.class*/})
@RunWith(RequirementAwareSuite.class)
public class BPELAllTest extends TestSuite {

	@BeforeClass
	public static void setUpSuite() {
		BPELTest.prepare();
	}

	@AfterClass
	public static void tearDownSuite() {
		BPELTest.clean();
	}
}
