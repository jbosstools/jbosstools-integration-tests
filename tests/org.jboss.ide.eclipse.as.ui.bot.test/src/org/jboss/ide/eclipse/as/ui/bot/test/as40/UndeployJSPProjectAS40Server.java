package org.jboss.ide.eclipse.as.ui.bot.test.as40;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;
import org.jboss.ide.eclipse.as.ui.bot.test.template.UndeployJSPProjectTemplate;

/**
 * @see UndeployJSPProjectTemplate
 * @author Lucia Jelinkova
 *
 */
@Server(state=ServerReqState.RUNNING, type=ServerReqType.AS, version="4.0")
public class UndeployJSPProjectAS40Server extends UndeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "undeploy, ctxPath=/" + DeployJSPProjectTemplate.PROJECT_NAME;
	}
}
