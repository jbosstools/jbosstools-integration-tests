package org.jboss.tools.portlet.ui.bot.test.example;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	JavaPortletExampleJBPortal.class, 
	JSFPortletExampleJBPortal.class, 
	SeamPortletExampleJBPortal.class,
	JSFPortletExampleGatein.class,
	RichFacesPortletExampleGatein.class,
	SeamPortletExampleGatein.class
	})
public class ExamplesSuite {

}
