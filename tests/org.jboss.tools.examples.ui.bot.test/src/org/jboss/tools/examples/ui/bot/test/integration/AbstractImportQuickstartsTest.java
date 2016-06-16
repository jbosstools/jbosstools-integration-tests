/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.examples.ui.bot.test.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerModule;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ModuleLabel;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerModule;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard.MavenImportWizardException;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizardFirstPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * This testsuite consist of one test. It imports all examples (location of
 * examples is defined by system property "examplesLocation") and checks for
 * errors and warnings.
 * 
 * @author rhopp, jkopriva
 *
 */

public abstract class AbstractImportQuickstartsTest {

	protected static String SERVER_NAME = "";
	protected QuickstartsReporter reporter = QuickstartsReporter.getInstance();
	protected static LogView errorLogView;

	private static final Logger log = Logger.getLogger(AbstractImportQuickstartsTest.class);

	@BeforeClass
	public static void setup() {
		setupLog();
	}

	@After
	public void cleanup() {
		clearWorkspace();
	}

	@AfterClass
	public static void teardown() {
		createReports();
	}

	protected static void setServerName(String serverName) {
		SERVER_NAME = serverName;
	}

	protected void checkServerStatus() {
		ServersView serversView = new ServersView();
		serversView.open();
		Server server = serversView.getServer(getServerFullName(serversView.getServers(), SERVER_NAME));
		assertTrue("Server has not been started!", server.getLabel().getState() == ServerState.STARTED);
		// assertTrue("Server has not been
		// synchronized!",server.getLabel().getPublishState() ==
		// ServerPublishState.SYNCHRONIZED);
	}

	/*
	 * Deploy and undeploy quickstart on running server
	 */
	protected void deployUndeployQuickstart(Quickstart qstart, String serverName) {
		new ConsoleView().clearConsole();
		ProjectExplorer explorer = new ProjectExplorer();

		findDeployableProjects(qstart, explorer);

		ServersView serversView = new ServersView();
		serversView.open();
		String fullServerName = getServerFullName(serversView.getServers(), serverName);
		Server server = serversView.getServer(fullServerName);

		for (String deployableProjectName : qstart.getDeployableProjectNames()) {
			try {
				// deploy
				deployProject(deployableProjectName, explorer);
				// check deploy status
				checkDeployedProject(qstart, fullServerName);
				// undeploy
				unDeployModule(qstart.getName().equals("template") ? "QUICKSTART_NAME" : qstart.getName(), server);
			} catch (CoreLayerException ex) {
				new DefaultShell("Server Error");
				new OkButton().click();
			}
		}
	}

	private void unDeployModule(String moduleName, Server server) {
		log.info("UNDEPLOYING MODULE" + moduleName + " ON SERVER " + server.getLabel());
		ServerModule serverModule = server.getModule(new RegexMatcher(".*" + moduleName + ".*"));
		serverModule.remove();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	private void findDeployableProjects(Quickstart qstart, ProjectExplorer explorer) {
		explorer.activate();
		for (Project project : explorer.getProjects()) {
			explorer.getProject(project.getName()).select();
			try {
				if (new ContextMenu("Run As", "1 Run on Server").isEnabled()) {
					qstart.addDeployableProjectName(project.getName());
				}
				new WizardDialog().finish();
			} catch (CoreLayerException ex) {
				continue;// non deployable project
			}

		}

	}

	private void deployProject(String deployableProject, ProjectExplorer explorer) {
		log.info("DEPLOYING " + deployableProject);
		explorer.activate();
		Project project = explorer.getProject(deployableProject);
		project.select();
		new ContextMenu("Run As", "1 Run on Server").select();
		new WizardDialog().finish();
	}

	private String getServerFullName(List<Server> servers, String partOfName) {
		for (Server srv : servers) {
			if (srv.getLabel().getName().contains(partOfName)) {
				return srv.getLabel().getName();
			}
		}
		return null;
	}

	protected String getDeployableProjectName(List<Project> projects, String quickstartName) {
		for (Project prj : projects) {
			if (prj.getName().contains(quickstartName)) {
				return prj.getName();
			}
		}
		return null;
	}

	protected static Collection<Quickstart> createQuickstartsList() {
		ArrayList<Quickstart> resultList = new ArrayList<Quickstart>();
		ArrayList<String> specificQuickstarts = new ArrayList<String>();
		if (System.getProperty("specificQuickstarts") != null
				&& !System.getProperty("specificQuickstarts").trim().equals("${specificQuickstarts}")) {
			specificQuickstarts = new ArrayList<String>(
					Arrays.asList(System.getProperty("specificQuickstarts").trim().split(",")));
		}
		File file = new File(System.getProperty("examplesLocation"));
		FileFilter directoryFilter = new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		};
		for (File f : file.listFiles(directoryFilter)) {
			// if (f.getAbsolutePath().contains("picketlink") ||
			// f.getAbsolutePath().contains("wsat")) { // JBIDE-18497
			// // Picketlink
			// // quickstart
			// // is
			// // not
			// // working
			// QuickstartsReporter.getInstance().addError(new
			// Quickstart("Picketlink", f.getAbsolutePath()),
			// "Picketlink was skipped due to JBIDE-18497");
			// continue;
			// }
			if (!f.getPath().contains("/.")) {
				if (specificQuickstarts.size() == 0 || specificQuickstarts.contains(f.getName())) {
					log.info("PROCESSING " + f.getAbsolutePath());
					Quickstart qstart = new Quickstart(f.getName(), f.getAbsolutePath());
					resultList.add(qstart);
				}
			}
		}
		return resultList;
	}

