package org.jboss.ide.eclipse.as.ui.bot.test.as7;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

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
		
		assertNoException("Starting server");
		assertServerState("Starting server", "Started");
		
	}

	public void restartServer(){
		serversView.restartServer(getServerName());

		assertNoException("Restarting server");
		assertServerState("Restarting server", "Started");
	}

	public void stopServer(){
		serversView.stopServer(getServerName());

		assertNoException("Stopping server");
		assertServerState("Stopping server", "Stopped");
	}

	protected void assertNoException(String message) {
		assertThat(message, "Exception:", not(new ConsoleOutputMatcher()));
	}

	protected void assertServerState(String message, String state) {
		assertThat(message, serversView.getServerStatus(getServerName()), is(state));
	}
}
