package org.jboss.ide.eclipse.as.ui.bot.test.eap6x;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.ServerStateDetectorsTemplate;

@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.EAP6_1plus, version=ServerReqVersion.GREATER_OR_EQUAL)
public class ServerStateDetectorsEAP6xServer extends ServerStateDetectorsTemplate {

	@Override
	protected String getManagerServicePoller() {
		return "JBoss 7 Manager Service";
	}
}
