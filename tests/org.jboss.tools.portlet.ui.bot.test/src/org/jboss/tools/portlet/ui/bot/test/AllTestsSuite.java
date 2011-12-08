package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortlet;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject;
import org.jboss.tools.portlet.ui.bot.test.core.RunJavaPortletOnServer;
import org.jboss.tools.portlet.ui.bot.test.example.JSFPortletExample;
import org.jboss.tools.portlet.ui.bot.test.example.JavaPortletExample;
import org.jboss.tools.portlet.ui.bot.test.example.SeamPortletExample;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortlet;
import org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject;
import org.jboss.tools.portlet.ui.bot.test.jsf.RunJSFPortletOnServer;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortlet;
import org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletProject;
import org.jboss.tools.portlet.ui.bot.test.seam.RunSeamPortletOnServer;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareUsageWindowClosingSuite.class)
@Suite.SuiteClasses({
	CreateJavaPortletProject.class,
	CreateJavaPortlet.class, 
	RunJavaPortletOnServer.class, 
	CreateJSFPortletProject.class,
	CreateJSFPortlet.class, 
	RunJSFPortletOnServer.class,
	CreateSeamPortletProject.class, 
	CreateSeamPortlet.class, 
	RunSeamPortletOnServer.class, 
	JavaPortletExample.class, 
	JSFPortletExample.class, 
	SeamPortletExample.class
	})
public class AllTestsSuite {

}
