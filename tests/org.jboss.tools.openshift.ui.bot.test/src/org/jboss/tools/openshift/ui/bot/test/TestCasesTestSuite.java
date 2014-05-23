package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.ui.bot.test.connection.Connection;
import org.jboss.tools.openshift.ui.bot.test.connection.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.domain.CreateDomain;
import org.jboss.tools.openshift.ui.bot.util.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

// Add own test classes. Those 3 classes are fundamental - do not remove them. Last is recommended.
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	Connection.class,	
	ManageSSH.class, 
	CreateDomain.class,
	
	// Here place a test class
	
	CleanUp.class
})
public class TestCasesTestSuite {

}
