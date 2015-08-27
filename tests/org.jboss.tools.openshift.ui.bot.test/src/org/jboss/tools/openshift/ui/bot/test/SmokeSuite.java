package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID401CreateNewApplicationViaExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID101OpenOpenShiftExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID102OpenNewConnectionShellTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID103oCreateNewConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID106RemoveConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID109EditConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID113ConnectionPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID201NewDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID202EditDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID203DeleteDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID207DomainPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID150CreateNewSSHKeyTest;
import org.jboss.tools.openshift.ui.bot.test.util.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	// OpenShift v2 smoke tests
	ID101OpenOpenShiftExplorerTest.class,
	ID102OpenNewConnectionShellTest.class,
	ID103oCreateNewConnectionTest.class,
	ID106RemoveConnectionTest.class,
	ID109EditConnectionTest.class,
	ID113ConnectionPropertiesTest.class,
	
	ID150CreateNewSSHKeyTest.class,
	
	ID201NewDomainTest.class,
	ID202EditDomainTest.class,
	ID203DeleteDomainTest.class,
	ID207DomainPropertiesTest.class,
	
	ID401CreateNewApplicationViaExplorerTest.class,

	CleanUp.class,
})
public class SmokeSuite {

}
