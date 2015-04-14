package org.jboss.tools.examples.ui.bot.test.integration;

import java.io.File;
import java.io.FileFilter;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard.MavenImportWizardException;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizardFirstPage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This testsuite consist of one test. It imports all examples (location of
 * examples is defined by system property "examplesLocation") and checks for
 * erros and warnings.
 * 
 * @author rhopp
 *
 */

@RunWith(RedDeerSuite.class)
public class ImportQuickstartsTest {

	private QuickstartsReporter reporter = QuickstartsReporter.getInstance();
	private static LogView errorLogView;

	@BeforeClass
	public static void setup() {
		setupMavenRepo();
		errorLogView = new LogView();
		errorLogView.open();
		errorLogView.deleteLog();
	}

	@Test
	public void importQuickstatsTest() {
		File file = new File(System.getProperty("examplesLocation"));
		FileFilter directoryFilter = new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		};
		for (File f : file.listFiles(directoryFilter)) {
			System.out.println("PROCESSING " + f.getAbsolutePath());
			if (f.getAbsolutePath().contains("picketlink")
					|| f.getAbsolutePath().contains("wsat")) { // JBIDE-18497
																// Picketlink
																// quickstart is
																// not working
				reporter.addError(
						new Quickstart("Picketlink", f.getAbsolutePath()),
						"Picketlink was skipped due to JBIDE-18497");
				continue;
			}
			Quickstart qstart = new Quickstart(f.getName(), f.getAbsolutePath());
			try {
				importQuickstart(qstart);
				checkForWarnings(qstart);
				checkForErrors(qstart);
				checkErrorLog(qstart);
			} catch (Exception ex) {
				reporter.addError(
						qstart,
						"Something completely wrong happened!: "
								+ ex.getMessage() + "\n");
			}
			deleteAllProjects();
			cleanupShells();
		}
		reporter.generateReport();
		reporter.generateErrorFilesForEachProject(new File("target/reports/"));
	}

	private void checkErrorLog(Quickstart qstart) {
		for (LogMessage message : errorLogView.getErrorMessages()){
			reporter.addError(qstart, message.getMessage());
		}
		errorLogView.deleteLog();
	}

	private void checkForWarnings(Quickstart q) {
		for (String warning : ExamplesOperator.getInstance().getAllWarnings()) {
			reporter.addWarning(q, warning);
		}

	}

	private void checkForErrors(Quickstart q) {
		for (String string : ExamplesOperator.getInstance().getAllErrors()) {
			if (string.contains("not up-to-date with pom.xml")) { // sometimes
																	// is maven
																	// update
																	// needed
				runUpdate();
			}
		}
		for (String error : ExamplesOperator.getInstance().getAllErrors()) {
			reporter.addError(q, error);
		}
	}

	private void runUpdate() {
		// TODO Auto-generated method stub
		new ProjectExplorer().getProjects().get(0).getTreeItem().select();
		new ContextMenu("Maven", "Update Project...").select();
		new DefaultShell("Update Maven Project");
		new PushButton("Select All").click();
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

	}

	private void deleteAllProjects() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.deleteAllProjects(false);

	}

	private void cleanupShells() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	private void importQuickstart(Quickstart quickstart) {
		ExtendedMavenImportWizard mavenImportWizard = new ExtendedMavenImportWizard();
		mavenImportWizard.open();
		MavenImportWizardFirstPage wizPage = (MavenImportWizardFirstPage) mavenImportWizard
				.getCurrentWizardPage();
		try {
			wizPage.setRootDirectory(quickstart.getPath().getAbsolutePath());
		} catch (WaitTimeoutExpiredException e) {
			reporter.addError(quickstart,
					"There is no project in this directory");
			cleanupShells();
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
