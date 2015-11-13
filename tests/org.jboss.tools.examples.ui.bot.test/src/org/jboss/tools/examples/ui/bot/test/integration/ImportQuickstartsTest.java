package org.jboss.tools.examples.ui.bot.test.integration;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.DefineMavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.PropertyDefinedMavenRepository;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard.MavenImportWizardException;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizardFirstPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * This testsuite consist of one test. It imports all examples (location of examples is defined by system property
 * "examplesLocation") and checks for errors and warnings.
 * 
 * @author rhopp
 *
 */

@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@DefineMavenRepository(propDefMavenRepo = @PropertyDefinedMavenRepository(ID = "test", snapshots = true) )
public class ImportQuickstartsTest {

	private QuickstartsReporter reporter = QuickstartsReporter.getInstance();
	private static LogView errorLogView;

	@BeforeClass
	public static void setup() {
		errorLogView = new LogView();
		errorLogView.open();
		errorLogView.deleteLog();
	}

	@Parameters(name = "{0}")
	public static Collection<Quickstart> data() {
		ArrayList<Quickstart> resultList = new ArrayList<Quickstart>();
		File file = new File(System.getProperty("examplesLocation"));
		FileFilter directoryFilter = new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		};
		for (File f : file.listFiles(directoryFilter)) {
			System.out.println("PROCESSING " + f.getAbsolutePath());
			if (f.getAbsolutePath().contains("picketlink") || f.getAbsolutePath().contains("wsat")) { // JBIDE-18497
																										// Picketlink
																										// quickstart
																										// is
																										// not
																										// working
				QuickstartsReporter.getInstance().addError(new Quickstart("Picketlink", f.getAbsolutePath()),
						"Picketlink was skipped due to JBIDE-18497");
				continue;
			}
			Quickstart qstart = new Quickstart(f.getName(), f.getAbsolutePath());
			resultList.add(qstart);
		}
		return resultList;
	}

	@Parameter
	public Quickstart qstart;

	/*
	 * Main tests. Imports quickstart as maven project and performs checks.
	 */
	@Test
	public void quickstartTest() {
		importQuickstart(qstart);
		checkForWarnings(qstart);
		checkForErrors(qstart);
		checkErrorLog(qstart);
	}

	@After
	public void cleanup() {
		cleanupShells();
		deleteAllProjects();
	}

	@AfterClass
	public static void teardown() {
		QuickstartsReporter.getInstance().generateReport();
		QuickstartsReporter.getInstance().generateErrorFilesForEachProject(new File("target/reports/"));
	}

	private void checkErrorLog(Quickstart qstart) {
		List<LogMessage> allErrors = errorLogView.getErrorMessages();
		String errorMessages = "";
		for (LogMessage message : allErrors) {
			reporter.addError(qstart, message.getMessage());
			errorMessages += "\t" + message.getMessage() + "\n";
		}
		errorLogView.deleteLog();
		if (!allErrors.isEmpty()) {
			fail("There are errors in error log:\n" + errorMessages);
		}
	}

	private void checkForWarnings(Quickstart q) {
		for (String warning : ExamplesOperator.getInstance().getAllWarnings()) {
			reporter.addWarning(q, warning);
		}

	}

	private void checkForErrors(Quickstart q) {
		for (String string : ExamplesOperator.getInstance().getAllErrors()) {
			if (string.contains("not up-to-date with pom.xml")) { // maven update is needed sometimes.
				runUpdate();
			}
		}
		String errorMessages = "";
		List<String> allErrors = ExamplesOperator.getInstance().getAllErrors();
		for (String error : allErrors) {
			reporter.addError(q, error);
			errorMessages += "\t" + error + "\n";
		}
		if (!allErrors.isEmpty()) {
			fail("There are errors in imported project:\n" + errorMessages);
		}
	}

	private void runUpdate() {
		new ProjectExplorer().getProjects().get(0).select();
		new ContextMenu("Maven", "Update Project...").select();
		new DefaultShell("Update Maven Project");
		new PushButton("Select All").click();
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

	}

	private void deleteAllProjects() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		List<Project> projects = projectExplorer.getProjects();
		for (Project p : projects) {
			DeleteUtils.forceProjectDeletion(p, false);
		}
	}

	private void cleanupShells() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	private void importQuickstart(Quickstart quickstart) {
		ExtendedMavenImportWizard mavenImportWizard = new ExtendedMavenImportWizard();
		mavenImportWizard.open();
		MavenImportWizardFirstPage wizPage = new MavenImportWizardFirstPage();
		try {
			wizPage.setRootDirectory(quickstart.getPath().getAbsolutePath());
		} catch (WaitTimeoutExpiredException e) {
			reporter.addError(quickstart, "There is no project in this directory");
			cleanupShells();
			fail("There is no project in this directory");
			return;
		}
		try {
			mavenImportWizard.finish();
		} catch (MavenImportWizardException e) {
			for (String error : e.getErrors()) {
				reporter.addError(quickstart, error);
			}
		}
	}

	private static void setupMavenRepo() {
		String mvnConfigFileName = new File("target/classes/settings.xml").getAbsolutePath();
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenSettingsPreferencePage prefPage = new MavenSettingsPreferencePage();
		preferenceDialog.select(prefPage);
		prefPage.setUserSettingsLocation(mvnConfigFileName);
		preferenceDialog.ok();
		new WaitUntil(new JobIsRunning());
	}

	/**
	 * Extended maven import wizard. When super.finsh() fails, waits another 15 minutes.
	 * 
	 * @author rhopp
	 *
	 */

	private class ExtendedMavenImportWizard extends MavenImportWizard {

		@Override
		public void finish() {
			try {
				super.finish();
			} catch (WaitTimeoutExpiredException ex) {
				new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(60 * 15));
			}
		}
	}
}
