package org.jboss.tools.central.test.ui.reddeer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerModule;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.reddeer.wizards.NewProjectExamplesWizardDialogCentral;
import org.jboss.tools.central.test.ui.reddeer.internal.ErrorsReporter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@JBossServer(type = ServerReqType.WILDFLY, state = ServerReqState.RUNNING)
public class HTML5Test {

	private static DefaultEditor centralEditor;
	private InternalBrowser browser;
	private Logger log = new Logger(this.getClass());
	private ProjectExplorer projectExplorer;
	private static ErrorsReporter reporter = ErrorsReporter.getInstance();
	private ExamplesOperator operator = ExamplesOperator.getInstance();
	private JavaScriptHelper jsHelper = JavaScriptHelper.getInstance();

	@InjectRequirement
	ServerRequirement req;

	@BeforeClass
	public static void setupClass() {
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		centralEditor = new DefaultEditor("JBoss Central");
	}

	@Before
	public void setup() {
		projectExplorer = new ProjectExplorer();
		projectExplorer.open();
	}

	@After
	public void teardown() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects(true);
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}

	@AfterClass
	public static void teardownClass() {
		reporter.generateReport();
	}

	@Test
	public void importEAP64ExamplesTest() {
		centralEditor.activate();
		browser = new InternalBrowser();
		jsHelper.setBrowser(browser);
		jsHelper.searchFor("eap-6.4.0.GA");
		// jsHelper.searchFor("Shows how to use Java EE Declarative Security to
		// Control Access to EJB 3");
		do {
			String[] examples = jsHelper.getExamples();
			log.error("List of examples: " + Arrays.toString(examples));
			for (String exampleName : examples) {
				log.step("Processing example: " + exampleName);
				if (!(exampleName.equals("app-client") || exampleName.equals("cluster-ha-singleton")
						|| exampleName.equals("ejb-asynchronous"))) {
					processCurrentExample(exampleName);
				}

			}
			jsHelper.nextPage();
		} while (jsHelper.hasNext());
	}

	/**
	 * Imports current example, checks for warnings/errors, tries to deploy it
	 * to server and finally deletes it.
	 * 
	 * @param exampleName
	 */
	private void processCurrentExample(String exampleName) {
		boolean skip = false;

		// import
		try {
			importExample(exampleName);
		} catch (Exception e) {
			skip = true;
			reporter.addError(new org.jboss.tools.central.reddeer.projects.Project(exampleName, null),
					"Error importing example: " + stacktraceToString(e));
		}

		org.jboss.tools.central.reddeer.projects.Project currentProject;

		if (!skip) {
			currentProject = new org.jboss.tools.central.reddeer.projects.Project(exampleName, getProjectName());
			// check for errors/warning
			checkErrorLog(currentProject);
			// try to deploy
			try {
				operator.deployProject(currentProject.getProjectName(), req.getServerNameLabelText(req.getConfig()));
				if (!exampleName.equals("ejb-security")) { // due to native
															// window popping up
					operator.checkDeployedProject(currentProject.getProjectName(),
							req.getServerNameLabelText(req.getConfig()));
				}
			} catch (CoreLayerException cle) {
				// log error
				reporter.addError(currentProject, "Unable to deploy example: " + currentProject.getName() + "("
						+ currentProject.getProjectName() + ")");
				try {
					new OkButton().click();
				} catch (Exception e) {
					reporter.addError(currentProject, stacktraceToString(cle));
				}
			}
		}
		// delete
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects(true);
		ServersView serversView = new ServersView();
		serversView.open();
		Server server = serversView.getServer(req.getServerNameLabelText(req.getConfig()));
		List<ServerModule> modules = server.getModules();
		for (ServerModule serverModule : modules) {
			serverModule.remove();
		}

	}

	private String stacktraceToString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	private void importExample(String exampleName) {
		log.step("Importing example: " + exampleName);
		centralEditor.activate();
		jsHelper.clickExample(exampleName);
		NewProjectExamplesWizardDialogCentral wizardDialog = new NewProjectExamplesWizardDialogCentral();
		wizardDialog.finish(exampleName);
	}

	private void checkErrorLog(org.jboss.tools.central.reddeer.projects.Project p) {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		ProblemsView pv = new ProblemsView();
		pv.open();
		for (Problem error : pv.getProblems(ProblemType.ERROR)) {
			reporter.addError(p, error.getDescription());
		}
		for (Problem warning : pv.getProblems(ProblemType.WARNING)) {
			reporter.addWarning(p, warning.getDescription());
		}

	}

	private String getProjectName() {
		projectExplorer.activate();
		List<Project> projects = projectExplorer.getProjects();
		// if (projects.size() !=1){
		// StringBuilder sb = new StringBuilder();
		// sb.append("There is more/less then one project imported\n");
		// for (Project project : projects) {
		// sb.append(project.getName()+"\n");
		// }
		// fail(sb.toString());
		// }
		return projects.get(0).getName();
	}

	// private void deleteProject(String exampleProjectName) {
	// projectExplorer.activate();
	// projectExplorer.getProject(exampleProjectName).delete(true);
	// }
}
