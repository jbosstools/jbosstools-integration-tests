package org.jboss.tools.portlet.ui.bot.test.example;

import org.jboss.tools.portlet.ui.bot.test.RequirementAwareUsageWindowClosingSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	JavaPortletExample.class, 
	JSFPortletExample.class, 
	SeamPortletExample.class
	})
public class ExamplesSuite {

}
