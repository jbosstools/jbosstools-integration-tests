package org.jboss.ide.eclipse.as.ui.bot.test.wildfly10;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;

@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.WILDFLY10x, version=ServerReqVersion.EQUAL)
public class DeployJSPProjectWildfly10Server extends DeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "Registered web context: /" + PROJECT_NAME;
	}

}
