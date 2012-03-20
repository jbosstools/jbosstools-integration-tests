package org.jboss.tools.portlet.ui.bot.test.server;

import org.jboss.tools.portlet.ui.bot.test.template.RunAsLoadsPortalURLTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;

@Require(server=@Server(version="4.3", state=ServerState.Running))
public class RunAsLoadsPortalURLJBPortal extends RunAsLoadsPortalURLTemplate {

	@Override
	public String[] getExpectedURLs() {
		return new String[]{"http://localhost:8080/portal"};
	}
}
