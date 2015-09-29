package org.jboss.ide.eclipse.as.ui.bot.test.eap6x;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;

@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.EAP6_1plus, version=ServerReqVersion.EQUAL)
public class DeployJSPProjectEAP6xServer extends DeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "Register web context: /" + PROJECT_NAME;
	}

}
