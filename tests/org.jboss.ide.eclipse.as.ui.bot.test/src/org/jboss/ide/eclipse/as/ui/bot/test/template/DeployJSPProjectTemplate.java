package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.ui.bot.test.Activator;
import org.jboss.ide.eclipse.as.ui.bot.test.web.PageSourceMatcher;
import org.jboss.ide.eclipse.as.ui.bot.test.wizard.ImportProjectWizard;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.IsNot.not;

/**
 * Imports pre-prepared JSP project and adds it into the server. Checks:
 * 
 * <ul>
 * 	<li>the console output</li>
 * 	<li>server's label</li>
 * 	<li>project is listed under the server</li>
 * 	<li>the index.jsp of the project</li>
 * </ul>
 * @author Lucia Jelinkova
 *
 */
public abstract class DeployJSPProjectTemplate extends SWTTestExt {

	public static final String PROJECT_NAME = "jsp-project";

	protected abstract String getConsoleMessage();

	@Before
	public void importProject(){
		ImportProjectWizard wizard = new ImportProjectWizard();
		wizard.setZipFilePath(SWTUtilExt.getPathToFileWithinPlugin(Activator.PLUGIN_ID, "projects/jsp-project.zip"));
		wizard.setProjectNames(PROJECT_NAME);
		wizard.execute();
	}

	@Test
	public void deployProject(){
		ServersView serversView = new ServersView();
		serversView.addProjectToServer(PROJECT_NAME, getServerName());

		// console
		assertThat(getConsoleMessage(), new ConsoleOutputMatcher(TaskDuration.NORMAL));
		assertThat("Exception:", not(new ConsoleOutputMatcher()));
		// web
		serversView.openWebPage(getServerName(), PROJECT_NAME);
		assertThat("Hello tests!", new PageSourceMatcher(TaskDuration.SHORT));
		// view
		assertTrue("Server contains project", serversView.containsProject(getServerName(), PROJECT_NAME));
		assertEquals("Started", serversView.getServerStatus(getServerName()));
		assertEquals("Synchronized", serversView.getServerPublishStatus(getServerName()));
	}
	
	protected String getServerName(){
		return configuredState.getServer().name;
	}
}
