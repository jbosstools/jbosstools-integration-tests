/*******************************************************************************
 * Copyright (c) 2007-20013 Red Hat, Inc.
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
import org.jboss.tools.openshift.ui.bot.test.adapter.CreateAdapterFromExplorer;
import org.jboss.tools.openshift.ui.bot.test.adapter.CreateAdapterFromServerView;
import org.jboss.tools.openshift.ui.bot.test.adapter.SwitchProjectDeployment;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationDownloadableCartridge;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationFromGithub;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationThroughShellMenu;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationWithDifferentGearSize;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationWithoutSSHKey;
import org.jboss.tools.openshift.ui.bot.test.application.CreateQuickstart;
import org.jboss.tools.openshift.ui.bot.test.application.DeployApplicationBinary;
import org.jboss.tools.openshift.ui.bot.test.application.ImportAndDeployGitHubProject;
import org.jboss.tools.openshift.ui.bot.test.application.ImportApplicationThroughOpenShiftExplorer;
import org.jboss.tools.openshift.ui.bot.test.application.ImportApplicationThroughServersView;
import org.jboss.tools.openshift.ui.bot.test.application.OpenShiftDebugFeatures;
import org.jboss.tools.openshift.ui.bot.test.application.RepublishApplication;
import org.jboss.tools.openshift.ui.bot.test.application.RestartApplication;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteEWSApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteJBossApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteJenkinsApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeletePHPApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeletePerlApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeletePythonApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteRubyApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteScalableEWSApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteScalableJBossApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteScalablePHPApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteScalablePerlApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteScalablePythonApp;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteScalableRubyApp;
import org.jboss.tools.openshift.ui.bot.test.cartridge.CannotEmbedConflictCartridges;
import org.jboss.tools.openshift.ui.bot.test.cartridge.EmbedCartridges;
import org.jboss.tools.openshift.ui.bot.test.connection.ConnectionEnterprise;
import org.jboss.tools.openshift.ui.bot.test.connection.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.connection.MultipleAccounts;
import org.jboss.tools.openshift.ui.bot.test.connection.SecurityStorage;
import org.jboss.tools.openshift.ui.bot.test.domain.CreateDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.DeleteDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.RenameDomain;
import org.jboss.tools.openshift.ui.bot.util.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <b>OpenShift RedDeer TestSuite</b>
 * Test for OpenShift Enterprise private cloud. 
 * 
 * @author mlabuda@redhat.com
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	/* Connection stuff */
	ConnectionEnterprise.class, 
	SecurityStorage.class,
	MultipleAccounts.class, 
	
	/* Domain*/
	CreateDomain.class,
 	DeleteDomain.class,
 	RenameDomain.class,
 	
 	/* SSH */
    CreateApplicationWithoutSSHKey.class,
	ManageSSH.class,

	/* Application creation*/
	CreateQuickstart.class,
 	ImportAndDeployGitHubProject.class, 
 	ImportApplicationThroughOpenShiftExplorer.class,
 	ImportApplicationThroughServersView.class,
 	CreateApplicationThroughShellMenu.class,
    CreateApplicationFromGithub.class,
    CreateApplicationDownloadableCartridge.class, 
    CreateApplicationWithDifferentGearSize.class,
    DeployApplicationBinary.class,
 	CreateAdapterFromExplorer.class,
	CreateAdapterFromServerView.class,
	SwitchProjectDeployment.class,
	EmbedCartridges.class,
	CannotEmbedConflictCartridges.class,
	RepublishApplication.class,
	OpenShiftDebugFeatures.class,
 	RestartApplication.class, 
 	
	/* Applications*/ 
	CreateDeleteJBossApp.class,
	CreateDeleteEWSApp.class, 
	CreateDeletePHPApp.class,
	CreateDeletePythonApp.class,
	CreateDeleteRubyApp.class, 
	CreateDeleteJenkinsApp.class, 
	CreateDeletePerlApp.class, 
	
	/* Scalable applications */
	CreateDeleteScalableEWSApp.class, 
	CreateDeleteScalableJBossApp.class, 
	CreateDeleteScalablePHPApp.class,
	CreateDeleteScalablePythonApp.class, 
	CreateDeleteScalablePerlApp.class, 
	CreateDeleteScalableRubyApp.class, 
	
	CleanUp.class
})
public class OpenShiftEnterpriseBotTests {
	
}
