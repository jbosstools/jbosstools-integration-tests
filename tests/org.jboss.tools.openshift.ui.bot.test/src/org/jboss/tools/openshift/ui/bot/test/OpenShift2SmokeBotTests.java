package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.reddeer.utils.CleanUpOS2;
import org.jboss.tools.openshift.ui.bot.test.application.adapter.ID804CreateServerAdapterTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID302OpenNewApplicationWizardWithoutConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID303OpenNewApplicationWizardWithoutSSHKeyTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID304OpenNewApplicationWizardWithoutDomainTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID401CreateNewApplicationViaExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID402DeleteOpenShiftApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID403CreateNewApplicationViaShellTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID404CreateNewApplicationViaCentralTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID405CreateQuickstartTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID408ApplicationPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID701ModifyAndRepublishApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.importing.ID501ImportApplicationViaExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.application.importing.ID502ImportApplicationViaMenuTest;
import org.jboss.tools.openshift.ui.bot.test.application.importing.ID503ImportApplicationViaAdapterTest;
import org.jboss.tools.openshift.ui.bot.test.common.ID001RemoteRequestTimeoutTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID101OpenOpenShiftExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID103CreateNewConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID104InvalidCredentialsValidationTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID105DefaultServerTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID106RemoveConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID107oHandleMoreAccountsTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID108xHandleMoreServersTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID109EditConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID110SecurityStorageTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID112RefreshConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID113ConnectionPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID201NewDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID202EditDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID203DeleteDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID204CreateMoreDomainsTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID205ManageDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID206RefreshDomainTest;
import org.jboss.tools.openshift.ui.bot.test.domain.ID207DomainPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID150CreateNewSSHKeyTest;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID151RemoveSSHKeyTest;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID152AddExistingSSHKeyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <b>OpenShift 2 Smoke Tests suite</b>
 * 
 * @author mlabuda@redhat.com
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
		// Common
		ID001RemoteRequestTimeoutTest.class,
		
		// Connection
		ID101OpenOpenShiftExplorerTest.class,
		ID103CreateNewConnectionTest.class,
		ID104InvalidCredentialsValidationTest.class,
		ID105DefaultServerTest.class,
		ID106RemoveConnectionTest.class,
		ID107oHandleMoreAccountsTest.class,
		ID108xHandleMoreServersTest.class,
		ID109EditConnectionTest.class,
		ID110SecurityStorageTest.class,
		ID112RefreshConnectionTest.class,
		ID113ConnectionPropertiesTest.class,
		
		// SSH Keys
		ID150CreateNewSSHKeyTest.class,
		ID151RemoveSSHKeyTest.class,
		ID152AddExistingSSHKeyTest.class,
	
		// Domain
		ID201NewDomainTest.class,
		ID202EditDomainTest.class,
		ID203DeleteDomainTest.class,
		ID204CreateMoreDomainsTest.class,
		ID205ManageDomainTest.class, 
		ID206RefreshDomainTest.class,
		ID207DomainPropertiesTest.class,

		// Application
		ID302OpenNewApplicationWizardWithoutConnectionTest.class,
		ID303OpenNewApplicationWizardWithoutSSHKeyTest.class,
		ID304OpenNewApplicationWizardWithoutDomainTest.class,
		
		// Application - creation
		ID401CreateNewApplicationViaExplorerTest.class,
		ID402DeleteOpenShiftApplicationTest.class,
		ID403CreateNewApplicationViaShellTest.class,
		ID404CreateNewApplicationViaCentralTest.class,
		ID405CreateQuickstartTest.class,
		ID408ApplicationPropertiesTest.class,
		
		// Application - import
		ID501ImportApplicationViaExplorerTest.class,
		ID502ImportApplicationViaMenuTest.class,
		ID503ImportApplicationViaAdapterTest.class,
		
		// Application - handle
		ID701ModifyAndRepublishApplicationTest.class,
	
		// Application - server adapter
		ID804CreateServerAdapterTest.class, 

		CleanUpOS2.class,
})
public class OpenShift2SmokeBotTests {

}
