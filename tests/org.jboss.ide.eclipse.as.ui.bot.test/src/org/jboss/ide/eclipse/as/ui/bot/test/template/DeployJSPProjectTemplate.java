package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.requirement.CloseAllEditorsRequirement.CloseAllEditors;
import org.jboss.ide.eclipse.as.reddeer.server.editor.ServerModuleWebPageEditor;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.ui.bot.test.Activator;
import org.jboss.ide.eclipse.as.ui.bot.test.condition.EditorWithBrowserContainsTextCondition;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.wst.common.project.facet.ui.RuntimesPropertyPage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
@CloseAllEditors
public abstract class DeployJSPProjectTemplate extends AbstractJBossServerTemplate {

	public static final String PROJECT_NAME = "jsp-project";
	
	private static final Logger log = Logger.getLogger(DeployJSPProjectTemplate.class);
	
	protected abstract String getConsoleMessage();

	@Before
	public void importProject(){
		log.step("Import " + PROJECT_NAME);
		ExternalProjectImportWizardDialog dialog = new ExternalProjectImportWizardDialog();
		dialog.open();
		
		WizardProjectsImportPage page = dialog.getFirstPage();
		page.setArchiveFile(Activator.getPathToFileWithinPlugin("projects/jsp-project.zip"));
		page.selectProjects(PROJECT_NAME);
		
		dialog.finish();
		
		Project project = new ProjectExplorer().getProject(PROJECT_NAME);
		
		log.step("Set targeted runtime for " + PROJECT_NAME);
		RuntimesPropertyPage targetedRuntimes = new RuntimesPropertyPage(project);
		targetedRuntimes.open();
		targetedRuntimes.selectRuntime(requirement.getRuntimeNameLabelText(requirement.getConfig()));
		targetedRuntimes.ok();
	}

	@Before
	public void clearServerConsole(){
		super.clearConsole();
	}
	
	@Test
	public void deployProject(){
		log.step("Add " + PROJECT_NAME + " to the server (Add module dialog)");
		JBossServer server = getServer();
		addModule(server);

		// view
		log.step("Assert module is visible on Servers view");
		assertNotNull("Server contains project", server.getModule(PROJECT_NAME));
		// console
		log.step("Assert console has deployment notification");
		new WaitUntil(new ConsoleHasText(getConsoleMessage()));
		assertNoException("Error in console after deploy");
		// web
		log.step("Open module's web page");
		ServerModuleWebPageEditor editor = server.getModule(PROJECT_NAME).openWebPage();
		new WaitUntil(new EditorWithBrowserContainsTextCondition(editor, "Hello tests"));
		
		log.step("Assert web page text");
		assertThat(editor.getText(), containsString("Hello tests!"));
		// view
		log.step("Assert server's states");
		assertThat(getServer().getLabel().getState(), is(ServerState.STARTED));
		assertThat(getServer().getLabel().getPublishState(), is(ServerPublishState.SYNCHRONIZED));
		// problems
		log.step("Assert no error on Problems view");
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		assertThat(problemsView.getProblems(ProblemType.ERROR).size(), is(0));
	}

	private void addModule(JBossServer server) {
		ModifyModulesDialog modifyModulesDialog = server.addAndRemoveModules();
		ModifyModulesPage modifyModulesPage = modifyModulesDialog.getFirstPage();
		modifyModulesPage.add(PROJECT_NAME);
		modifyModulesDialog.finish();
	}
}
