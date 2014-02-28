package org.jboss.ide.eclipse.as.ui.bot.test.as71;


import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.ServerStateDetectorsTemplate;

@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.AS, version="7.1")
public class ServerStateDetectorsAS71Server extends ServerStateDetectorsTemplate{
	
	@Override
	protected String getManagerServicePoller() {
		return "JBoss 7 Manager Service";
	}
}
