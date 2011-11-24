package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortlet;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject;
import org.jboss.tools.portlet.ui.bot.test.jsf.RunJSFPortletOnServer;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	CreateJSFPortletProject.class,
	CreateJSFPortlet.class,
	RunJSFPortletOnServer.class
	})
public class JSFPortletTestSuite {

}
