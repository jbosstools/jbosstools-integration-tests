package org.jboss.ide.eclipse.as.ui.bot.test.eap6x;

import org.jboss.ide.eclipse.as.ui.bot.test.template.ServerStateDetectorsTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;


@Require(server=@Server(type=ServerType.EAP, version="6.1", state=ServerState.NotRunning))
public class ServerStateDetectorsEAP6xServer extends ServerStateDetectorsTemplate {

	@Override
	protected String getManagerServicePoller() {
		return "JBoss 7 Manager Service";
	}
}