	protected static void setupLog() {
		errorLogView = new LogView();
		errorLogView.open();
		errorLogView.deleteLog();
	}

	protected static void clearWorkspace() {
		cleanupShells();
		deleteAllProjects();
		closeBrowser();
		new ConsoleView().clearConsole();
	}

	protected void runQuickstarts(Quickstart qstart, String serverName) {
		try {
			importQuickstart(qstart);
			importTestUtilsIfNeeded(qstart);
			if (!isError() && System.getProperty("deployOnServer") != null
					&& System.getProperty("deployOnServer").equals("true")) {
				checkServerStatus();
				deployUndeployQuickstart(qstart, SERVER_NAME);
			}
		} catch (NoProjectException ex) {
			// there was no project in this directory. Pass the test.
			return;
		}
		checkForWarnings(qstart);
		checkForErrors(qstart);
		checkErrorLog(qstart);
	}

	protected static void createReports() {
		QuickstartsReporter.getInstance().generateReport();
		QuickstartsReporter.getInstance().generateErrorFilesForEachProject(new File("target/reports/"));
		QuickstartsReporter.getInstance().generateAllErrorsFile(new File("target/reports/allErrors.txt"));
	}

	protected void checkErrorLog(Quickstart qstart) {
		List<LogMessage> allErrors = new ArrayList<LogMessage>();
		List<LogMessage> errors = errorLogView.getErrorMessages();
		String errorMessages = "";
		for (LogMessage message : errors) {
			if (!message.getMessage().contains("Unable to delete")
					&& !message.getMessage().contains("Could not delete")) {
				reporter.addError(qstart, "ERROR IN ERROR LOG: " + message.getMessage());
				errorMessages += "\t" + message.getMessage() + "\n";
			}
		}
		errorLogView.deleteLog();
		if (!allErrors.isEmpty()) {
			fail("There are errors in error log:\n" + errorMessages);
		}
	}

	private void restartServer() {
		ServersView serversView = new ServersView();
		serversView.open();
		Server server = serversView.getServer(getServerFullName(serversView.getServers(), SERVER_NAME));
		server.clean();
		server.restart();
	}

	protected void checkForWarnings(Quickstart q) {
		for (String warning : ExamplesOperator.getInstance().getAllWarnings()) {
			reporter.addWarning(q, warning);
		}

	}

	protected void checkForErrors(Quickstart q) {
		updateProjectsIfNeeded();
		String errorMessages = "";
		List<String> allErrors = new ArrayList<String>();
		List<String> errors = ExamplesOperator.getInstance().getAllErrors();
		for (String error : errors) {
			if (!error.contains("Unable to delete") && !error.contains("Could not delete")) {
				reporter.addError(q, "ERROR IN PROJECT: " + error);
				errorMessages += "\t" + error + "\n";
				allErrors.add(error);
			}
		}
		if (!allErrors.isEmpty()) {
			fail("There are errors in imported project:\n" + errorMessages);
		}
	}

	protected boolean isError() {
		updateProjectsIfNeeded();
		if (!ExamplesOperator.getInstance().getAllErrors().isEmpty()) {
			return true;
		}
		return false;
	}

