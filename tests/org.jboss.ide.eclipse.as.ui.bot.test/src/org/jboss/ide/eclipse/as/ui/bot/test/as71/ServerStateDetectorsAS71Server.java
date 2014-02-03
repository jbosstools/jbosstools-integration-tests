package org.jboss.ide.eclipse.as.ui.bot.test.as71;


import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.ServerStateDetectorsTemplate;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;

@Server(state=ServerReqState.STOPPED, type=ServerReqType.AS, version="7.1")
public class ServerStateDetectorsAS71Server extends ServerStateDetectorsTemplate{
	
	@InjectRequirement
	protected ServerRequirement requirement;
	
	@Override
	protected String getManagerServicePoller() {
		return "JBoss 7 Manager Service";
	}
	
	@Override
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	}
}
