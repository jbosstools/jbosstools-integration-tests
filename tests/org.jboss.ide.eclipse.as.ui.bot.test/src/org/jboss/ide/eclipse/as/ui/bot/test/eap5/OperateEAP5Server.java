package org.jboss.ide.eclipse.as.ui.bot.test.eap5;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;

/**
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.EAP5x)
public class OperateEAP5Server extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "Manage this JBoss EAP Instance";
	}
	
	@Override
	protected boolean ignoreExceptionInConsole(){
		return true;
	}
}
