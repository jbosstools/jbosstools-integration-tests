package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerEditor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.ide.eclipse.as.ui.bot.test.matcher.ConsoleContainsTextMatcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.exception.RedDeerException;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

/**
 * Checks the server startup / shutdown with different startup/shutdown pollers.  
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class ServerStateDetectorsTemplate {
	
	public static final String TIMEOUT_POLLER = "Timeout";
	
	public static final String WEB_PORT_POLLER = "Web Port";
	
	public static final String PROCESS_TERMINATED_POLLER = "Process Terminated";
	
	@InjectRequirement
	protected ServerRequirement requirement;
	
	private ServersView serversView;
	
	@Before
	public void setup(){
		serversView = new ServersView();
		serversView.open();
	}
	
	@After
	public void cleanup(){
		try {
			stopServer();
		} catch (RedDeerException e){
			// do nothing
		}
	}
	
	protected abstract String getManagerServicePoller();
	
	@Test
	public void timeoutPollers(){
		setTimeouts(20);
		setPollers(TIMEOUT_POLLER, TIMEOUT_POLLER);
		operateServer();
	}
	
	@Test
	public void webPortPollers(){
		setPollers(WEB_PORT_POLLER, WEB_PORT_POLLER);
		operateServer();
	}
	
	@Test
	public void managementServicePollers(){
		setPollers(getManagerServicePoller(), getManagerServicePoller());
		operateServer();
	}
	
	@Test
	public void managementService_ProcessTerminatedPollers(){
		setPollers(getManagerServicePoller(), PROCESS_TERMINATED_POLLER);
		operateServer();
	}
	
	protected void operateServer(){
		startServer();
		restartServer();
		stopServer();
	}
	
	protected void startServer(){
		serversView.getServer(getServerName()).start();
		
		assertNoException("Starting server");
		assertServerState("Starting server", ServerState.STARTED);
	}

	protected void restartServer(){
		serversView.getServer(getServerName()).restart();;
		
		assertNoException("Restarting server");
		assertServerState("Restarting server", ServerState.STARTED);
	}

	protected void stopServer(){
		serversView.getServer(getServerName()).stop();;
		
		assertNoException("Stopping server");
		assertServerState("Stopping server", ServerState.STOPPED);
	}

	protected void assertNoException(String message) {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		consoleView.toggleShowConsoleOnStandardOutChange(false);
		
		new WaitUntil(new ConsoleHasNoChange(), TimePeriod.LONG);
		assertThat(message, consoleView, not(new ConsoleContainsTextMatcher("Exception")));

		consoleView.close();
	}

	protected void assertServerState(String message, ServerState state) {
		assertThat(message, serversView.getServer(getServerName()).getLabel().getState(), is(state));
	}

	protected void setTimeouts(int timeout){
		JBossServerView view = new JBossServerView();
		view.open();
		
		JBossServerEditor editor = view.getServer(getServerName()).open();
		
		editor.setStartTimeout(20);
		editor.setStopTimeout(20);
		editor.save();
	}
	
	protected void setPollers(String startupPoller, String shutdownPoller){
		JBossServerView view = new JBossServerView();
		view.open();
		
		JBossServerEditor editor = view.getServer(getServerName()).open();
		
		editor.setStartupPoller(startupPoller);
		editor.setShutdownPoller(shutdownPoller);
		editor.save();
	}
	
	protected String getServerName() {
		return requirement.getServerNameLabelText(requirement.getConfig());
	}
}
