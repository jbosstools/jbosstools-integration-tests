package org.jboss.tools.arquillian.ui.bot.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;

//@CleanWorkspace
@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.EAP6_1plus, version=ServerReqVersion.EQUAL)

public class ArqTestServer  {
	
	@Test
	public void testIt() {	
		
		ServersView view = new ServersView();
		view.open();
		
		assertTrue ("The server did not start", 
				view.getServers().get(0).getLabel().getState().equals(ServerState.STARTED));
		
	} /* method */

} /* class */
