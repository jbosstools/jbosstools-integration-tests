package org.jboss.tools.portlet.ui.bot.test.core;

import org.jboss.tools.portlet.ui.bot.test.GateinStartupFix;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	GateinStartupFix.class,
	CreateJavaPortletProject.class,
	CreateJavaPortletRuntime4x.class,
	CreateJavaPortletRuntime5x.class,
	RunJavaPortletOnServer.class, 
	LoadJavaPortletInBrowserRuntime4x.class
	})
public class JavaPortletTestSuite {

}
