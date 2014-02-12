package org.jboss.ide.eclipse.as.ui.bot.test.as51;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;

/**
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@Server(state=ServerReqState.STOPPED, type=ServerReqType.AS, version="5.1")
public class OperateAS51Server extends OperateServerTemplate {
	
	@Override
	public String getWelcomePageText() {
		return "Manage this JBoss AS Instance";
	}
}
