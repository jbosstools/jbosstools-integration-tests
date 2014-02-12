package org.jboss.ide.eclipse.as.ui.bot.test.eap5;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServerTemplate;

@Server(state=ServerReqState.PRESENT, type=ServerReqType.EAP, version="5")
public class DeleteServerEAP5Server extends DeleteServerTemplate {

}
