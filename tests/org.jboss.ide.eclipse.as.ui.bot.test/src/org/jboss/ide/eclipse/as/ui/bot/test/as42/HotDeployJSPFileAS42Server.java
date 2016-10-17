package org.jboss.ide.eclipse.as.ui.bot.test.as42;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFileTemplate;
import org.jboss.reddeer.requirements.server.ServerReqState;

@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.AS4_2,cleanup=false)
public class HotDeployJSPFileAS42Server extends HotDeployJSPFileTemplate {

}
