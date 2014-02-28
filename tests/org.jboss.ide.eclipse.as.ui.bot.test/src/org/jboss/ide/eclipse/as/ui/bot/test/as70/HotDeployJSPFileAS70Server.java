package org.jboss.ide.eclipse.as.ui.bot.test.as70;


import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFileTemplate;

@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.AS, version="7.0")
public class HotDeployJSPFileAS70Server extends HotDeployJSPFileTemplate {

}
