package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.After;
import org.junit.Before;
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
	private ConsoleView consoleView = new ConsoleView();

	protected abstract String getServerName();

	@Test
	public void operateServer() {
		new WaitWhile(new JobIsRunning());
		new WaitUntil(new ServerHasState("Stopped"));
		startServer();
		restartServer();
		stopServer();
		deleteServer();
	}
	
	@Before
	public void setUp(){
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		for(Server server : serversView.getServers()){
			if(!server.getLabel().getName().equals(serversView.getServer(getServerName()).getLabel().getName())){
				System.out.println(server.getLabel().getName());
				System.out.println(serversView.getServer(getServerName()).getLabel().getName());
				server.delete();
			}
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	@After
	public void cleanServerAndConsoleView() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		for (Server server : serversView.getServers()) {
			server.delete(true);
		}
		consoleView.open();
		consoleView.clearConsole();
	}

	public void startServer() {
		serversView.getServer(getServerName()).start();
		final String state = "Started";
		new WaitUntil(new ServerHasState(state));
		
		assertNoException("Starting server");
		assertServerState("Starting server", state);
	}

	public void restartServer() {
		serversView.getServer(getServerName()).restart();
		final String state = "Started";
		new WaitUntil(new ServerHasState(state));

		assertNoException("Restarting server");
		assertServerState("Restarting server", state);

	}

	public void stopServer() {
		serversView.getServer(getServerName()).stop();
		final String state = "Stopped";
		new WaitUntil(new ServerHasState(state));

		assertNoException("Stopping server");
		assertServerState("Stopping server", state);
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
		} else {
			fail("Text from console could not be obtained.");
		}
	}

	protected void assertServerState(String message, String state) {
		// need to catch EclipseLayerException because:
		// serverView cannot find server with name XXX for the first time
		String textState;
		serversView.open();
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
	
	private class ServerHasState implements WaitCondition {

		private String state;

		private ServerHasState(String state) {
			this.state = state;
		}

		@Override
		public boolean test() {
			return serversView.getServer(getServerName()).getLabel().getState().getText().equals(state);
		}

		@Override
		public String description() {
			return "Server in server view is in given state.";
		}
	}
}
