package org.jboss.ide.eclipse.as.ui.bot.test.as71;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
/**
 * 
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@Server(state=ServerReqState.STOPPED, type=ServerReqType.AS, version="7.1")
public class OperateAS71Server extends OperateServerTemplate {

	@InjectRequirement
	private ServerRequirement requirement;
	
	@Override
	public String getWelcomePageText() {
		return "Welcome to AS 7";
	}
	
	@Override
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	}
}
