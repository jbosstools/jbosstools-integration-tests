package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.ServerManagementTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite concentrating on specific features
 * 
 * @author lfabriko
 * 
 */
@SuiteClasses({
	ServerManagementTest.class
})
@RunWith(TeiidSuite.class)
public class FeatureTests {

}
