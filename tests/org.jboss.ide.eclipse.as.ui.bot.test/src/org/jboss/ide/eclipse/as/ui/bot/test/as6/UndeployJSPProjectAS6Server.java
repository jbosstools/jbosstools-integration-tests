package org.jboss.ide.eclipse.as.ui.bot.test.as6;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;
import org.jboss.ide.eclipse.as.ui.bot.test.template.UndeployJSPProjectTemplate;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;

/**
 * @see UndeployJSPProjectTemplate
 * @author Lucia Jelinkova
 *
 */
@Server(state=ServerReqState.RUNNING, type=ServerReqType.AS, version="6")
public class UndeployJSPProjectAS6Server extends UndeployJSPProjectTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	@Override
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	} 
	
	@Override
	protected String getConsoleMessage() {
		return "undeploy, ctxPath=/" + DeployJSPProjectTemplate.PROJECT_NAME;
	}
}
