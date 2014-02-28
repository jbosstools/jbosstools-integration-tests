package org.jboss.ide.eclipse.as.ui.bot.test.as70;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;

/**
 * @see DeployJSPProjectTemplate
 * @author Lucia Jelinkova
 *
 */
@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.AS, version="7.0")
public class DeployJSPProjectAS70Server extends DeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "registering web context: /" + PROJECT_NAME;
	}
}
