package org.jboss.ide.eclipse.as.ui.bot.test.as6;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;

/**
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.AS6x)
public class OperateAS6Server extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "Manage this JBoss AS Instance";
	}
	
	@Override
	protected boolean ignoreExceptionInConsole(){
		return true;
	}
	
	@Override
	public boolean setHeadlessModeOnMac() {
		return true;
	}
}
