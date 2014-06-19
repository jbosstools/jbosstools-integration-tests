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
import org.jboss.tools.openshift.ui.bot.test.adapter.CreateAdapterFromServerView;
import org.jboss.tools.openshift.ui.bot.test.adapter.CreateAdapterFromExplorer;
import org.jboss.tools.openshift.ui.bot.test.adapter.SwitchProjectDeployment;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationFromGithub;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationThroughShellMenu;
import org.jboss.tools.openshift.ui.bot.test.application.CreateQuickstart;
import org.jboss.tools.openshift.ui.bot.test.application.DeployApplicationBinary;
import org.jboss.tools.openshift.ui.bot.test.application.ImportAndDeployGitHubProject;
import org.jboss.tools.openshift.ui.bot.test.application.ImportApplicationThroughOpenShiftExplorer;
import org.jboss.tools.openshift.ui.bot.test.application.ImportApplicationThroughServersView;
import org.jboss.tools.openshift.ui.bot.test.application.OpenShiftDebugFeatures;
import org.jboss.tools.openshift.ui.bot.test.application.RepublishApplication;
import org.jboss.tools.openshift.ui.bot.test.application.RestartApplication;
import org.jboss.tools.openshift.ui.bot.test.application.create.CreateDeleteScalablePythonApp;
import org.jboss.tools.openshift.ui.bot.test.cartridge.CannotEmbedConflictCartridges;
import org.jboss.tools.openshift.ui.bot.test.cartridge.EmbedCartridge;
import org.jboss.tools.openshift.ui.bot.test.connection.Connection;
import org.jboss.tools.openshift.ui.bot.test.connection.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.connection.MultipleAccounts;
import org.jboss.tools.openshift.ui.bot.test.connection.SecurityStorage;
import org.jboss.tools.openshift.ui.bot.test.domain.CreateMultipleDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.DeleteMultipleDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.RenameDomain;
import org.jboss.tools.openshift.ui.bot.util.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <b>OpenShift RedDeer TestSuite</b>
 * 
 * Tests for OpenShift Online.
 * 
 * @author mlabuda@redhat.com
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	/* Connection */
	Connection.class, 
	SecurityStorage.class,
	MultipleAccounts.class,
	ManageSSH.class, 
	
	/* Domain */
	CreateMultipleDomain.class,
 	DeleteMultipleDomain.class,
 	RenameDomain.class,

	/* Application */
 	ImportAndDeployGitHubProject.class,
 	ImportApplicationThroughOpenShiftExplorer.class,
 	ImportApplicationThroughServersView.class,
 	CreateApplicationThroughShellMenu.class,
 	CreateApplicationFromGithub.class,
 	DeployApplicationBinary.class,
 	CreateAdapterFromExplorer.class,
 	CreateAdapterFromServerView.class,
 	CreateQuickstart.class,
	EmbedCartridge.class,
	CannotEmbedConflictCartridges.class,
	SwitchProjectDeployment.class,
	CreateDeleteScalablePythonApp.class,
	RepublishApplication.class,
	OpenShiftDebugFeatures.class,
 	RestartApplication.class,
 	
 	CleanUp.class
})
public class OpenShiftOnlineBotTests {
	
}
