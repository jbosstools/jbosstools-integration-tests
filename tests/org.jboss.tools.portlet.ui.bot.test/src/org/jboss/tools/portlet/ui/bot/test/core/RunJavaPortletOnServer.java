package org.jboss.tools.portlet.ui.bot.test.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject.PROJECT_NAME;
import static org.hamcrest.Matchers.not;
import static org.jboss.tools.portlet.ui.bot.test.matcher.factory.DefaultMatchersFactory.inConsoleOutput;

import org.jboss.tools.portlet.ui.bot.task.console.ConsoleClearingTask;
import org.jboss.tools.portlet.ui.bot.task.server.RunninngProjectOnServerTask;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.Test;

/**
 * Performs Run on Server on the java portlet project and checks if there is no exception in the
 * console.  
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(clearWorkspace=false, clearProjects=false, server=@Server(required=true, state=ServerState.Running, type=ServerType.EPP))
public class RunJavaPortletOnServer extends SWTTaskBasedTestCase {

	@Test
	public void testRunOnServer(){
		doPerform(new ConsoleClearingTask());
		doPerform(new RunninngProjectOnServerTask(PROJECT_NAME));
		
		assertThat("Exception", not(inConsoleOutput()));
		System.out.println();
	}
}
