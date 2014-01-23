package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.ui.bot.test.editor.ServerEditor;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

/**
 * Checks the server startup / shutdown with different startup/shutdown pollers.  
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class ServerStateDetectorsTemplate extends SWTTestExt{
	
	public static final String TIMEOUT_POLLER = "Timeout";
	
	public static final String WEB_PORT_POLLER = "Web Port";
	
	public static final String PROCESS_TERMINATED_POLLER = "Process Terminated";
	
	private ServersView serversView;
	
	@Before
	public void setup(){
		serversView = new ServersView();
	}
	
	protected abstract String getManagerServicePoller();
	
	protected String getServerName(){
		return configuredState.getServer().name;
	}
	
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
		assertFalse(message, new ConsoleHasText("Exception:").test());
	}

	protected void assertServerState(String message, ServerState state) {
		assertThat(message, serversView.getServer(getServerName()).getLabel().getState(), is(state));
	}

	protected void setTimeouts(int timeout){
		ServerEditor editor = new ServerEditor(getServerName());
		editor.open();
		
		editor.setStartTimeout(20);
		editor.setStopTimeout(20);
		editor.save();
	}
	
	protected void setPollers(String startupPoller, String shutdownPoller){
		ServerEditor editor = new ServerEditor(getServerName());
		editor.open();
		
		editor.setStartupPoller(startupPoller);
		editor.setShutdownPoller(shutdownPoller);
		editor.save();
	}
}
