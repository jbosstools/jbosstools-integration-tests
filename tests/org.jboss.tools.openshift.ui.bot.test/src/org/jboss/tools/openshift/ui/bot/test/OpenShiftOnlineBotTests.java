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

import org.jboss.tools.openshift.ui.bot.test.app.CreateAdapterFromServerView;
import org.jboss.tools.openshift.ui.bot.test.app.CreateAppUsingWizard;
import org.jboss.tools.openshift.ui.bot.test.app.CreateApplicationFromGithub;
import org.jboss.tools.openshift.ui.bot.test.app.RepublishApp;
import org.jboss.tools.openshift.ui.bot.test.app.RestartApplication;
import org.jboss.tools.openshift.ui.bot.test.cartridge.EmbedCartridge;
import org.jboss.tools.openshift.ui.bot.test.domain.CreateDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.DeleteDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.RenameDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.Connection;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateAdapter;
import org.jboss.tools.openshift.ui.bot.test.explorer.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.explorer.MultipleAccounts;
import org.jboss.tools.openshift.ui.bot.test.explorer.OpenShiftDebugFeatures;
import org.jboss.tools.openshift.ui.bot.util.CleanUp;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.jboss.reddeer.junit.runner.RedDeerSuite;

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
	CreateDomain.class,
 	DeleteDomain.class,
 	RenameDomain.class,

	/* Application */
 	CreateAppUsingWizard.class,
 	CreateApplicationFromGithub.class,
 	// TODO deploy existing app
 	CreateAdapter.class,
 	CreateAdapterFromServerView.class,
	EmbedCartridge.class,
	// TODO Conflict cartridge 
	RepublishApp.class,
	OpenShiftDebugFeatures.class,
 	RestartApplication.class,
 	// TODO import application
 	// TODO maven profile
 	// TODO multimaven app
 	
 	CleanUp.class
})
public class OpenShiftOnlineBotTests {
	
}
