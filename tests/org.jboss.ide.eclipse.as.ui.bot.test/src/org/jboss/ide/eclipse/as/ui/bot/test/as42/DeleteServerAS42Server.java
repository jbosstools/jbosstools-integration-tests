package org.jboss.ide.eclipse.as.ui.bot.test.as42;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServerTemplate;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS, version="4.2")
public class DeleteServerAS42Server extends DeleteServerTemplate {

}
