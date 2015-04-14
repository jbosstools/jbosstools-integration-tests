package org.jboss.tools.central.reddeer.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerModule;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.central.reddeer.projects.ArchetypeProject;
import org.jboss.tools.central.reddeer.projects.CentralExampleProject;
import org.jboss.tools.central.reddeer.wizards.JBossCentralProjectWizard;
import org.jboss.tools.central.reddeer.wizards.NewProjectExamplesReadyPage;
import org.jboss.tools.central.reddeer.wizards.NewProjectExamplesWizardDialogCentral;
import org.jboss.tools.maven.reddeer.project.examples.wait.MavenRepositoryNotFound;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardFirstPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ExampleRequirement;
import org.jboss.tools.maven.reddeer.project.examples.wizard.MavenExamplesRequirementPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.NewProjectExamplesStacksRequirementsPage;

/**
 * This singleton class is helper for adding/deploying projects and examples
 * from JBoss Central or via File->New->Project Examples
 * 
 * 
 * @author rhopp
 *
 */

public class ExamplesOperator {
	
	private static final Logger log = Logger.getLogger(ExamplesOperator.class);

	private static ExamplesOperator instance;

	public static ExamplesOperator getInstance() {
		if (instance == null) {
			instance = new ExamplesOperator();
		}
		return instance;
	}

	
	/**
	 * Deploys existing project to given server.
	 * 
	 * @param projectName
	 * @param serverName
	 */
	
	public void deployProject(String projectName, String serverName) {
		JBossServerView serversView = new JBossServerView();
		serversView.open();
		ModifyModulesDialog modulesDialog = serversView.getServer(serverName)
				.addAndRemoveModules();
		modulesDialog.getFirstPage().add(projectName);
		modulesDialog.finish();
		new WaitUntil(new WaitForProjectToStartAndSynchronize(projectName, serverName),
				TimePeriod.LONG);
	}
	
	/**
	 * Imports archetype project from JBoss Central from section "Start from scratch"
	 * 
	 * @param project
	 */

	public void importArchetypeProject(ArchetypeProject project) {
		log.step("Import project start");
		JBossCentralProjectWizard dialog = new JBossCentralProjectWizard(project);
		dialog.open();
		NewProjectExamplesStacksRequirementsPage firstPage = (NewProjectExamplesStacksRequirementsPage) dialog
				.getCurrentWizardPage();
		firstPage.setTargetRuntime(1);
		log.step("Import project first page");
		new DefaultLink();
		if (project.isBlank()){
			firstPage.toggleBlank(project.isBlank());
		}
		checkRequirements(firstPage.getRequirements());
		dialog.next();
		ArchetypeExamplesWizardFirstPage secondPage = (ArchetypeExamplesWizardFirstPage) dialog
				.getCurrentWizardPage();
		assertFalse("Project Name cannot be empty", secondPage.getProjectName().equals(""));
		dialog.next();
		ArchetypeExamplesWizardPage thirdPage = (ArchetypeExamplesWizardPage) dialog.getCurrentWizardPage();
		assertFalse("Group ID cannot be empty",thirdPage.getGroupID().equals(""));
		NewProjectExamplesReadyPage projectReadyPage = dialog.finishAndWait();
		checkProjectReadyPage(projectReadyPage, project);
		projectReadyPage.finish();
		checkForErrors();
	}

	/**
	 * Imports example project from JBoss Central from "Start from a sample" section
	 * 
	 * @param project
	 */
	
