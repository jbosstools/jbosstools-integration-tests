package org.jboss.ide.eclipse.as.ui.bot.test.eap6x;

import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.EAP, version="6.1", state=ServerState.NotRunning))
public class OperateEAP6xServer extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "Welcome to JBoss EAP 6";
	}

}
