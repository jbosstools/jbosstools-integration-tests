package org.jboss.tools.portlet.ui.bot.test.template;

import static org.hamcrest.core.IsNot.not;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.exceptionInConsoleOutput;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.portlet.ui.bot.task.server.RunninngProjectOnServerTask;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.Test;

/**
 * Performs Run on Server on the specified portlet project and checks if there is no exception in the
 * console.  
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(clearWorkspace=false, clearProjects=false, server=@Server(state=ServerState.Running))
public abstract class RunPortletOnServerTemplate extends SWTTaskBasedTestCase {

	protected abstract String getProjectName(); 
	
	@Test
	public void testRunOnServer(){
		new ConsoleView().clearConsole();
		doPerform(new RunninngProjectOnServerTask(getProjectName()));
		
		doAssertThatInWorkspace(not(exceptionInConsoleOutput()));
	}
}
