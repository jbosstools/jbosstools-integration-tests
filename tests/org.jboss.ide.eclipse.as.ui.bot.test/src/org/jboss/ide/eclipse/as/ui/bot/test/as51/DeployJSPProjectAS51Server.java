package org.jboss.ide.eclipse.as.ui.bot.test.as51;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;

/**
 * @see DeployJSPProjectTemplate
 * @author Lucia Jelinkova
 *
 */
@Server(state=ServerReqState.RUNNING, type=ServerReqType.AS, version="5.1")
public class DeployJSPProjectAS51Server extends DeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "deploy, ctxPath=/" + PROJECT_NAME;
	}
}
