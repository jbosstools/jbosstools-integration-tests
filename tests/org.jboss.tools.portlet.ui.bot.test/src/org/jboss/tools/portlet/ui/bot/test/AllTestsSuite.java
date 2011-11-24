package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortlet;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject;
import org.jboss.tools.portlet.ui.bot.test.core.RunJavaPortletOnServer;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortlet;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject;
import org.jboss.tools.portlet.ui.bot.test.jsf.RunJSFPortletOnServer;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	CreateJavaPortletProject.class,
	CreateJavaPortlet.class, 
	RunJavaPortletOnServer.class, 
	CreateJSFPortletProject.class,
	CreateJSFPortlet.class, 
	RunJSFPortletOnServer.class
	})
public class AllTestsSuite {

}
