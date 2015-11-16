package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.ui.IViewReference;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.lookup.WorkbenchPartLookup;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerModule;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.reddeer.wizards.NewProjectExamplesWizardDialogCentral;
import org.jboss.tools.central.test.ui.reddeer.internal.ErrorsReporter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@JBossServer(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
public class HTML5Parameterized {

	private static DefaultEditor centralEditor;
	private static InternalBrowser browser;
	private static ErrorsReporter reporter = ErrorsReporter.getInstance();
	private static ExamplesOperator operator = ExamplesOperator.getInstance();
	private static JavaScriptHelper jsHelper = JavaScriptHelper.getInstance();
	private static Logger log = new Logger(HTML5Parameterized.class);
	private ProjectExplorer projectExplorer;

	@InjectRequirement
	ServerRequirement req;
	
	@Parameters(name="{0}")
	public static Collection<CentralProject> data() {
		closeWelcomeScreen();
		List<CentralProject> resultList = new ArrayList<CentralProject>();
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		centralEditor = new DefaultEditor("JBoss Central");
		centralEditor.activate();
		browser = new InternalBrowser();
		jsHelper.setBrowser(browser);
		new WaitWhile(new CentralBrowserIsLoading(), TimePeriod.LONG);
		jsHelper.searchFor("eap-6.4.0.GA");
		do {
			String[] examples = jsHelper.getExamples();
			for (String exampleName : examples) {
				if (!(exampleName.equals("app-client") || exampleName.equals("cluster-ha-singleton")
						|| exampleName.equals("ejb-asynchronous"))) {
					resultList.add(new CentralProject(exampleName, jsHelper.getDescriptionForExample(exampleName)));
				}

			}
			jsHelper.nextPage();
		} while (jsHelper.hasNext());
		return resultList;
	}

	private static void closeWelcomeScreen() {
		log.debug("Trying to close Welcome Screen");
		for (IViewReference viewReference : WorkbenchPartLookup.getInstance().findAllViewReferences()) {
			if (viewReference.getPartName().equals("Welcome")) {
				final IViewReference iViewReference = viewReference;
				Display.syncExec(new Runnable() {
					@Override
					public void run() {
						iViewReference.getPage().hideView(iViewReference);
					}
				});
				log.debug("Welcome Screen closed");
				break;
			}
		}
	}
	
	@BeforeClass
	public static void setupClass(){
		closeWelcomeScreen();
		String mvnConfigFileName = new File("target/classes/settings.xml").getAbsolutePath();
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenSettingsPreferencePage prefPage = new MavenSettingsPreferencePage();
		preferenceDialog.select(prefPage);
		prefPage.setUserSettingsLocation(mvnConfigFileName);
		preferenceDialog.ok();
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
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		consoleView.clearConsole();
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}

	
	@AfterClass
	public static void teardownClass() {
		reporter.generateReport();
	}
	
	@Parameter
	public CentralProject project;

	@Test
	public void testik() {
		log.error("Processing example: "+project.getName());
		log.error("\twith description: "+project.getDescription());
		processExample(project.getName(), project.getDescription());
	}
	
	/**
	 * Imports current example, checks for warnings/errors, tries to deploy it
	 * to server and finally deletes it.
	 * 
	 * @param exampleName
	 */
	private void processExample(String exampleName, String description) {
		boolean skip = false;
		jsHelper.searchFor(description);
		String[] examples = jsHelper.getExamples();
		if (examples.length>1){
			fail("Muj fail! :-D");
		}
		// import
		try {
			importExample(exampleName);
		} catch (Exception e) {
			skip = true;
			fail("Error importing example: " + stacktraceToString(e));
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
				new OkButton().click();
				// log error
				fail("Unable to deploy example: " + currentProject.getName() + "("
						+ currentProject.getProjectName() + ")"+"\nReason: "+stacktraceToString(cle));
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
		StringBuilder sb = new StringBuilder("Errors after project example import\n");
		boolean errorsArePresent = false;
		for (Problem error : pv.getProblems(ProblemType.ERROR)) {
//			reporter.addError(p, error.getDescription());
			sb.append(error.getDescription()+"\n");
			errorsArePresent = true;
		}
		for (Problem warning : pv.getProblems(ProblemType.WARNING)) {
			reporter.addWarning(p, warning.getDescription());
		}
		if (errorsArePresent){
			fail(sb.toString());
		}

	}

	private String getProjectName() {
		projectExplorer.activate();
		List<Project> projects = projectExplorer.getProjects();
		return projects.get(0).getName();
	}
	
	
	static class CentralBrowserIsLoading extends AbstractWaitCondition{

		@Override
		public boolean test() {
			String html = jsHelper.getHTML();
			System.out.println(html);
			return html.contains("Loading JBoss Central, please wait");
		}

		@Override
		public String description() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
