package org.jboss.tools.teiid.reddeer.condition;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * Condition that specifies if a server has state either 'Stopped' or 'Started'
 * 
 * @author apodhrad
 * 
 */
public class ServerHasState implements WaitCondition {

	protected Logger log = Logger.getLogger(this.getClass());

	private String serverName;
	private ServerState state;

	public ServerHasState(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public boolean test() {
		Server server = new ServersView().getServer(serverName);
		state = server.getLabel().getState();
		System.out.println("Server's state: " + state);
		return state.equals(ServerState.STARTED) || state.equals(ServerState.STOPPED);
	}

	@Override
	public String description() {
		return "Server has state '" + state + "'";
	}

}
