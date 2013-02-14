package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.tools.openshift.ui.bot.test.explorer.ConnectionProd;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateAdapter;
import org.jboss.tools.openshift.ui.bot.test.explorer.EnvVar;
import org.jboss.tools.openshift.ui.bot.test.explorer.ImportApp;
import org.jboss.tools.openshift.ui.bot.test.explorer.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.explorer.PortForward;
import org.jboss.tools.openshift.ui.bot.test.explorer.RenameDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.TailFiles;
import org.jboss.tools.openshift.ui.bot.test.explorer.WebBrowser;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <b>OpenShift SWTBot TestSuite</b>
 * <br>
 * This bot test suite will try to demonstrate a new OpenShift Application and domain life cycle, 
 * and is meant to run on CI Jenkins using matrix job which will contain all supported OS and JDK
 *  
 * @author sbunciak
 */
@SuiteClasses({ 
	ConnectionProd.class, 
	ManageSSH.class, 
	RenameDomain.class,
	ImportApp.class,
	TailFiles.class,
	EnvVar.class,
	PortForward.class,
	WebBrowser.class,
	CreateAdapter.class
	})
@RunWith(RequirementAwareSuite.class)
public class OpenShiftJenkinsBotTests {
	
}
