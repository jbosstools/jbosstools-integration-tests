package org.jboss.tools.runtime.as.ui.bot.test.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Checks if the given server can be started, restarted, stopped and deleted
 * without error.
 * 
 * @author Lucia Jelinkova
 * @author Radoslav Rabara
 */
public abstract class OperateServerTemplate {

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private ServersView serversView = new ServersView();
	private ConsoleView consoleView = new ConsoleView();

	protected abstract String getServerName();

	@Test
	public void operateServer() {
		serverIsPresentInServersView();
		new WaitWhile(new JobIsRunning());
		new WaitUntil(new ServerHasState("Stopped"));
		startServer();
		restartServer();
		stopServer();
		deleteServer();
	}
	
	private void serverIsPresentInServersView() {
		ServersView sw = new ServersView();
		sw.open();
		try {
			sw.getServer(getServerName());
		} catch(EclipseLayerException e) {
			String failMessage = "Server \"" + getServerName() + "\" not found in Servers View.";
			LOGGER.log(Level.SEVERE, failMessage, e);
			fail(failMessage);
		}
	}

	@Before
	public void setUp(){
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		serversView.open();
		for(Server server : serversView.getServers()){
			if(!server.getLabel().getName().equals(serversView.getServer(getServerName()).getLabel().getName())){
				server.delete();
			}
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	@After
	public void cleanServerAndConsoleView() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		serversView.open();
		for (Server server : serversView.getServers()) {
			server.delete(true);
		}
		consoleView.open();
		consoleView.clearConsole();
	}

	public void startServer() {
		serversView.getServer(getServerName()).start();
		final String state = "Started";
		new WaitUntil(new ConsoleHasNoChange(TimePeriod.getCustom(5)), TimePeriod.LONG);
		new WaitUntil(new ServerHasState(state));
		
		assertNoException("Starting server");
		assertServerState("Starting server", state);
	}

	public void restartServer() {
		serversView.getServer(getServerName()).restart();
		final String state = "Started";
		new WaitUntil(new ConsoleHasNoChange(TimePeriod.getCustom(5)), TimePeriod.LONG);
		new WaitUntil(new ServerHasState(state));

		assertNoException("Restarting server");
		assertNoError("Restarting server");
		assertServerState("Restarting server", state);

	}

	public void stopServer() {
		serversView.getServer(getServerName()).stop();
		final String state = "Stopped";
		new WaitUntil(new ConsoleHasNoChange(TimePeriod.getCustom(5)), TimePeriod.LONG);
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

	protected void assertNoError(String message) {
		ConsoleView console = new ConsoleView();

		console.open();
		String consoleText = console.getConsoleText();
		if (consoleText != null) {
			assertThat(message, consoleText, not(containsString("Error:")));
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

		private String expectedState;
		private String state;
		private ServerHasState(String expectedState) {
			this.expectedState = expectedState;
		}

		@Override
		public boolean test() {
			state = serversView.getServer(getServerName()).getLabel().getState().getText();
			return state.equals(state);
		}

		@Override
		public String description() {
			return "Server in server view is in given state."
					+ "Expected: \"" + expectedState + "\" but was \"" + state + "\"";
		}
	}
}
