package org.jboss.tools.portlet.ui.bot.test.example;

import static org.hamcrest.Matchers.not;
import static org.jboss.tools.portlet.ui.bot.matcher.WorkspaceAssert.assertThatInWorkspace;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.exceptionInConsoleOutput;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.isNumberOfErrors;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.PortletMatchersFactory.canLoadAt4xRuntime;

import org.hamcrest.Matcher;
import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.SWTBotAware;
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
@Require(server=@Server(version="4.3", state=ServerState.Running, type=ServerType.EPP))
public abstract class AbstractPortletExampleTest extends ExampleTest {

	protected abstract PortletDefinition getPortletDefinition();
	
	@Override
	protected void executeExample() {
		doPerform(new ConsoleClearingTask());
		
		for (String project : getProjectNames()){
			doPerform(new RunninngProjectOnServerTask(project));			
		}
		
		doAssertThatInWorkspace(0, isNumberOfErrors());
		doAssertThatInWorkspace(not(exceptionInConsoleOutput()));
		doAssertThatInWorkspace(getPortletDefinition(), canLoadAt4xRuntime());
	}
	
	protected void doPerform(AbstractSWTTask task){
		task.setBot(bot);
		task.perform();
	}
	
	protected void doAssertThatInWorkspace(Matcher<Void> matcher){
		if (matcher instanceof SWTBotAware){
			((SWTBotAware) matcher).setBot(bot);			
		}
		assertThatInWorkspace(matcher);
	}
	
	protected <T> void doAssertThatInWorkspace(T actual, Matcher<T> matcher){
		if (matcher instanceof SWTBotAware){
			((SWTBotAware) matcher).setBot(bot);			
		}
		assertThatInWorkspace(actual, matcher);
	}
}
