/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.reddeer.utils.v2.CleanUp;
import org.jboss.tools.openshift.ui.bot.test.application.adapter.ID801SwitchProjectDeploymentTest;
import org.jboss.tools.openshift.ui.bot.test.application.adapter.ID802ServerAdapterOverviewTest;
import org.jboss.tools.openshift.ui.bot.test.application.adapter.ID803ServerAdapterHandlingTest;
import org.jboss.tools.openshift.ui.bot.test.application.adapter.ID804CreateServerAdapterTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID901DeleteMoreApplicationsTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID902DeployGitProjectTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID903ApplicationMarkersTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID904DeployApplicationWARArchiveTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID905ManageSnapshotsTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID906RestoreApplicationFromSnapshotTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID907DownloadableCartridgeContentAssistTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID301OpenNewApplicationWizardTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID302OpenNewApplicationWizardWithoutConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID303OpenNewApplicationWizardWithoutSSHKeyTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID304OpenNewApplicationWizardWithoutDomainTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID305CartridgeContentAssistTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID306PreselectLastUsedConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.application.basic.ID307WizardDataProcessingTest;
import org.jboss.tools.openshift.ui.bot.test.application.cartridge.ID601EmbedCartridgeTest;
import org.jboss.tools.openshift.ui.bot.test.application.cartridge.ID602oAddConflictCartridgesTest;
import org.jboss.tools.openshift.ui.bot.test.application.cartridge.ID603AddDownloadableEmbeddableCartridgeTest;
import org.jboss.tools.openshift.ui.bot.test.application.cartridge.ID604AddJenkinsCartridgeWithoutJenkinsApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.cartridge.ID605ShowWarningOnMultipleCartridgeRemoveTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID401CreateNewApplicationViaExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID402DeleteOpenShiftApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID403CreateNewApplicationViaShellTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID404CreateNewApplicationViaCentralTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID405CreateQuickstartTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID406CreateApplicationOnDownloadableCartridgeTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID407CreateApplicationFromExistingAndChangeRemoteNameTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID408ApplicationPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID409CreateApplicationWithoutAdapterTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID410CreateScalableApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID412UseAnotherDomainTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID413CreateApplicationWithEmbeddableCartridgeTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID414CreateApplicationFromExistingProjectTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID415DisableMavenBuildTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID416CreateApplicationOnSourceCodeFromGithubTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID417CreateApplicationWithEnvironmentVariablesTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID701ModifyAndRepublishApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID702AddMavenProfileTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID703RefreshApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID704RestartApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID705TailFilesTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID706PortForwardingTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID707HandleEnvironmentVariablesTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID708ShowInBrowserTest;
import org.jboss.tools.openshift.ui.bot.test.application.importing.ID501ImportApplicationViaExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.application.importing.ID502ImportApplicationViaMenuTest;
import org.jboss.tools.openshift.ui.bot.test.application.importing.ID503ImportApplicationViaAdapterTest;
import org.jboss.tools.openshift.ui.bot.test.common.ID001RemoteRequestTimeoutTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID101OpenOpenShiftExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID102OpenNewConnectionShellTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID103oCreateNewConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID104InvalidCredentialsValidationTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID105DefaultServerTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID106RemoveConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID107oHandleMoreAccountsTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID109EditConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID110SecurityStorageTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID111OpenNewConnectionLinkTest;
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
 * <b>OpenShift RedDeer TestSuite</b>
 * Test suite for OpenShift Online instance.
 * 
 * @author mlabuda@redhat.com
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
		// Common
		ID001RemoteRequestTimeoutTest.class,
		
		// Connection
		ID101OpenOpenShiftExplorerTest.class,
		ID102OpenNewConnectionShellTest.class,
		ID103oCreateNewConnectionTest.class,
		ID104InvalidCredentialsValidationTest.class,
		ID105DefaultServerTest.class,
		ID106RemoveConnectionTest.class,
		ID107oHandleMoreAccountsTest.class,
		ID109EditConnectionTest.class,
		ID110SecurityStorageTest.class,
		ID111OpenNewConnectionLinkTest.class,
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
		ID301OpenNewApplicationWizardTest.class,
		ID302OpenNewApplicationWizardWithoutConnectionTest.class,
		ID303OpenNewApplicationWizardWithoutSSHKeyTest.class,
		ID304OpenNewApplicationWizardWithoutDomainTest.class,
		ID305CartridgeContentAssistTest.class,
		ID306PreselectLastUsedConnectionTest.class,
		ID307WizardDataProcessingTest.class,
		
		// Application - creation
		ID401CreateNewApplicationViaExplorerTest.class,
		ID402DeleteOpenShiftApplicationTest.class,
		ID403CreateNewApplicationViaShellTest.class,
		ID404CreateNewApplicationViaCentralTest.class,
		ID405CreateQuickstartTest.class,
		ID406CreateApplicationOnDownloadableCartridgeTest.class,
		ID407CreateApplicationFromExistingAndChangeRemoteNameTest.class,
		ID408ApplicationPropertiesTest.class,
		ID409CreateApplicationWithoutAdapterTest.class,
		ID410CreateScalableApplicationTest.class,
		ID412UseAnotherDomainTest.class,
		ID413CreateApplicationWithEmbeddableCartridgeTest.class,
		ID414CreateApplicationFromExistingProjectTest.class,
		ID415DisableMavenBuildTest.class,
		ID416CreateApplicationOnSourceCodeFromGithubTest.class,
		ID417CreateApplicationWithEnvironmentVariablesTest.class,

		// Application - import
		ID501ImportApplicationViaExplorerTest.class,
		ID502ImportApplicationViaMenuTest.class,
		ID503ImportApplicationViaAdapterTest.class,
		
		// Application - embedded cartridges
		ID601EmbedCartridgeTest.class,
		ID602oAddConflictCartridgesTest.class,
		ID603AddDownloadableEmbeddableCartridgeTest.class,
		ID604AddJenkinsCartridgeWithoutJenkinsApplicationTest.class,
		ID605ShowWarningOnMultipleCartridgeRemoveTest.class,
		
		// Application - handle
		ID701ModifyAndRepublishApplicationTest.class,
		ID702AddMavenProfileTest.class,
		ID703RefreshApplicationTest.class,
		ID704RestartApplicationTest.class,
		ID705TailFilesTest.class,
		ID706PortForwardingTest.class,
		ID707HandleEnvironmentVariablesTest.class,
		ID708ShowInBrowserTest.class,
		
		// Application - server adapter
		ID801SwitchProjectDeploymentTest.class,
		ID802ServerAdapterOverviewTest.class,
		ID803ServerAdapterHandlingTest.class,
		ID804CreateServerAdapterTest.class,
				
		// Application - advanced 
		ID901DeleteMoreApplicationsTest.class,
		ID902DeployGitProjectTest.class,		
		ID903ApplicationMarkersTest.class,
		ID904DeployApplicationWARArchiveTest.class,
		ID905ManageSnapshotsTest.class,
		ID906RestoreApplicationFromSnapshotTest.class,
		ID907DownloadableCartridgeContentAssistTest.class,
		
		CleanUp.class,
})
public class OpenShiftOnlineV2BotTests {
	
}
