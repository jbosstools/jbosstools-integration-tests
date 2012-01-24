package org.jboss.tools.portlet.ui.bot.test.jsf;

import org.jboss.tools.portlet.ui.bot.test.GateinStartupFix;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	GateinStartupFix.class,
	CreateJSFPortletProject.class,
	CreateJSFPortletRuntime4x.class,
	CreateJSFPortletRuntime5x.class,
	RunJSFPortletOnServer.class, 
	LoadJSFPortletInBrowserRuntime4x.class
	})
public class JSFPortletTestSuite {

}
