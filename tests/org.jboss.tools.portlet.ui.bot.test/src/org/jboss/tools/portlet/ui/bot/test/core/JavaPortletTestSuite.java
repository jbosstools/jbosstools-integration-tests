package org.jboss.tools.portlet.ui.bot.test.core;

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
public class JavaPortletTestSuite {

}
