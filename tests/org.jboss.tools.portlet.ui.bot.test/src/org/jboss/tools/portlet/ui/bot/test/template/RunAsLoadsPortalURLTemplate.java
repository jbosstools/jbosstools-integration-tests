package org.jboss.tools.portlet.ui.bot.test.template;

import org.jboss.tools.portlet.ui.bot.matcher.browser.BrowserUrlMatcher;
import org.jboss.tools.portlet.ui.bot.task.SWTTask;
import org.jboss.tools.portlet.ui.bot.task.server.RunninngProjectOnServerTask;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletGatein;
import org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.junit.Test;

/**
 * Creates a java portlet and checks the loaded URL in browser after Run as...
 * 
 * This test is ran only once - no need to run it specially for java, jsf and seam portlet. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public abstract class RunAsLoadsPortalURLTemplate extends SWTTaskBasedTestCase {

	private static final String PROJECT_NAME = CreateJavaPortletProject.PROJECT_NAME;
	
	public abstract String[] getExpectedURLs();
		
	@Test
	public void runAsLoadsPortalURL(){
		createProject();
		createJavaPortlet();
		doPerform(runOnServerTask());
		
		doAssertThatInWorkspace(getExpectedURLs(), new BrowserUrlMatcher());
	}

	private void createProject() {
		new CreateJavaPortletProject().createDynamicWebProject();
	}
	
	private void createJavaPortlet() {
		new CreateJavaPortletGatein().createPortlet();
	}
	
	private SWTTask runOnServerTask() {
		return new RunninngProjectOnServerTask(PROJECT_NAME);
	}
}
