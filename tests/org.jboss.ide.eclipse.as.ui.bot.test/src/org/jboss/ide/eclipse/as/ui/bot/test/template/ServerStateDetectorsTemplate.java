package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerEditor;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.swt.exception.RedDeerException;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

/**
 * Checks the server startup / shutdown with different startup/shutdown pollers.  
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class ServerStateDetectorsTemplate extends AbstractJBossServerTemplate {
	
	public static final String TIMEOUT_POLLER = "Timeout";
	
	public static final String WEB_PORT_POLLER = "Web Port";
	
	public static final String PROCESS_TERMINATED_POLLER = "Process Terminated";
	
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
		getServer().start();
		
		assertNoException("Starting server");
		assertServerState("Starting server", ServerState.STARTED);
	}

	protected void restartServer(){
		getServer().restart();;
		
		assertNoException("Restarting server");
		assertServerState("Restarting server", ServerState.STARTED);
	}

	protected void stopServer(){
		getServer().stop();;
		
		assertNoException("Stopping server");
		assertServerState("Stopping server", ServerState.STOPPED);
	}

	protected void assertServerState(String message, ServerState state) {
		assertThat(message, getServer().getLabel().getState(), is(state));
	}

	protected void setTimeouts(int timeout){
		JBossServerEditor editor = getServer().open();
		
		editor.setStartTimeout(20);
		editor.setStopTimeout(20);
		editor.save();
	}
	
	protected void setPollers(String startupPoller, String shutdownPoller){
		JBossServerEditor editor = getServer().open();
		
		editor.setStartupPoller(startupPoller);
		editor.setShutdownPoller(shutdownPoller);
		editor.save();
	}
}
