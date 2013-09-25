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

import org.jboss.tools.openshift.ui.bot.test.app.CreateDeleteJBossApp;
import org.jboss.tools.openshift.ui.bot.test.app.CreateDeletePHPApp;
import org.jboss.tools.openshift.ui.bot.test.app.CreateDeletePythonApp;
import org.jboss.tools.openshift.ui.bot.test.app.CreateDeleteScaledRubyApp;
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
 * This bot test will try to demonstrate a new OpenShift Application and domain life cycle, 
 * and is meant to run on machine with OpenShift Enterprise installed to test integration of hosted OpenShift within JBDS.   
 * 
 * @author sbunciak
 */
@SuiteClasses({
	/* Connection stuff */
	Connection.class, 
	ManageSSH.class, 
	
	/* Domain */
 	DeleteDomain.class,
 	CreateDomain.class,
	RenameDomain.class,

	/* Cartridge */
	EmbedCartridges.class,
	
	/* Explorer */
//	OpenShiftDebugFeatures.class,
//	CreateAdapter.class,
	
	/* App */
//	CreateDeleteJBossApp.class,
	//CreateDeleteEWSApp.class,
	//CreateDeletePHPApp.class,
	//CreateDeletePythonApp.class,
//	CreateDeleteScaledRubyApp.class,
//	RestartApplication.class,
	//RepublishApp.class,
})
@RunWith(RequirementAwareSuite.class)
public class OpenShiftAllBotTests {
	
}
