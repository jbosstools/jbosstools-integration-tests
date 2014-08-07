package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.application.advanced.ID904DeployApplicationWARArchiveTest;
import org.jboss.tools.openshift.application.advanced.ID905ManageSnapshotsTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID306PreselectLastUsedConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID307WizardDataProcessingTest;
import org.jboss.tools.openshift.ui.bot.test.application.cartridge.ID601EmbedCartridgeTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID406CreateApplicationOnDownloadableCartridgeTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID417CreateApplicationWithEnvironmentVariablesTest;
import org.jboss.tools.openshift.ui.bot.test.application.importing.ID503ImportApplicationViaAdapterTest;
import org.jboss.tools.openshift.ui.bot.test.common.ID001RemoteRequestTimeoutTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID103xCreateNewConnectionEnterpriseTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID108xHandleMoreServersTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID201NewDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID204CreateMoreDomainsTest;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID150CreateNewSSHKeyTest;
import org.jboss.tools.openshift.ui.utils.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for single tests. Put any test class there. 
 * Do not forget on requirements (connection, domain, SSH keys...).
 * Useful for new tests or debugging the existing ones.
 * 
 * @author mlabuda@redhat.com
 *
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	ID001RemoteRequestTimeoutTest.class,
	
//	ID103oCreateNewConnectionTest.class,
	ID103xCreateNewConnectionEnterpriseTest.class,
	ID108xHandleMoreServersTest.class,
	
	ID150CreateNewSSHKeyTest.class,

	ID201NewDomainTest.class,
	ID204CreateMoreDomainsTest.class,
	
	ID503ImportApplicationViaAdapterTest.class,
	ID905ManageSnapshotsTest.class,
	
	CleanUp.class})
public class TestCasesTestSuite {

}
