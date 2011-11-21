package org.jboss.tools.portlet.ui.bot.test.create;

import org.jboss.tools.portlet.ui.bot.test.task.SWTTask;
import org.jboss.tools.portlet.ui.bot.test.task.wizard.web.jboss.PortletCreationTask;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.junit.Test;

/**
 * Creates a new java portlet. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJavaPortlet extends SWTTaskBasedTestCase {

	private static final String CLASS_NAME = "UITestingPortlet";
	
	private static final String PACKAGE_NAME = "org.jboss.tools.tests.ui.portlet";
	
	@Test
	public void testCreate(){
		doPerform(getCreatePortletTask());
		System.out.println("");
	}

	private SWTTask getCreatePortletTask() {
		PortletCreationTask task = new PortletCreationTask();
		task.setProject(CreateJavaPortletProject.PROJECT_NAME);
		task.setPackageName(PACKAGE_NAME);
		task.setClassName(CLASS_NAME);
		return task;
	}
}
