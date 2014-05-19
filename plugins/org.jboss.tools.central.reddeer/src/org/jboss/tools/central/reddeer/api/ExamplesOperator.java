package org.jboss.tools.central.reddeer.api;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerModule;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.formtext.DefaultFormText;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.tools.central.reddeer.projects.ArchetypeProject;
import org.jboss.tools.central.reddeer.projects.CentralExampleProject;
import org.jboss.tools.central.reddeer.projects.Project;
import org.jboss.tools.central.reddeer.wizards.JBossCentralProjectWizard;
import org.jboss.tools.central.reddeer.wizards.NewProjectExamplesWizardDialogCentral;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardFirstPage;
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
		ModifyModulesDialog modulesDialog = serversView.getServers().get(0)
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
		new DefaultHyperlink(project.getName()).activate();
		JBossCentralProjectWizard dialog = new JBossCentralProjectWizard();
		NewProjectExamplesStacksRequirementsPage firstPage = (NewProjectExamplesStacksRequirementsPage) dialog
				.getCurrentWizardPage();
		firstPage.setTargetRuntime(1);
		if (project.isBlank()){
			firstPage.toggleBlank(project.isBlank());
		}
		dialog.next();
		ArchetypeExamplesWizardFirstPage secondPage = (ArchetypeExamplesWizardFirstPage) dialog
				.getCurrentWizardPage();
		if (secondPage.getProjectName().equals("")) {
			secondPage.setPackage(project.getProjectName());
		}
			dialog.finish(project.getProjectName(), project.isBlank());
	}

	/**
	 * Imports example project from JBoss Central from "Start from a sample" section
	 * 
	 * @param project
	 */
	
	public void importExampleProjectFromCentral(CentralExampleProject project) {
		ExampleLabel label = new ExampleLabel(project.getCategory());
		label.hover();
		new DefaultFormText(project.getName()).click();
		NewProjectExamplesWizardDialogCentral dialog = new NewProjectExamplesWizardDialogCentral();
		MavenExamplesRequirementPage reqPage = (MavenExamplesRequirementPage) dialog
				.getWizardPage(0);
		checkRequirements(reqPage.getRequirements());
		dialog.next();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		try {
			dialog.finish();
		} catch (WaitTimeoutExpiredException ex) { // waiting in dialog.finish()
													// is not enough!
			new WaitWhile(new ShellWithTextIsActive("New Project Example"),
					TimePeriod.VERY_LONG);
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
		checkForErrors(project);
	}

	/**
	 * Checks whether is project deployed properly.
	 * 
	 * @param projectName
	 * @param serverNameLabel
	 */
	
	public void checkDeployedProject(String projectName, String serverNameLabel) {
		JBossServerView serversView = new JBossServerView();
		JBossServerModule module = serversView.getServer(serverNameLabel)
				.getModule(projectName);
		module.openWebPage();
		final InternalBrowser browser = new InternalBrowser();
		new WaitUntil(new WaitCondition() {

			public boolean test() {
				return !browser.getText().equals("");
			}

			public String description() {
				return null;
			}
		});
		assertNotEquals("", browser.getText());
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
		for (TreeItem warning : problemsView.getAllWarnings()) {
			warnings.add(warning.getText());
		}
		return warnings;
	}
	
	private void checkForErrors(Project project) {
		new DefaultShell("New Project Example");
		if (project instanceof ArchetypeProject) {
			if (!((ArchetypeProject) project).isBlank()) {
				CheckBox readmeChckBox = new CheckBox(1);
				assertTrue("Readme checkbox should be checked by default",
						readmeChckBox.isChecked());
			}
		}
		boolean showQuickFix = new CheckBox("Show the Quick Fix dialog")
				.isChecked();
		new PushButton("Finish").click();
		if (showQuickFix) {
			new DefaultShell("Quick Fix").close();
		}
		new WaitWhile(new JobIsRunning());
		List<TreeItem> errors = new ProblemsView().getAllErrors();
		if (!errors.isEmpty()) {
			String failureMessage = "There are errors after importing project";
			for (TreeItem treeItem : errors) {
				failureMessage += treeItem.getText();
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
			return synch && started;
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

	private class ExampleLabel extends DefaultLabel {

		public ExampleLabel(String label) {
			super(label);
		}

		public void hover() {
			WidgetHandler.getInstance().notify(SWT.MouseEnter, getSWTWidget());
		}
	}

}
