package org.jboss.ide.eclipse.as.ui.bot.test.eap4;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFileTemplate;

@Server(state=ServerReqState.RUNNING, type=ServerReqType.EAP, version="4.3")
public class HotDeployJSPFileEAP4Server extends HotDeployJSPFileTemplate {

}
