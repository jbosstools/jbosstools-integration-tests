package org.jboss.ide.eclipse.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.jboss.ide.eclipse.as.ui.bot.test.web.PageSourceMatcher;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

/**
 * Starts, restarts and stops the server and checks:
 * <ul>
 * 	<li>the console output</li>
 * 	<li>server's label</li>
 * 	<li>welcome page is available (if the result state is started)</li>
 * </ul>
 * @author Lucia Jelinkova
 *
 */
public abstract class OperateServerTemplate extends SWTTestExt {

	protected ServersView serversView = new ServersView();

	public abstract String getWelcomePageText();
	
	protected String getServerName(){
		return configuredState.getServer().name;
	}

	@Test
	public void operateServer(){
		startServer();
		restartServer();
		stopServer();
	}
	
	public void startServer(){
		serversView.startServer(getServerName());
		serversView.openWebPage(configuredState.getServer().name);
		
		assertNoException("Starting server");
		assertServerState("Starting server", "Started");
		assertWebPageContains(getWelcomePageText());
	}

	public void restartServer(){
		serversView.restartServer(getServerName());
		serversView.openWebPage(configuredState.getServer().name);
		
		assertNoException("Restarting server");
		assertServerState("Restarting server", "Started");
		assertWebPageContains(getWelcomePageText());
	}

	public void stopServer(){
		serversView.stopServer(getServerName());
		serversView.openWebPage(configuredState.getServer().name);
		
		assertNoException("Stopping server");
		assertServerState("Stopping server", "Stopped");
	}

	protected void assertNoException(String message) {
		assertThat(message, "Exception:", not(new ConsoleOutputMatcher()));
	}

	protected void assertServerState(String message, String state) {
		assertThat(message, serversView.getServerStatus(getServerName()), is(state));
	}
	
	private void assertWebPageContains(String string) {
		serversView.openWebPage(configuredState.getServer().name);
		assertThat(string, new PageSourceMatcher(TaskDuration.NORMAL));
	}
}
