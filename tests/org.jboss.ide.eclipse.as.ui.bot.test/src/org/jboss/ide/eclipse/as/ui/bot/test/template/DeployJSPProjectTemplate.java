package org.jboss.ide.eclipse.as.ui.bot.test.template;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.ide.eclipse.as.reddeer.requirement.CloseAllEditorsRequirement.CloseAllEditors;
import org.jboss.ide.eclipse.as.reddeer.server.editor.ServerModuleWebPageEditor;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerModule;
import org.jboss.ide.eclipse.as.ui.bot.test.Activator;
import org.jboss.ide.eclipse.as.ui.bot.test.condition.EditorWithBrowserContainsTextCondition;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.wst.common.project.facet.ui.RuntimesPropertyPage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


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
		
		Project project = getProject();
		
		log.step("Set targeted runtime for " + PROJECT_NAME);
		RuntimesPropertyPage targetedRuntimes = new RuntimesPropertyPage(project);
		targetedRuntimes.open();
		targetedRuntimes.selectRuntime(serverRequirement.getRuntimeNameLabelText(serverRequirement.getConfig()));
		
		OkButton ok = new OkButton();
		ok.click();
		new WaitWhile(new ShellWithTextIsAvailable(targetedRuntimes.getPageTitle())); 
	}

	@Before
	public void clearServerConsole(){
		super.clearConsole();
	}
	
	@Test
	public void deployProject(){
		log.step("Assert project is built");
		try {
			assertProjectIsBuilt();
		} catch (Exception e){
			log.step("*** Refresh project");
			getProject().refresh();
			log.step("Re-assert project is built");
			assertProjectIsBuilt();
			log.step("Re-throw exception");
			throw e;
		}
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
		JBossServerModule module = server.getModule(PROJECT_NAME);
		module.openWebPage();
		ServerModuleWebPageEditor editor = new ServerModuleWebPageEditor(module.getLabel().getName()); 
				
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

	private void assertProjectIsBuilt() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		log.debug("Workspace root is: " + root.getLocation().toFile());

		IProject project = root.getProject(PROJECT_NAME);
		log.debug("Project location is: " + project.getLocation().toFile());
		
		IFolder buildFolder = project.getFolder("build");
		log.debug("Build folder location is: " + buildFolder.getLocation().toFile());
		log.debug("Build folder exists: " + buildFolder.exists());
		
		IFolder classesFolder = project.getFolder("build/classes/org");
		log.debug("Classes folder location is: " + classesFolder.getLocation().toFile());
		log.debug("Classes folder exists: " + classesFolder.exists());
		
		File buildFolderFilesystem = project.getFile("build").getLocation().toFile();
		log.debug("Build folder on filesystem exists: " + buildFolderFilesystem.exists());

		if (buildFolderFilesystem.exists()){
			log.debug("Children of build folder");
			for (String fileName : buildFolderFilesystem.list()){
				log.debug(fileName);
				if ("classes".equals(fileName)){
					log.debug("Children of classes folder");
					for (String fileName2 : new File(buildFolderFilesystem, fileName).list()){
						log.debug(fileName2);
					}
				}
			}
		}
		assertTrue(classesFolder.exists());
	}

	private void addModule(JBossServer server) {
		ModifyModulesDialog modifyModulesDialog = server.addAndRemoveModules();
		ModifyModulesPage modifyModulesPage = modifyModulesDialog.getFirstPage();
		modifyModulesPage.add(PROJECT_NAME);
		modifyModulesDialog.finish();
	}
	
	private Project getProject(){
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		return explorer.getProject(PROJECT_NAME);
	}
}
