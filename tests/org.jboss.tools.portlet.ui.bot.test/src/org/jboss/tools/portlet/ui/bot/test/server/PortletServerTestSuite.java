package org.jboss.tools.portlet.ui.bot.test.server;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	RunAsLoadsPortalURLJBPortal.class, 
	RunAsLoadsPortalURLGatein.class
	})
public class PortletServerTestSuite {

}
