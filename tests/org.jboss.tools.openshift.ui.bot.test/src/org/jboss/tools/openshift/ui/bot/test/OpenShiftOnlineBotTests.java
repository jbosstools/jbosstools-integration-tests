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
import org.jboss.tools.openshift.ui.bot.test.application.CreateAdapterFromServerView;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationThroughShellMenu;
import org.jboss.tools.openshift.ui.bot.test.application.CreateApplicationFromGithub;
import org.jboss.tools.openshift.ui.bot.test.application.ImportAndDeployGitHubProject;
import org.jboss.tools.openshift.ui.bot.test.application.ImportApplicationFromOpenShift;
import org.jboss.tools.openshift.ui.bot.test.application.RepublishApplication;
import org.jboss.tools.openshift.ui.bot.test.application.RestartApplication;
import org.jboss.tools.openshift.ui.bot.test.cartridge.EmbedCartridge;
import org.jboss.tools.openshift.ui.bot.test.domain.CreateMultipleDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.DeleteMultipleDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.RenameDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.Connection;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateAdapter;
import org.jboss.tools.openshift.ui.bot.test.explorer.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.explorer.MultipleAccounts;
import org.jboss.tools.openshift.ui.bot.test.explorer.OpenShiftDebugFeatures;
import org.jboss.tools.openshift.ui.bot.util.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <b>OpenShift RedDeer TestSuite</b>
 * <br>
 * Tests for OpenShift Online - production.
 * <b>
 * Please do not change the order of tests - relationship between automated tests and TCMS
 * <b/>
 * 
 * @author mlabuda
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	/* Connection */
	Connection.class, 
	MultipleAccounts.class,
	ManageSSH.class, 
	
	/* Domain */
	CreateMultipleDomain.class,
 	DeleteMultipleDomain.class,
 	RenameDomain.class,

	/* Application */
 	ImportAndDeployGitHubProject.class,
 	ImportApplicationFromOpenShift.class,
 	CreateApplicationThroughShellMenu.class,
 	CreateApplicationFromGithub.class,
 	CreateAdapter.class,
 	CreateAdapterFromServerView.class,
	EmbedCartridge.class,
	// TODO Conflict cartridge 
	RepublishApplication.class,
	OpenShiftDebugFeatures.class,
 	RestartApplication.class,
 	
 	CleanUp.class
})
public class OpenShiftOnlineBotTests {
	
}
