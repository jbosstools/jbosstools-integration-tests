package org.jboss.tools.portlet.ui.bot.test.core;

import org.jboss.tools.portlet.ui.bot.test.RequirementAwareUsageWindowClosingSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	CreateJavaPortletProject.class,
	CreateJavaPortlet.class, 
	RunJavaPortletOnServer.class, 
	LoadJavaPortletInBrowserRuntime4x.class
	})
public class JavaPortletTestSuite {

}
