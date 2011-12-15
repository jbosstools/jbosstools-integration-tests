package org.jboss.tools.portlet.ui.bot.test.seam;

import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.file;
import static org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.server.RunningFileOnServerTask;
import org.jboss.tools.portlet.ui.bot.test.template.RunPortletOnServerTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;

/**
 * Performs Run on Server on the jsf portlet project and checks if there is no exception in the
 * console.  
 * <br /><br />
 * NOTE: This test is not permormed on 4.x runtime because of a bunch of issues with Portlet Bridge and Seam 2.0, e. g:
 * <ul>
 * 	<li>JBIDE-8289 </li>
 * 	<li>PBR-186 </li>
 * 	<li>JBIDE-9481 </li>
 * </ul>
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(clearWorkspace=false, clearProjects=false, server=@Server(version="5.0", operator=">", state=ServerState.Present))
public class RunSeamPortletOnServer extends RunPortletOnServerTemplate {

	private static final WorkspaceFile DATASOURCE_FILE = file(PROJECT_NAME, "resources/" + PROJECT_NAME + "-ds.xml");
	
	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	public void testRunOnServer() {
		doPerform(new RunningFileOnServerTask(DATASOURCE_FILE));
		super.testRunOnServer();
	}
}
