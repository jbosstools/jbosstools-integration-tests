package org.jboss.tools.portlet.ui.bot.test.server;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	RunAsLoadsPortalURLRuntime4x.class, 
	RunAsLoadsPortalURLRuntime5x.class
	})
public class PortletServerTestSuite {

}
