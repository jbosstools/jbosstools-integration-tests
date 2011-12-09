package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortlet;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject;
import org.jboss.tools.portlet.ui.bot.test.core.RunJavaPortletOnServer;
import org.jboss.tools.portlet.ui.bot.test.example.JSFPortletExample;
import org.jboss.tools.portlet.ui.bot.test.example.JavaPortletExample;
import org.jboss.tools.portlet.ui.bot.test.example.SeamPortletExample;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletRuntime4x;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletRuntime5x;
import org.jboss.tools.portlet.ui.bot.test.jsf.RunJSFPortletOnServer;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletProject;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletRuntime4x;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletRuntime5x;
import org.jboss.tools.portlet.ui.bot.test.seam.RunSeamPortletOnServer;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	CreateJavaPortletProject.class,
	CreateJavaPortlet.class, 
	RunJavaPortletOnServer.class, 
	CreateJSFPortletProject.class,
	CreateJSFPortletRuntime4x.class, 
	CreateJSFPortletRuntime5x.class, 
	RunJSFPortletOnServer.class,
	CreateSeamPortletProject.class,
	CreateSeamPortletRuntime4x.class,
	CreateSeamPortletRuntime5x.class, 
	RunSeamPortletOnServer.class, 
	JavaPortletExample.class, 
	JSFPortletExample.class, 
	SeamPortletExample.class
	})
public class AllTestsSuite {

}
