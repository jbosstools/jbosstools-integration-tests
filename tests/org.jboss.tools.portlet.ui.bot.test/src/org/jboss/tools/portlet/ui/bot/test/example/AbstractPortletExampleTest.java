package org.jboss.tools.portlet.ui.bot.test.example;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.inConsoleOutput;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.isNumberOfErrors;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.console.ConsoleClearingTask;
import org.jboss.tools.portlet.ui.bot.task.server.RunninngProjectOnServerTask;
import org.jboss.tools.ui.bot.ext.ExampleTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * Common ancestor for example projects tests. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(required=true, state=ServerState.Running, type=ServerType.EPP))
public abstract class AbstractPortletExampleTest extends ExampleTest {

	protected void doPerform(AbstractSWTTask task){
		task.setBot(bot);
		task.perform();
	}
	
	@Override
	protected void executeExample() {
		doPerform(new ConsoleClearingTask());
		
		for (String project : getProjectNames()){
			doPerform(new RunninngProjectOnServerTask(project));			
		}
		
		assertThat(0, isNumberOfErrors());
		assertThat("Exception:", not(inConsoleOutput()));
	}
}