	public void importExampleProjectFromCentral(CentralExampleProject project) {
		NewProjectExamplesWizardDialogCentral dialog = new NewProjectExamplesWizardDialogCentral();
		dialog.open(project);
		MavenExamplesRequirementPage reqPage = (MavenExamplesRequirementPage) dialog
				.getWizardPage(0);
		checkRequirements(reqPage.getRequirements());
		try{
			new WaitUntil(new MavenRepositoryNotFound());
			fail("Maven repository is not present. Link with message: "+new DefaultLink().getText());
		}catch(WaitTimeoutExpiredException ex){
			//Do nothing
		}
		dialog.next();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		try {
			dialog.finish(project.getProjectName());
		} catch (WaitTimeoutExpiredException ex) { // waiting in dialog.finish()
													// is not enough!
			new WaitWhile(new ShellWithTextIsActive("New Project Example"),
					TimePeriod.VERY_LONG);
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
		checkForErrors();	
	}

	/**
	 * Checks whether is project deployed properly.
	 * 
	 * @param projectName
	 * @param serverNameLabel
	 */
	
	public void checkDeployedProject(String projectName, String serverNameLabel) {
		new WaitUntil(new ConsoleHasNoChange(), TimePeriod.LONG);
		JBossServerView serversView = new JBossServerView();
		serversView.open();
		JBossServerModule module = serversView.getServer(serverNameLabel)
				.getModule(projectName);
		module.openWebPage();
		final BrowserEditor browser = new BrowserEditor(new RegexMatcher(".*"));
		try {
			new WaitUntil(new BrowserIsnotEmpty(browser));
		} catch (WaitTimeoutExpiredException e) {
			// try to refresh browser and wait one more time.
			browser.refreshPage();
			new WaitUntil(new BrowserIsnotEmpty(browser));
		}
		assertNotEquals("", browser.getText());
		new DefaultEditor().close();
	}

	/**
	 * Returns all warnings currently in workspace.
	 * 
	 * @param project
	 * @return
	 */
	
	public List<String> getAllWarnings(){
		List<String> warnings = new ArrayList<String>();
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		for (Problem warning : problemsView.getProblems(ProblemType.WARNING)) {
			warnings.add(warning.getDescription());
		}
		return warnings;
	}
	
	public List<String> getAllErrors(){
		List<String> errors = new ArrayList<String>();
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		for (Problem error : problemsView.getProblems(ProblemType.ERROR)) {
			errors.add(error.getDescription());
		}
		return errors;
	}

	private void checkProjectReadyPage(NewProjectExamplesReadyPage page, ArchetypeProject project){
		assertFalse(page.isQuickFixEnabled());
		if (!project.isBlank()){
			assertTrue(page.isShowReadmeEnabled());
		}
	}

	private void checkForErrors() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<Problem> errors = problemsView.getProblems(ProblemType.ERROR);
		if (!errors.isEmpty()) {
			String failureMessage = "There are errors after importing project";
			for (Problem problem: errors) {
				failureMessage += problem.getDescription();
				failureMessage += System.getProperty("line.separator");
			}
			fail(failureMessage);
		}
	}

	private void checkRequirements(List<ExampleRequirement> requirements) {
		for (ExampleRequirement requirement : requirements) {
			assertTrue("Requirement \""+requirement.getName()+"\" is not met.", requirement.isMet());
		}
	}

	private class WaitForProjectToStartAndSynchronize implements WaitCondition {

		String projectName;
		String serverName;
		JBossServerModule module = null;

		public WaitForProjectToStartAndSynchronize(String projectName, String serverName) {
			this.projectName = projectName;
			this.serverName = serverName;
		}

		public boolean test() {
			boolean synch = getModule().getLabel().getPublishState()
					.compareTo(ServerPublishState.SYNCHRONIZED) == 0;
			boolean started = getModule().getLabel().getState()
					.compareTo(ServerState.STARTED) == 0;
			// return synch && started;  https://issues.jboss.org/browse/JBIDE-19288
			return synch;
		}

		public String description() {
			return "Waiting for module to be started-synchronized, but was "
					+ getModule().getLabel().getState() + "-"
					+ getModule().getLabel().getPublishState();
		}
		
		private JBossServerModule getModule(){
			int counter = 0;
			while(module == null && counter<5){
				JBossServerView serversView = new JBossServerView();
				serversView.open();
				try{
					module =  serversView.getServer(serverName).getModule(
						projectName);
				}catch(EclipseLayerException ex){
					//module not found
					counter++;
				}
			}
			return module;
		}
	}
	
	private class BrowserIsnotEmpty implements WaitCondition {
		
		BrowserEditor browser;
		
		public BrowserIsnotEmpty(BrowserEditor browser) {
			this.browser = browser;
		}
		
		public boolean test() {
			return !browser.getText().equals("");
		}

		public String description() {
			return "Browser is empty!";
		}
	}

}
