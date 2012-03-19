package org.jboss.tools.portlet.ui.bot.test.example;

import static org.jboss.tools.portlet.ui.bot.matcher.factory.PortletMatchersFactory.canLoadAt4xRuntime;

import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * Common ancestor for example projects tests for 4x runtime. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(version="4.3", state=ServerState.Running, type=ServerType.EPP))
public abstract class AbstractPortletExampleJBPortal extends
		AbstractPortletExampleTest {

	protected abstract PortletDefinition getPortletDefinition();
	
	@Override
	protected void executeExample() {
		super.executeExample();
		doAssertThatInWorkspace(getPortletDefinition(), canLoadAt4xRuntime());
	}
	
	@Override
	public String getExampleCategory() {
		return "Portlet";
	}
}
