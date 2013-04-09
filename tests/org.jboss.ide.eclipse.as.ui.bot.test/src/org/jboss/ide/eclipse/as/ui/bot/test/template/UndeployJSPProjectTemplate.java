package org.jboss.ide.eclipse.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
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
public abstract class UndeployJSPProjectTemplate extends SWTTestExt {

	protected abstract String getConsoleMessage();
	@Test
	public void undeployProject(){
		ServersView serversView = new ServersView();
		serversView.removeProjectFromServer(DeployJSPProjectTemplate.PROJECT_NAME, configuredState.getServer().name);
		
		// console
		assertThat(getConsoleMessage(), new ConsoleOutputMatcher(TaskDuration.NORMAL));
		assertThat("Exception:", not(new ConsoleOutputMatcher()));
		// view
		assertFalse("Server contains project", serversView.containsProject(configuredState.getServer().name, DeployJSPProjectTemplate.PROJECT_NAME));	
		assertEquals("Started", serversView.getServerStatus(configuredState.getServer().name));
		assertEquals("Synchronized", serversView.getServerPublishStatus(configuredState.getServer().name));
	}
}
