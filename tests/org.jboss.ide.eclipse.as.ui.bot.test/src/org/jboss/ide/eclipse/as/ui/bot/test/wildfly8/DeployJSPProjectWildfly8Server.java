package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.EAP, version="6.1", state=ServerState.Running))
public class DeployJSPProjectWildfly8Server extends DeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "Register web context: /" + PROJECT_NAME;
	}

}
