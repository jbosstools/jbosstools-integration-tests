package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.ui.bot.test.connection.ID101OpenOpenShiftExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID102OpenNewConnectionShellTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID103CreateNewConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID106RemoveConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID109EditConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID113ConnectionPropertiesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	// OpenShift v2 smoke tests
	ID101OpenOpenShiftExplorerTest.class,
	ID102OpenNewConnectionShellTest.class,
	ID103CreateNewConnectionTest.class,
	ID106RemoveConnectionTest.class,
	ID109EditConnectionTest.class,
	ID113ConnectionPropertiesTest.class,	
})
public class SmokeSuite {

}
