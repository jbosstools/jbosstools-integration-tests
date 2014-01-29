package org.jboss.ide.eclipse.as.ui.bot.test.as71;

import org.jboss.ide.eclipse.as.ui.bot.test.template.ServerStateDetectorsTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.JbossAS, version="7.1", state=ServerState.NotRunning))
public class ServerStateDetectorsAS71Server extends ServerStateDetectorsTemplate{
	
	@Override
	protected String getManagerServicePoller() {
		return "JBoss 7 Manager Service";
	}
}
