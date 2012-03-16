package org.jboss.tools.portlet.ui.bot.test.example;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
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
