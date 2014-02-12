package org.jboss.ide.eclipse.as.ui.bot.test.as71;


import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;

/**
 * @see DeployJSPProjectTemplate
 * @author Lucia Jelinkova
 *
 */
@Server(state=ServerReqState.RUNNING, type=ServerReqType.AS, version="7.1")
public class DeployJSPProjectAS71Server extends DeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "Registering web context: /" + PROJECT_NAME;
	}
}
