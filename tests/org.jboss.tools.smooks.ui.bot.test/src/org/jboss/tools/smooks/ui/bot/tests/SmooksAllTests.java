package org.jboss.tools.smooks.ui.bot.tests;

import org.jboss.tools.smooks.ui.bot.testcase.SmooksProject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( {SmooksProject.class})
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
