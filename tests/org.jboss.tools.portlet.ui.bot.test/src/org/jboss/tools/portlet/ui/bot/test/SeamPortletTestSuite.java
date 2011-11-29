package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortlet;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletProject;
import org.jboss.tools.portlet.ui.bot.test.seam.RunSeamPortletOnServer;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	CreateSeamPortletProject.class,
	CreateSeamPortlet.class,
	RunSeamPortletOnServer.class
	})
public class SeamPortletTestSuite {

}
