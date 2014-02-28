package org.jboss.ide.eclipse.as.ui.bot.test.as6;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;

/**
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.AS, version="6")
public class OperateAS6Server extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "Manage this JBoss AS Instance";
	}
}
