package org.jboss.tools.portlet.ui.bot.test.template;

import static org.jboss.tools.portlet.ui.bot.matcher.WorkspaceAssert.assertThatInWorkspace;

import org.jboss.tools.portlet.ui.bot.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.portlet.ui.bot.task.console.ConsoleClearingTask;
import org.jboss.tools.portlet.ui.bot.task.editor.CloseAllEditors;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.AbstractPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.Test;

/**
 * Creates a new portlet and checks if the project is hot deployed (undeployed and deployed). 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(clearWorkspace=false, clearProjects=false, server=@Server(version="5.0", operator=">", state=ServerState.Running))
public abstract class HotDeploymentGateinTemplate extends SWTTaskBasedTestCase {

	protected abstract String getProjectName();
	
	protected abstract AbstractPortletCreationTask createPortlet();
	
	@Test
	public void hotDeployment(){
		
		doPerform(new CloseAllEditors());
		
		if(needRestart()){
			servers.restartServer(configuredState.getServer().name);
		}
		
		doPerform(new ConsoleClearingTask());
		
		bot.sleep(TIME_1S);
		doPerform(createPortlet());
		bot.sleep(TIME_5S);
		
		assertThatInWorkspace("undeploy, ctxPath=/" + getProjectName(), new ConsoleOutputMatcher(TaskDuration.LONG));
		assertThatInWorkspace("deploy, ctxPath=/" + getProjectName(), new ConsoleOutputMatcher(TaskDuration.LONG));
	}
	
	protected boolean needRestart(){
		return false;
	}
}
