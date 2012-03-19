package org.jboss.tools.portlet.ui.bot.test.seam;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	CreateSeamPortletProject.class,
	CreateSeamPortletJBPortal.class,
	CreateSeamPortletGatein.class,
	RunSeamPortletOnServer.class, 
	HotSeamPortletDeploymentGatein.class
	})
public class SeamPortletTestSuite {

}
