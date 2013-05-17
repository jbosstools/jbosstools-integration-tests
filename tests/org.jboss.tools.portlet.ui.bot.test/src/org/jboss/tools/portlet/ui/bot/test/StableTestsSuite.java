package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.portlet.ui.bot.test.compatibility.JBDS5vs7CompatibilityGatein;
import org.jboss.tools.portlet.ui.bot.test.compatibility.JBDS6vs7CompatibilityGatein;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletGatein;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletJBPortal;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject;
import org.jboss.tools.portlet.ui.bot.test.core.HotJavaPortletDeploymentGatein;
import org.jboss.tools.portlet.ui.bot.test.core.LoadJavaPortletInBrowserJBPortal;
import org.jboss.tools.portlet.ui.bot.test.core.RunJavaPortletOnServer;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletGatein;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletJBPortal;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject;
import org.jboss.tools.portlet.ui.bot.test.jsf.HotJSFPortletDeploymentGatein;
import org.jboss.tools.portlet.ui.bot.test.jsf.LoadJSFPortletInBrowserJBPortal;
import org.jboss.tools.portlet.ui.bot.test.jsf.RunJSFPortletOnServer;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletGatein;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletJBPortal;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletProject;
import org.jboss.tools.portlet.ui.bot.test.seam.RunSeamPortletOnServer;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	CreateJavaPortletProject.class,
	CreateJavaPortletJBPortal.class, 
	CreateJavaPortletGatein.class,
	RunJavaPortletOnServer.class,
	LoadJavaPortletInBrowserJBPortal.class,
	HotJavaPortletDeploymentGatein.class,
	CreateJSFPortletProject.class,
	CreateJSFPortletJBPortal.class, 
	CreateJSFPortletGatein.class, 
	RunJSFPortletOnServer.class,
	LoadJSFPortletInBrowserJBPortal.class,
	HotJSFPortletDeploymentGatein.class,
	CreateSeamPortletProject.class,
	CreateSeamPortletJBPortal.class,
	CreateSeamPortletGatein.class, 
	RunSeamPortletOnServer.class,
	JBDS5vs7CompatibilityGatein.class,
	JBDS6vs7CompatibilityGatein.class
})
public class StableTestsSuite {

}
