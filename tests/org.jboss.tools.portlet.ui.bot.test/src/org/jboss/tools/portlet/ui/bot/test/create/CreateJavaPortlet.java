package org.jboss.tools.portlet.ui.bot.test.create;

import static org.jboss.tools.portlet.ui.bot.test.create.CreateJavaPortletProject.PROJECT_NAME;
import static org.jboss.tools.portlet.ui.bot.test.matcher.problems.ProblemViewMatchersFactory.isNumberOfErrors;
import static org.jboss.tools.portlet.ui.bot.test.matcher.workspace.WorkspaceMatchersFactory.existsInProject;

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
	
	private static final String SOURCE_FILE_NAME = "src";
	
	@Test
	public void testCreate(){
		doPerform(getCreatePortletTask());
		
		doAssertThat(0, isNumberOfErrors());
		doAssertThat(SOURCE_FILE_NAME + "/" + PACKAGE_NAME + "/" + CLASS_NAME + ".java", existsInProject(PROJECT_NAME));
		doAssertThat("WebContent/WEB-INF/default-object.xml", existsInProject(PROJECT_NAME));
		doAssertThat("WebContent/WEB-INF/portlet-instances.xml", existsInProject(PROJECT_NAME));
		System.out.println("");
	}

	private SWTTask getCreatePortletTask() {
		PortletCreationTask task = new PortletCreationTask();
		task.setProject(PROJECT_NAME);
		task.setPackageName(PACKAGE_NAME);
		task.setClassName(CLASS_NAME);
		return task;
	}
}
