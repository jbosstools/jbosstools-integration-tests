package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.ui.bot.test.application.RepublishApplication;
import org.jboss.tools.openshift.ui.bot.test.domain.CreateDomain;
import org.jboss.tools.openshift.ui.bot.test.openshiftexplorer.Connection;
import org.jboss.tools.openshift.ui.bot.test.openshiftexplorer.ManageSSH;
import org.jboss.tools.openshift.ui.bot.util.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

// Add own test classes. Those 3 classes are fundamental - do not remove them. Last is recommended.
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	Connection.class,	
	ManageSSH.class, 
	CreateDomain.class,
	
	RepublishApplication.class,
 	
	CleanUp.class
})
public class TestCasesTestSuite {

}
