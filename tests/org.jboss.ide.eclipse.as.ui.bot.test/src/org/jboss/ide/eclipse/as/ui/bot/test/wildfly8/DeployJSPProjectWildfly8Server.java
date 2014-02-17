package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;

@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.WILDFLY8_0, version=ServerReqVersion.GREATER_OR_EQUAL)
public class DeployJSPProjectWildfly8Server extends DeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "Register web context: /" + PROJECT_NAME;
	}

}
