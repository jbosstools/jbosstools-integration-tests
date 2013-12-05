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

import org.jboss.tools.openshift.ui.bot.test.app.CreateDeleteEWSApp;
import org.jboss.tools.openshift.ui.bot.test.app.CreateDeleteJBossApp;
import org.jboss.tools.openshift.ui.bot.test.app.CreateDeletePHPApp;
import org.jboss.tools.openshift.ui.bot.test.app.CreateDeletePythonApp;
import org.jboss.tools.openshift.ui.bot.test.app.CreateDeleteScaledRubyApp;
import org.jboss.tools.openshift.ui.bot.test.app.RepublishApp;
import org.jboss.tools.openshift.ui.bot.test.app.RestartApplication;
import org.jboss.tools.openshift.ui.bot.test.cartridge.EmbedCartridges;
import org.jboss.tools.openshift.ui.bot.test.domain.CreateDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.DeleteDomain;
import org.jboss.tools.openshift.ui.bot.test.domain.RenameDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.Connection;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateAdapter;
import org.jboss.tools.openshift.ui.bot.test.explorer.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.explorer.OpenShiftDebugFeatures;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <b>OpenShift SWTBot TestSuite</b>
 * <br>
 * Test for OpenShift Enterprise private cloud. 
 * <b>
 * Please do not change the order of tests - relationship between automated tests and TCMS
 * <b/>
 * 
 * @author mlabuda
 */
@SuiteClasses({
	/* Connection stuff */
	Connection.class, 
	ManageSSH.class, 
	
	/* Domain*/
	CreateDomain.class,
 	DeleteDomain.class,
 	RenameDomain.class,

	/* Application creation*/
 	// TODO create app from github template
 	// TODO deploy existing app
 	CreateAdapter.class,
	EmbedCartridges.class,
	// TODO Conflict cartridge 
	RepublishApp.class,
	OpenShiftDebugFeatures.class,
 	RestartApplication.class, 
 	// TODO import application
 	// TODO maven profile
 	// TODO multimaven app
 	
	/* Applications*/ 
	CreateDeleteJBossApp.class,
	CreateDeleteEWSApp.class, 
	CreateDeletePHPApp.class,
	CreateDeletePythonApp.class,
	CreateDeleteScaledRubyApp.class, 

})
@RunWith(RequirementAwareSuite.class)
public class OpenShiftEnterpriseBotTests {
	
}