	protected void updateProjectsIfNeeded() {
		for (String string : ExamplesOperator.getInstance().getAllErrors()) {
			if (string.contains("not up-to-date with pom.xml")) { // maven
																	// update is
																	// needed
																	// sometimes.
				runUpdate();
			}
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

	protected static void deleteAllProjects() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		List<Project> projects = projectExplorer.getProjects();
		for (Project p : projects) {
			DeleteUtils.forceProjectDeletion(p, false);
		}
	}

	protected static void cleanupShells() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	protected void importQuickstart(Quickstart quickstart) throws NoProjectException {
		ExtendedMavenImportWizard mavenImportWizard = new ExtendedMavenImportWizard();
		mavenImportWizard.open();
		MavenImportWizardFirstPage wizPage = new MavenImportWizardFirstPage();
		try {
			wizPage.setRootDirectory(quickstart.getPath().getAbsolutePath());
		} catch (WaitTimeoutExpiredException e) {
			cleanupShells();
			throw new NoProjectException();
		}
		try {
			mavenImportWizard.finish();
		} catch (MavenImportWizardException e) {
			for (String error : e.getErrors()) {
				reporter.addError(quickstart, error);
			}
		}
	}

	private void importTestUtilsIfNeeded(Quickstart qstart) {
		for (String error : ExamplesOperator.getInstance().getAllErrors()) {
			if (error.contains("Missing artifact org.javaee7:test-utils")) {
				Quickstart testUtils = new Quickstart("test-utils",
						qstart.getPath().getAbsolutePath().replace(qstart.getName(), "test-utils"));
				importQuickstart(testUtils);
				break;
			}
			if (error.contains("Missing artifact org.javaee7:util")) {
				Quickstart testUtils = new Quickstart("util",
						qstart.getPath().getAbsolutePath().replace(qstart.getName(), "test-utils"));
				importQuickstart(testUtils);
				break;
			}
		}
	}

	protected static void closeBrowser() {
		try {
			BrowserEditor browser = new BrowserEditor(new RegexMatcher(".*"));
			while (browser != null) {
				browser.close();
				try {
					browser = new BrowserEditor(new RegexMatcher(".*"));
				} catch (CoreLayerException ex) {
					browser = null;
				}
			}
		} catch (CoreLayerException ex) {
			return;
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
	 * Extended maven import wizard. When super.finsh() fails, waits another 15
	 * minutes.
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

	class NoProjectException extends RedDeerException {

		public NoProjectException() {
			super("There is no project in this directory");
		}

	}

	/**
	 * Checks whether is project deployed properly.
	 * 
	 * @param projectName
	 * @param serverNameLabel
	 */

	public void checkDeployedProject(Quickstart qstart, String serverNameLabel) {
		if (!qstart.getName().contains("ejb-timer") && !qstart.getName().contains("cluster-ha-singleton")) {
			new WaitWhile(new JobIsRunning());
			new WaitUntil(new ConsoleHasNoChange(TimePeriod.LONG), TimePeriod.VERY_LONG);
		}
		JBossServerView serversView = new JBossServerView();
		serversView.open();
		String moduleName = qstart.getName().equals("template") ? "QUICKSTART_NAME" : qstart.getName();
		JBossServerModule module = (JBossServerModule) serversView.getServer(serverNameLabel)
				.getModule(new RegexMatcher(".*" + moduleName + ".*")); // cannot
																		// be
																		// used
																		// projectName
																		// -
																		// issues
																		// with
																		// parent
																		// projects
		if (new ContextMenu("Show In", "Web Browser").isEnabled()) {
			module.openWebPage();

			final BrowserEditor browser = new BrowserEditor(new RegexMatcher(".*"));
			try {
				new WaitUntil(new BrowserIsnotEmpty(browser));
			} catch (WaitTimeoutExpiredException e) {
				// try to refresh browser and wait one more time.
				browser.refreshPage();
				new WaitUntil(new BrowserIsnotEmpty(browser));
			}

			// Now the browser should not be empty. Let's check for error
			// messages
			// (strings like "404")
			checkBrowserForErrorPage(browser);
			assertNotEquals("", browser.getText());
			new DefaultEditor().close();

		}
		checkConsoleForException();
		checkServerViewForStatus(moduleName, serverNameLabel);

	}

	protected void checkServerViewForStatus(String projectName, String serverNameLabel) {
		ServersView serversView = new ServersView();
		serversView.open();
		Server server = serversView.getServer(serverNameLabel);
		ServerModule serverModule = server.getModule(new RegexMatcher(".*" + projectName + ".*"));
		ModuleLabel moduleLabel = serverModule.getLabel();
		ServerState moduleState = moduleLabel.getState();
		org.junit.Assert.assertTrue("Module has not been started!", moduleState == ServerState.STARTED);
	}

	protected void checkConsoleForException() {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		assertFalse("Console contains text 'Operation (\"deploy\") failed':\n" + consoleView.getConsoleText(),
				consoleView.getConsoleText().contains("Operation (\"deploy\") failed"));
	}

	protected void checkBrowserForErrorPage(BrowserEditor browser) {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		assertFalse("Browser contains text 'Status 404'\n Console output:\n" + consoleView.getConsoleText(),
				browser.getText().contains("Status 404") || browser.getText().contains("404 - Not Found"));
		assertFalse(
				"Browser contains text 'Error processing request'\n Console output:\n" + consoleView.getConsoleText(),
				browser.getText().contains("Error processing request"));
	}

	class BrowserIsnotEmpty extends AbstractWaitCondition {

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

	class ModuleStarted extends AbstractWaitCondition {

		String projectName;
		String serverNameLabel;

		public ModuleStarted(String projectName, String serverNameLabel) {
			this.projectName = projectName;
			this.serverNameLabel = serverNameLabel;
		}

		public boolean test() {
			ServersView serversView = new ServersView();
			serversView.open();
			Server server = serversView.getServer(serverNameLabel);
			ServerModule serverModule = server.getModule(new RegexMatcher(".*" + projectName + ".*"));
			ModuleLabel moduleLabel = serverModule.getLabel();
			ServerState moduleState = moduleLabel.getState();
			return moduleState == ServerState.STARTED;
		}

		public String description() {
			return "Module has not started!";
		}
	}

}