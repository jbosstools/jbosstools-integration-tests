package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.tools.openshift.ui.bot.test.explorer.ConnectionProd;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateApp;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.DeleteApp;
import org.jboss.tools.openshift.ui.bot.test.explorer.DeleteDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.ManageSSH;
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
	CreateDomain.class,
	CreateApp.class,
	DeleteApp.class, 
	DeleteDomain.class 
	})
@RunWith(RequirementAwareSuite.class)
public class OpenShiftJenkinsBotTests {
	
}
