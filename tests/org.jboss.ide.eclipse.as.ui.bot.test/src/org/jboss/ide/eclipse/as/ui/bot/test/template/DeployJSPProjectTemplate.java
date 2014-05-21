package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.ide.eclipse.as.ui.bot.test.Activator;
import org.jboss.ide.eclipse.as.ui.bot.test.condition.BrowserContainsTextCondition;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.wst.common.project.facet.ui.RuntimesPropertyPage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


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
public abstract class DeployJSPProjectTemplate {

	public static final String PROJECT_NAME = "jsp-project";
	
	@InjectRequirement
	protected ServerRequirement requirement;

	protected abstract String getConsoleMessage();

	@Before
	public void importProject(){
		ExternalProjectImportWizardDialog dialog = new ExternalProjectImportWizardDialog();
		dialog.open();
		
		WizardProjectsImportPage page = dialog.getFirstPage();
		page.setArchiveFile(Activator.getPathToFileWithinPlugin("projects/jsp-project.zip"));
		page.selectProjects(PROJECT_NAME);
		
		dialog.finish();
		
		Project project = new ProjectExplorer().getProject(PROJECT_NAME);
		RuntimesPropertyPage targetedRuntimes = new RuntimesPropertyPage(project);
		targetedRuntimes.open();
		targetedRuntimes.selectRuntime(requirement.getRuntimeNameLabelText(requirement.getConfig()));
		targetedRuntimes.ok();
	}

	@Test
	public void deployProject(){
		JBossServerView serversView = new JBossServerView();
		JBossServer server = serversView.getServer(getServerName());
		addModule(server);

		// view
		assertNotNull("Server contains project", server.getModule(PROJECT_NAME));
		// console
		new WaitUntil(new ConsoleHasText(getConsoleMessage()));
		assertFalse(new ConsoleHasText("Exception").test());
		// web
		serversView.open();
		server.getModule(PROJECT_NAME).openWebPage();
		new WaitUntil(new BrowserContainsTextCondition("Hello tests!"), TimePeriod.NORMAL);
		// view
		assertThat(getServer().getLabel().getState(), is(ServerState.STARTED));
		assertThat(getServer().getLabel().getPublishState(), is(ServerPublishState.SYNCHRONIZED));
		// problems
		assertThat(new ProblemsView().getAllErrors().size(), is(0));
	}

	private void addModule(JBossServer server) {
		ModifyModulesDialog modifyModulesDialog = server.addAndRemoveModules();
		ModifyModulesPage modifyModulesPage = modifyModulesDialog.getFirstPage();
		modifyModulesPage.add(PROJECT_NAME);
		modifyModulesDialog.finish();
	}
	
	protected Server getServer(){
		return new org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView().getServer(getServerName());
	}

	protected String getServerName() {
		return requirement.getServerNameLabelText(requirement.getConfig());
	} 
}
