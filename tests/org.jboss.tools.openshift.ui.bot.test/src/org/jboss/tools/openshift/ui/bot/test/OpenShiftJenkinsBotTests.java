package org.jboss.tools.openshift.ui.bot.test;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.explorer.Connection;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateApp;
import org.jboss.tools.openshift.ui.bot.test.explorer.CreateDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.DeleteApp;
import org.jboss.tools.openshift.ui.bot.test.explorer.DeleteDomain;
import org.jboss.tools.openshift.ui.bot.test.explorer.EmbedCartrides;
import org.jboss.tools.openshift.ui.bot.test.explorer.ManageSSH;
import org.jboss.tools.openshift.ui.bot.test.explorer.RenameDomain;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
	Connection.class, 
	ManageSSH.class, 
	CreateDomain.class,
	CreateApp.class, 
	EmbedCartrides.class,
	DeleteApp.class, 
	RenameDomain.class,
	DeleteDomain.class })
@RunWith(RequirementAwareSuite.class)
public class OpenShiftJenkinsBotTests {
	public static String JBOSS_APP_NAME = TestProperties
			.get("openshift.jbossapp.name") + new Date().getTime();
}
