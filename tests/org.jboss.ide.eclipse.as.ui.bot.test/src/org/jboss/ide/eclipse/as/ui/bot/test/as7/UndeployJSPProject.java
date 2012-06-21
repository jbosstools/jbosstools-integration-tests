package org.jboss.ide.eclipse.as.ui.bot.test.as7;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

/**
 * Removes JSP project from server. Checks:
 * 
 * <ul>
 * 	<li>the console output</li>
 * 	<li>server's label</li>
 * 	<li>project is not listed under the server</li>
 * </ul>
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(type=ServerType.EAP, state=ServerState.Running), clearProjects=false, clearWorkspace=false)
public class UndeployJSPProject extends SWTTestExt {

	@Test
	public void undeployProject(){
		ServersView serversView = new ServersView();
		serversView.removeProjectFromServer(DeployJSPProject.PROJECT_NAME, configuredState.getServer().name);
		
		// console
		assertThat("Undeployed \"" + DeployJSPProject.PROJECT_NAME + ".war\"", new ConsoleOutputMatcher(TaskDuration.NORMAL));
		assertThat("Exception:", not(new ConsoleOutputMatcher()));
		// view
		assertFalse("Server contains project", serversView.containsProject(configuredState.getServer().name, DeployJSPProject.PROJECT_NAME));	
		assertEquals("Started", serversView.getServerStatus(configuredState.getServer().name));
		assertEquals("Synchronized", serversView.getServerPublishStatus(configuredState.getServer().name));
	}
}
