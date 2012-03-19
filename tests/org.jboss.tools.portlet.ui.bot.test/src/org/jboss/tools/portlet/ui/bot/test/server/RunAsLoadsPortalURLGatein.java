package org.jboss.tools.portlet.ui.bot.test.server;

import org.jboss.tools.portlet.ui.bot.test.template.RunAsLoadsPortalURLTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;

@Require(server=@Server(version="5.0", operator=">", state=ServerState.Running))
public class RunAsLoadsPortalURLGatein extends RunAsLoadsPortalURLTemplate {

	@Override
	public String getExpectedURL() {
		return "http://localhost:8080/portal/classic/";
	}
}
