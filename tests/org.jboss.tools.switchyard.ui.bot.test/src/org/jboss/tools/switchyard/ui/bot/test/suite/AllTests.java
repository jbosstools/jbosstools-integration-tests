package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.switchyard.ui.bot.test.BottomUpBPELTest;
import org.jboss.tools.switchyard.ui.bot.test.BottomUpCamelTest;
import org.jboss.tools.switchyard.ui.bot.test.SimpleTest;
import org.jboss.tools.switchyard.ui.bot.test.WSProxyRESTTest;
import org.jboss.tools.switchyard.ui.bot.test.WSProxySOAPTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite
 * 
 * @author apodhrad
 *
 */
@SuiteClasses({
	SimpleTest.class,
	WSProxySOAPTest.class,
	WSProxyRESTTest.class,
	BottomUpBPELTest.class,
	BottomUpCamelTest.class
})
@RunWith(SwitchyardSuite.class)
public class AllTests extends TestSuite {

}
