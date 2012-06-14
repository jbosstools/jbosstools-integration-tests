package org.jboss.ide.eclipse.as.ui.bot.test.as7;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.jboss.ide.eclipse.as.ui.bot.test.Activator;
import org.jboss.ide.eclipse.as.ui.bot.test.web.PageSourceMatcher;
import org.jboss.ide.eclipse.as.ui.bot.test.wizard.ImportProjectWizard;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Before;
import org.junit.Test;

@Require(server=@Server(type=ServerType.EAP, state=ServerState.Running))
public class DeployJSPProject extends SWTTestExt {

	private static final String PROJECT_NAME = "jsp-as7";
	
	@Before
	public void importProject(){
		ImportProjectWizard wizard = new ImportProjectWizard();
		wizard.setCopyProjectsIntoWorkspace(true);
		wizard.setProjectPath(SWTUtilExt.getPathToFileWithinPlugin(Activator.PLUGIN_ID, "projects"));
		wizard.setProjectNames(PROJECT_NAME);
		wizard.execute();
	}
	
	@Test
	public void deployJSPProject(){
		ServersView serversView = new ServersView();
		serversView.addProjectToServer(PROJECT_NAME, configuredState.getServer().name);
		
		// console
		assertThat("Exception:", not(new ConsoleOutputMatcher(TaskDuration.NORMAL)));
		assertThat("Registering web context: /" + PROJECT_NAME, new ConsoleOutputMatcher(TaskDuration.NORMAL));
		// view
		assertTrue("Server contains project", serversView.containsProject(configuredState.getServer().name, PROJECT_NAME));
		assertEquals("Started", serversView.getServerStatus(configuredState.getServer().name));
		assertEquals("Synchronized", serversView.getServerPublishStatus(configuredState.getServer().name));
		// web
		serversView.openWebPage(configuredState.getServer().name, PROJECT_NAME);
		assertThat("Hello tests!", new PageSourceMatcher());
	}
}
