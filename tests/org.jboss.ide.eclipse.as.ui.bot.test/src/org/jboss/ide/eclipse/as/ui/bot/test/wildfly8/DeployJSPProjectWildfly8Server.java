package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqOperator;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;

@Server(state=ServerReqState.RUNNING, type=ServerReqType.WILDFLY, version="8.0", operator=ServerReqOperator.GREATER_OR_EQUAL)
public class DeployJSPProjectWildfly8Server extends DeployJSPProjectTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	@Override
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	} 
	
	@Override
	protected String getConsoleMessage() {
		return "Register web context: /" + PROJECT_NAME;
	}

}
