package org.jboss.tools.portlet.ui.bot.test.template;

import static org.jboss.tools.portlet.ui.bot.matcher.WorkspaceAssert.assertThatInWorkspace;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.portlet.ui.bot.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.portlet.ui.bot.task.editor.CloseAllEditors;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.Test;

/**
 * Creates a new portlet and checks if the project is hot deployed (undeployed and deployed). 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
@Require(clearWorkspace=false, clearProjects=false, server=@Server(version="5.0", operator=">", state=ServerState.Running))
public abstract class HotDeploymentGateinTemplate extends SWTTaskBasedTestCase {

	protected abstract String getProjectName();
	
	protected abstract void createPortlet();
	
	@Test
	public void hotDeployment(){
		
		doPerform(new CloseAllEditors());
		
		if(needRestart()){
			servers.restartServer(configuredState.getServer().name);
		}
		new WaitUntil(new ConsoleHasNoChange(), TimePeriod.LONG);
		
		ConsoleView console = new ConsoleView();
		console.clearConsole();
		
		bot.sleep(TIME_5S);
		createPortlet();
		bot.sleep(TIME_5S);
		
		console.open();
		if(console.getConsoleText() == null || console.getConsoleText().isEmpty()){
			throw new AssertionError("Hot-deployment of project '" + getProjectName() + "' was not registered in console output.");
		}
		assertThatInWorkspace("undeploy, ctxPath=/" + getProjectName(), new ConsoleOutputMatcher());
		assertThatInWorkspace("deploy, ctxPath=/" + getProjectName(), new ConsoleOutputMatcher());
	}
	
	protected boolean needRestart(){
		return false;
	}
}
