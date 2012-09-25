package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletGatein;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletJBPortal;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject;
import org.jboss.tools.portlet.ui.bot.test.core.HotJavaPortletDeploymentGatein;
import org.jboss.tools.portlet.ui.bot.test.core.LoadJavaPortletInBrowserJBPortal;
import org.jboss.tools.portlet.ui.bot.test.core.RunJavaPortletOnServer;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	CreateJavaPortletProject.class,
	CreateJavaPortletJBPortal.class, 
	CreateJavaPortletGatein.class,
	RunJavaPortletOnServer.class,
	LoadJavaPortletInBrowserJBPortal.class,
	HotJavaPortletDeploymentGatein.class
	})
public class StableTestsSuite {

}
