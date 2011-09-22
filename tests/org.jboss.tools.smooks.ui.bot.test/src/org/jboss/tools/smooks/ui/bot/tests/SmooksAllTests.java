package org.jboss.tools.smooks.ui.bot.tests;

import org.jboss.tools.smooks.ui.bot.testcase.SmooksCSV2Java2XML;
import org.jboss.tools.smooks.ui.bot.testcase.SmooksProject;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RequirementAwareSuite.class)
@SuiteClasses( {SmooksProject.class, SmooksCSV2Java2XML.class})

public class SmooksAllTests extends SmooksTest {

	@BeforeClass
	public static void setUpSuite() {		
		SmooksTest.prepare();		
	}

	@AfterClass
	public static void tearDownSuite() {
		SmooksTest.clean();
	}
}
