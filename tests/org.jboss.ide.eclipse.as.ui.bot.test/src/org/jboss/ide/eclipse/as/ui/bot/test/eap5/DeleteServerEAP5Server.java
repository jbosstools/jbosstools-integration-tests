package org.jboss.ide.eclipse.as.ui.bot.test.eap5;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServerTemplate;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.EAP5x)
public class DeleteServerEAP5Server extends DeleteServerTemplate {

}
