package org.jboss.ide.eclipse.as.ui.bot.test.as7;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;
@Require(server=@Server(type=ServerType.EAP, state=ServerState.NotRunning))
public class OperateAS7Server extends SWTTestExt {

	private ServersView serversView = new ServersView();

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
		assertWebPageContains("Welcome to EAP 6");
	}

	public void restartServer(){
		serversView.restartServer(getServerName());
		serversView.openWebPage(configuredState.getServer().name);
		
		assertNoException("Restarting server");
		assertServerState("Restarting server", "Started");
		assertWebPageContains("Welcome to EAP 6");
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
	}
}
