package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.IsNot.not;

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
		serversView.removeProjectFromServer(DeployJSPProjectTemplate.PROJECT_NAME, getServerName());
		
		// console
		assertThat(getConsoleMessage(), new ConsoleOutputMatcher(TaskDuration.NORMAL));
		assertThat("Exception:", not(new ConsoleOutputMatcher()));
		// view
		assertFalse("Server contains project", serversView.containsProject(getServerName(), DeployJSPProjectTemplate.PROJECT_NAME));	
		assertEquals("Started", serversView.getServerStatus(getServerName()));
		assertEquals("Synchronized", serversView.getServerPublishStatus(getServerName()));
	}
	
	protected String getServerName(){
		return configuredState.getServer().name;
	}
}
