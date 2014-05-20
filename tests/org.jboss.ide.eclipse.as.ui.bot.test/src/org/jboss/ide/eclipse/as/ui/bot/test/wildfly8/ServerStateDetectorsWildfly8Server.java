package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.ServerStateDetectorsTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;


@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.WILDFLY8x, version=ServerReqVersion.GREATER_OR_EQUAL)
public class ServerStateDetectorsWildfly8Server extends ServerStateDetectorsTemplate {

	@Override
	protected String getManagerServicePoller() {
		return "Wildfly Manager Service";
	}
}
