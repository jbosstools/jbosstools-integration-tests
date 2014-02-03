package org.jboss.ide.eclipse.as.ui.bot.test.as50;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFileTemplate;

@Server(state=ServerReqState.RUNNING, type=ServerReqType.AS, version="5.0")
public class HotDeployJSPFileAS50Server extends HotDeployJSPFileTemplate {

}
