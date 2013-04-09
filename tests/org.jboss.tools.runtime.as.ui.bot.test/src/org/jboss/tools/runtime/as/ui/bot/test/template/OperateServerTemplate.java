package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.After;
import org.junit.Test;

/**
 * Checks if the given server can be started, restarted, stopped and deleted
 * without error.
 * 
 * @author Lucia Jelinkova
 * 
 */
public abstract class OperateServerTemplate {

	private ServersView serversView = new ServersView();

	protected abstract String getServerName();

	@Test
	public void operateServer() {
		startServer();
		restartServer();
		stopServer();
		deleteServer();
	}

	@After
	public void cleanServerView() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		for (Server server : serversView.getServers()) {
			server.delete(true);
		}
	}

	public void startServer() {
		serversView.getServer(getServerName()).start();

		assertNoException("Starting server");
		assertServerState("Starting server", "Started");
	}

	public void restartServer() {
		serversView.getServer(getServerName()).restart();

		assertNoException("Restarting server");
		assertServerState("Restarting server", "Started");

	}

	public void stopServer() {
		serversView.getServer(getServerName()).stop();

		assertNoException("Stopping server");
		assertServerState("Stopping server", "Stopped");
	}

	public void deleteServer() {
		serversView.getServer(getServerName()).delete();
	}

	protected void assertNoException(String message) {
		ConsoleView console = new ConsoleView();
		
		console.open();
		String consoleText = console.getConsoleText();
		if (consoleText != null) {
			assertThat(message, consoleText, not(containsString("Exception:")));
		}else{
			fail("Text from console could not be obtained.");
		}
	}

	protected void assertServerState(String message, String state) {
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		// need to catch EclipseLayerException because:
		// serverView cannot find server with name XXX for the first time
		String textState;
		try {
			textState = serversView.getServer(getServerName()).getLabel()
					.getState().getText();
		} catch (EclipseLayerException ex) {
			new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
			textState = serversView.getServer(getServerName()).getLabel()
					.getState().getText();
		}

		assertThat(message, textState, is(state));
	}
}
