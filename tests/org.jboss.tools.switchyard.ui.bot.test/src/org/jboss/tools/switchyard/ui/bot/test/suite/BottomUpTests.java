package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.switchyard.ui.bot.test.BottomUpBPELTest;
import org.jboss.tools.switchyard.ui.bot.test.BottomUpCamelTest;
import org.jboss.tools.switchyard.ui.bot.test.BottomUpEJBTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite
 * 
 * @author apodhrad
 * 
 */
@SuiteClasses({ BottomUpBPELTest.class, BottomUpCamelTest.class, BottomUpEJBTest.class })
@RunWith(SwitchyardSuite.class)
public class BottomUpTests extends TestSuite {

}
