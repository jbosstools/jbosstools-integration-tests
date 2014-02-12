package org.jboss.ide.eclipse.as.ui.bot.test.eap4;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;

/**
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@Server(state=ServerReqState.STOPPED, type=ServerReqType.EAP, version="4.3")
public class OperateEAP4Server extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "JBoss Management";
	}
}
