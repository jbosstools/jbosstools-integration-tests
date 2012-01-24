package org.jboss.tools.portlet.ui.bot.test.seam;

import org.jboss.tools.portlet.ui.bot.test.GateinStartupFix;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	GateinStartupFix.class,
	CreateSeamPortletProject.class,
	CreateSeamPortletRuntime4x.class,
	CreateSeamPortletRuntime5x.class,
	RunSeamPortletOnServer.class
	})
public class SeamPortletTestSuite {

}
