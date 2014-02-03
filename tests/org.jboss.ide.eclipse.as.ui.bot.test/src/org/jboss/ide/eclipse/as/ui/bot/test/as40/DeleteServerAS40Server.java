package org.jboss.ide.eclipse.as.ui.bot.test.as40;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServerTemplate;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;

@Server(state=ServerReqState.PRESENT, type=ServerReqType.AS, version="4.0")
public class DeleteServerAS40Server extends DeleteServerTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	@Override
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	} 
}
