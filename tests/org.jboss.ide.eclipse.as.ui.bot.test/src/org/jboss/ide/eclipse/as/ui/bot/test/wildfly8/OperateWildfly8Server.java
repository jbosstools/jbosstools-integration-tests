package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;

@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.WILDFLY8x, version=ServerReqVersion.EQUAL)
public class OperateWildfly8Server extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "Welcome to WildFly 8";
	}
}
