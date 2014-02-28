package org.jboss.ide.eclipse.as.ui.bot.test.eap60;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFileTemplate;

@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.EAP, version="6.0")
public class HotDeployJSPFileEAP60Server extends HotDeployJSPFileTemplate {

}
