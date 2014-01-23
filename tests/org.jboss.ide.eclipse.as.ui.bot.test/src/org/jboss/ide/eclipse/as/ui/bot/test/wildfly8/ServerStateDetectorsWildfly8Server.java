package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import org.jboss.ide.eclipse.as.ui.bot.test.template.ServerStateDetectorsTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;


@Require(server=@Server(type=ServerType.WildFly, version="8", state=ServerState.NotRunning))
public class ServerStateDetectorsWildfly8Server extends ServerStateDetectorsTemplate {

	@Override
	protected String getManagerServicePoller() {
		return "Wildfly Manager Service";
	}
}
