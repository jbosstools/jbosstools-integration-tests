package org.jboss.tools.portlet.ui.bot.test.template;

import static org.jboss.tools.portlet.ui.bot.matcher.factory.PortletMatchersFactory.canLoadAt4xRuntime;

import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.Test;

/**
 * Tests that the portlet can be loaded in browser. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(clearWorkspace=false, clearProjects=false, server=@Server(version="4.3", state=ServerState.Present))
public abstract class LoadPortletInBrowser4xRuntimeTemplate extends SWTTaskBasedTestCase {

	protected abstract PortletDefinition getPortletDefinition();
	
	@Test
	public void testLoadPortlet(){
		doAssertThatInWorkspace(getPortletDefinition(), canLoadAt4xRuntime());
	}
}
