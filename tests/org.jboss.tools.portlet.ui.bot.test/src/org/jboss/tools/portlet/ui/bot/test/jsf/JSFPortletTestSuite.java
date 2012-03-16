package org.jboss.tools.portlet.ui.bot.test.jsf;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	CreateJSFPortletProject.class,
	CreateJSFPortletRuntime4x.class,
	CreateJSFPortletRuntime5x.class,
	RunJSFPortletOnServer.class, 
	LoadJSFPortletInBrowserRuntime4x.class
	})
public class JSFPortletTestSuite {

}
