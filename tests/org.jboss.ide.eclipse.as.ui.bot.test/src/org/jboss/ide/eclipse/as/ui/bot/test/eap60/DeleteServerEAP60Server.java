package org.jboss.ide.eclipse.as.ui.bot.test.eap60;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServerTemplate;

@Server(state=ServerReqState.PRESENT, type=ServerReqType.EAP, version="6.0")
public class DeleteServerEAP60Server extends DeleteServerTemplate {

}
