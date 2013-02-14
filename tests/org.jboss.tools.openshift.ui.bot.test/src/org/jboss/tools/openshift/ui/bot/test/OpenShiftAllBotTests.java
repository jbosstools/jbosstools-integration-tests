/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.tools.openshift.ui.bot.test.explorer.ConnectionEnterprise;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateAdapter;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateJBossApp;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.DeleteApp;
import org.jboss.tools.openshift.ui.bot.test.explorer.DeleteDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.EmbedCartridges;
import org.jboss.tools.openshift.ui.bot.test.explorer.EnvVar;
import org.jboss.tools.openshift.ui.bot.test.explorer.ImportApp;
import org.jboss.tools.openshift.ui.bot.test.explorer.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.explorer.PortForward;
import org.jboss.tools.openshift.ui.bot.test.explorer.RenameDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.TailFiles;
import org.jboss.tools.openshift.ui.bot.test.explorer.WebBrowser;
import org.jboss.tools.openshift.ui.bot.test.wizard.RepublishApp;
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
	ConnectionEnterprise.class, 
	/* TODO: temporarily disable jobs to see if this test suite passes on OpenShift Enterprise
	ManageSSH.class, 
	CreateDomain.class,
	CreateJBossApp.class,
	EmbedCartridges.class,
	RepublishApp.class,
	DeleteApp.class, 
	RenameDomain.class,
	DeleteDomain.class,
	ImportApp.class,
	TailFiles.class,
	EnvVar.class,
	PortForward.class,
	WebBrowser.class,
	CreateAdapter.class
	*/
})
@RunWith(RequirementAwareSuite.class)
public class OpenShiftAllBotTests {
	
}
