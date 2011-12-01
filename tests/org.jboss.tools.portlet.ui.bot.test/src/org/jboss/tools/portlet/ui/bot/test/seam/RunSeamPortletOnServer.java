package org.jboss.tools.portlet.ui.bot.test.seam;

import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.file;
import static org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.server.MarkFileAsDeployableTask;
import org.jboss.tools.portlet.ui.bot.test.template.RunPortletOnServerTemplate;

/**
 * Performs Run on Server on the jsf portlet project and checks if there is no exception in the
 * console.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class RunSeamPortletOnServer extends RunPortletOnServerTemplate {

	private static final WorkspaceFile DATASOURCE_FILE = file(PROJECT_NAME, "resources/" + PROJECT_NAME + "-ds.xml");
	
	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	public void testRunOnServer() {
		doPerform(new MarkFileAsDeployableTask(DATASOURCE_FILE));
		super.testRunOnServer();
	}
}
