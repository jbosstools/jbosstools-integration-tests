package org.jboss.ide.eclipse.as.ui.bot.test.eap7;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;

@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.EAP7x, version=ServerReqVersion.EQUAL)
public class OperateEAP7Server extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "Welcome to JBoss EAP 6";
	}

	@Override
	public boolean setHeadlessModeOnMac() {
		return false;
	}
}
