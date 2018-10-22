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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.exception.RedDeerException;
import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.core.util.FileUtil;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.eclipse.reddeer.eclipse.core.resources.DefaultProject;
import org.eclipse.reddeer.eclipse.core.resources.MavenProject;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.exception.EclipseLayerException;
import org.eclipse.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.eclipse.reddeer.eclipse.ui.browser.BrowserEditor;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.log.LogMessage;
import org.eclipse.reddeer.eclipse.ui.views.log.LogView;
import org.eclipse.reddeer.eclipse.utils.DeleteUtils;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ModuleLabel;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServerModule;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerState;
import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.exception.WorkbenchLayerException;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerModule;
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
 * @author rhopp, jkopriva, vprusa
 *
 */

public abstract class AbstractImportQuickstartsTest {

	protected static String SERVER_NAME = "";
	protected QuickstartsReporter reporter = QuickstartsReporter.getInstance();
	protected String blacklistFileContents = "";
	protected JSONObject blacklistErrorsFileContents;
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
		ServersView2 serversView = new ServersView2();
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

		ServersView2 serversView = new ServersView2();
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
				if (new ContextMenuItem("Run As", "1 Run on Server").isEnabled()) {
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
		new ContextMenuItem("Run As", "1 Run on Server").select();
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
		try {
			for (File f : file.listFiles(directoryFilter)) {
				if (!f.getPath().contains("/.")) {
					if (specificQuickstarts.size() == 0 || specificQuickstarts.contains(f.getName())) {
						log.info("PROCESSING " + f.getAbsolutePath());
						Quickstart qstart = new Quickstart(f.getName(), f.getAbsolutePath());
						resultList.add(qstart);
					}
				}
			}
		} catch (NullPointerException ex) {
			fail("Please check path to quickstarts. Folder does not exist! " + file.getAbsolutePath());
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
		if (System.getProperty("deployOnServer") != null && System.getProperty("deployOnServer").equals("true")) {
			closeBrowser();
		}
		try {
			new ConsoleView().clearConsole();
		} catch (WorkbenchLayerException ex) {
			// Swallowing exception - ConsoleView is not opened
		}
	}

	protected void runQuickstarts(Quickstart qstart, String serverName, String blacklistFile) {
		runQuickstarts(qstart, serverName, blacklistFile, "");
	}

	protected void runQuickstarts(Quickstart qstart, String serverName, String blacklistFile,
			String blacklistErrorsFile) {
		loadBlacklistFile(blacklistFile);
		loadBlacklistErrorsFile(blacklistErrorsFile);
		if (!blacklistFileContents.contains(qstart.getName())) {
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
			mavenUpdate(qstart);
			log.info("Check for warnings and errors");
			checkForWarnings(qstart);
			if (blacklistErrorsFileContents == null || (!blacklistErrorsFileContents.containsKey(qstart.getName())
					&& !blacklistErrorsFileContents.containsKey("*"))) {

				checkForErrors(qstart);
				checkErrorLog(qstart);
			} else {
				log.info("Lets ignore known errors:");

				JSONArray errorsToIgnore = new JSONArray();
				if (blacklistErrorsFileContents.containsKey(qstart.getName())) {
					errorsToIgnore = (JSONArray) blacklistErrorsFileContents.get(qstart.getName());
				}
				if (blacklistErrorsFileContents.containsKey("*")) {
					JSONArray errorsToIgnoreForAll = (JSONArray) blacklistErrorsFileContents.get("*");
					for (Object o : errorsToIgnoreForAll) {
						errorsToIgnore.add(o);
					}
				}
				log.info(errorsToIgnore.toJSONString());

				List<String> errorsToIgnoreList = (List<String>) (List<?>) Arrays.asList(errorsToIgnore.toArray());
				checkForErrors(qstart, errorsToIgnoreList);
				checkErrorLog(qstart, errorsToIgnoreList);
			}
		}
	}

	private void loadBlacklistFile(String blacklistFile) {
		String pathToFile = "";
		try {
			pathToFile = new File(blacklistFile).getCanonicalPath();
			blacklistFileContents = FileUtil.readFile(pathToFile);
		} catch (IOException ex) {
			fail("Blacklist file not found! Path is: " + pathToFile);
		}
	}

	// https://www.mkyong.com/java/json-simple-example-read-and-write-json
	private void loadBlacklistErrorsFile(String blacklisterrorsFile) {
		if (blacklisterrorsFile.isEmpty()) {
			return;
		}
		String pathToFile = "";
		try {
			pathToFile = new File(blacklisterrorsFile).getCanonicalPath();
			JSONParser parser = new JSONParser();
			blacklistErrorsFileContents = (JSONObject) parser.parse(new FileReader(pathToFile));
		} catch (IOException ex) {
			fail("Blacklist file not found! Path is: " + pathToFile);
		} catch (ParseException e) {
			fail("ParseException: unable to parse file at ath is: " + pathToFile);
		}
	}

	protected static void createReports() {
		QuickstartsReporter.getInstance().generateReport();
		QuickstartsReporter.getInstance().generateErrorFilesForEachProject(new File("target/reports/"));
		QuickstartsReporter.getInstance().generateAllErrorsFile(new File("target/reports/allErrors.txt"));
	}

	protected void checkErrorLog(Quickstart qstart) {
		checkErrorLog(qstart, Collections.emptyList());
	}

	protected void checkErrorLog(Quickstart qstart, List<String> expectedErrorsRegexes) {
		List<LogMessage> allErrors = new ArrayList<LogMessage>();
		List<LogMessage> errors = errorLogView.getErrorMessages();
		String errorMessages = "";
		for (LogMessage message : errors) {
			if (!message.getMessage().contains("Unable to delete") && !message.getMessage().contains("Could not delete")
					&& !expectedErrorsRegexes.stream().filter(ee -> message.getMessage().matches(ee)).findAny()
							.isPresent()) {
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
		ServersView2 serversView = new ServersView2();
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
		checkForErrors(q, Collections.emptyList());
	}

	protected void checkForErrors(Quickstart q, List<String> expectedErrorsRegexes) {
		updateProjectsIfNeeded();
		String errorMessages = "";
		List<String> allErrors = new ArrayList<String>();
		List<String> errors = ExamplesOperator.getInstance().getAllErrors();
		for (String error : errors) {
			if (!error.contains("Unable to delete") && !error.contains("Could not delete")
					&& !expectedErrorsRegexes.stream().filter(ee -> error.matches(ee)).findAny().isPresent()) {
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
		new ContextMenuItem("Maven", "Update Project...").select();
		new DefaultShell("Update Maven Project");
		new PushButton("Select All").click();
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

	}

	protected static void deleteAllProjects() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		List<DefaultProject> projects = projectExplorer.getProjects();
		for (DefaultProject p : projects) {
			DeleteUtils.forceProjectDeletion(p, false);
		}
	}

	protected static void cleanupShells() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	protected void mavenUpdate(Quickstart quickstart) {
		ProjectExplorer pe = new ProjectExplorer();
		TreeItem projectItem;
		String quickstartBaseName = quickstart.getName();
		String quickstartBaseNameWF = "wildfly-" + quickstartBaseName;
		String quickstartBaseNameJB = "jboss-" + quickstartBaseName;

		if (pe.containsProject(quickstartBaseName)) {
			projectItem = pe.getProject(quickstartBaseName).getTreeItem();
		} else if (pe.containsProject(quickstartBaseNameWF)) {
			quickstart.setName(quickstartBaseNameWF);
			projectItem = pe.getProject(quickstart.getName()).getTreeItem();
		} else {
			quickstart.setName(quickstartBaseNameJB);
			projectItem = pe.getProject(quickstart.getName()).getTreeItem();
		}
		MavenProject project = new MavenProject(projectItem);
		project.updateMavenProject();
	}

	protected void importQuickstart(Quickstart quickstart) throws NoProjectException {
		if (!quickstartImported(quickstart)) {
			ExtendedMavenImportWizard mavenImportWizard = new ExtendedMavenImportWizard();
			mavenImportWizard.open();
			MavenImportWizardFirstPage wizPage = new MavenImportWizardFirstPage(mavenImportWizard);
			try {
				wizPage.setRootDirectory(quickstart.getPath().getAbsolutePath());
			} catch (WaitTimeoutExpiredException e) {
				cleanupShells();
				throw new NoProjectException();
			}
			try {
				mavenImportWizard.finish();
				mavenUpdate(quickstart);
			} catch (MavenImportWizardException e) {
				for (String error : e.getErrors()) {
					reporter.addError(quickstart, error);
				}
			}
		}
	}

	private boolean quickstartImported(Quickstart qstart) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		List<DefaultProject> projects = projectExplorer.getProjects();
		for (DefaultProject p : projects) {
			if (p.getName().equals(qstart.getName())) {
				return true;
			}
		}
		return false;
	}

	private void importTestUtilsIfNeeded(Quickstart qstart) {
		for (String error : ExamplesOperator.getInstance().getAllErrors()) {
			if (error.contains("Missing artifact org.javaee7:test-utils")) {
				Quickstart testUtils = new Quickstart("test-utils",
						qstart.getPath().getAbsolutePath().replaceAll(qstart.getName() + "$", "test-utils"));
				importQuickstart(testUtils);
				continue;
			}
			if (error.contains("Missing artifact org.javaee7:util")) {
				Quickstart util = new Quickstart("util",
						qstart.getPath().getAbsolutePath().replaceAll(qstart.getName() + "$", "util"));
				importQuickstart(util);
				continue;
			}
			if (error.contains("Missing artifact org.javaee7:jaspic-common")) {
				Quickstart jaspicCommon = new Quickstart("jaspic-common", qstart.getPath().getAbsolutePath()
						.replaceAll(qstart.getName() + "$", "jaspic" + File.separator + "common"));
				importQuickstart(jaspicCommon);
				continue;
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
		MavenSettingsPreferencePage prefPage = new MavenSettingsPreferencePage(preferenceDialog);
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
		ServersView2 serversView = new ServersView2();
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
		if (new ContextMenuItem("Show In", "Web Browser").isEnabled()) {
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
		ServersView2 serversView = new ServersView2();
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
			ServersView2 serversView = new ServersView2();
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