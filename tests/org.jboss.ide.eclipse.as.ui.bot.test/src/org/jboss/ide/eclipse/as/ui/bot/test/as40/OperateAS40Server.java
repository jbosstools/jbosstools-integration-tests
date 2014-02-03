package org.jboss.ide.eclipse.as.ui.bot.test.as40;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;

/**
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@Server(state=ServerReqState.STOPPED, type=ServerReqType.AS, version="4.0")
public class OperateAS40Server extends OperateServerTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	@Override
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	} 
	
	@Override
	public String getWelcomePageText() {
		return "JBoss Management";
	}
}
