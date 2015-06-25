package org.jboss.ide.eclipse.as.ui.bot.test.as70;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;

/**
 * 
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@JBossServer(state=ServerReqState.STOPPED, type=ServerReqType.AS7_0)
public class OperateAS70Server extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "Welcome to AS 7";
	}
	
	@Override
	protected boolean ignoreExceptionInConsole(){
		return true;
	}
	
	@Override
	public boolean setHeadlessModeOnMac() {
		return false;
	}
}
