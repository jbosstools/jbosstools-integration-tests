package org.jboss.tools.portlet.ui.bot.test.example;

import org.jboss.tools.portlet.ui.bot.test.RequirementAwareUsageWindowClosingSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	JavaPortletExampleRuntime4x.class, 
	JSFPortletExampleRuntime4x.class, 
	SeamPortletExampleRuntime4x.class,
	JSFPortletExampleRuntime5x.class,
	RichFacesPortletExampleRuntime5x.class,
	SeamPortletExampleRuntime5x.class
	})
public class ExamplesSuite {

}
