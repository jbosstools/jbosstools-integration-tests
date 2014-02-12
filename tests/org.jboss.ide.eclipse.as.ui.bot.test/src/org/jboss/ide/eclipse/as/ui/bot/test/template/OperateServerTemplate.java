package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.ide.eclipse.as.ui.bot.test.condition.BrowserContainsTextCondition;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertFalse;


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
public abstract class OperateServerTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	public abstract String getWelcomePageText();

	@Test
	public void operateServer(){
		startServer();
		restartServer();
		stopServer();
	}

	public void startServer(){
		getServer().start();

		assertNoException("Starting server");
		assertServerState("Starting server", ServerState.STARTED);
		assertWebPageContains(getWelcomePageText());
	}

	public void restartServer(){
		getServer().restart();

		assertNoException("Restarting server");
		assertServerState("Restarting server", ServerState.STARTED);
		assertWebPageContains(getWelcomePageText());
	}

	public void stopServer(){
		getServer().stop();

		assertNoException("Stopping server");
		assertServerState("Stopping server", ServerState.STOPPED);
	}

	protected void assertNoException(String message) {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		assertFalse(consoleView.getConsoleText().contains("Exception"));
	}

	protected void assertServerState(String message, ServerState state) {
		assertThat(message, getServer().getLabel().getState(), is(state));
	}

	private void assertWebPageContains(String string) {
		getServer().openWebPage();
		new WaitUntil(new BrowserContainsTextCondition(string), TimePeriod.NORMAL);
	}
	
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	} 

	protected JBossServer getServer() {
		JBossServerView view = new JBossServerView();
		return view.getServer(getServerName());
	}
}
