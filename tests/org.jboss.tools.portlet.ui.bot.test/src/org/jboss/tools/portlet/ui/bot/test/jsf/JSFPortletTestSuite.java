package org.jboss.tools.portlet.ui.bot.test.jsf;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	CreateJSFPortletProject.class,
	CreateJSFPortletJBPortal.class,
	CreateJSFPortletGatein.class,
	RunJSFPortletOnServer.class, 
	LoadJSFPortletInBrowserJBPortal.class, 
	HotJSFPortletDeploymentGatein.class
	})
public class JSFPortletTestSuite {

}
