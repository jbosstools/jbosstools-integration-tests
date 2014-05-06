package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationDownloadableCartridge;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationWithDifferentGearSize;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationWithoutSSHKey;
import org.jboss.tools.openshift.ui.bot.test.connection.ConnectionEnterprise;
import org.jboss.tools.openshift.ui.bot.test.connection.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.domain.CreateDomain;
import org.jboss.tools.openshift.ui.bot.util.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

// Add own test classes. Those 3 classes are fundamental - do not remove them. Last is recommended.
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	ConnectionEnterprise.class,	
	ManageSSH.class, 
	CreateDomain.class,
	
	CreateApplicationDownloadableCartridge.class,
	
	CleanUp.class
})
public class TestCasesTestSuite {

}
