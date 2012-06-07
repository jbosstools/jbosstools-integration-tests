package org.jboss.tools.portlet.ui.bot.test.example;

import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * Common ancestor for example projects tests for 5x runtime. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(version="5.0", operator=">", state=ServerState.Running, type=ServerType.EPP))
public abstract class AbstractPortletExampleGatein extends
		AbstractPortletExampleTest {

	@Override
	public String getExampleCategory() {
		return "Portlet for JBoss Enterprise Portal Platform 5.x/GateIn 3.1";
	}
	
	@Override
	protected boolean failOnMissingExample() {
		return true;
	}
}
