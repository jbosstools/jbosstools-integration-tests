/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.utils.CentralUtils;
import org.jboss.tools.common.launcher.reddeer.wizards.NewLauncherProjectWizard;
import org.jboss.tools.common.launcher.reddeer.wizards.NewLauncherProjectWizardPage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author zcervink
 * @see https://issues.jboss.org/browse/JBIDE-26520
 *
 */
@RunWith(RedDeerSuite.class)
public class LauncherApplicationTest {

	private static final String CENTRAL_LABEL = "Red Hat Central";
	private static final Logger log = Logger.getLogger(ExamplesOperator.class);
	private static final String BLACKLIST_ERRORS_REGEXES_FILE = "resources/blacklist-launcher-application-errors-regexes.json";
	private JSONObject blacklistErrorsFileContents;
	

	@BeforeClass
	public static void setup() {
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_LABEL).click();
		// activate central editor
		new DefaultEditor(CENTRAL_LABEL);
	}

	@After
	public void teardown() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_LABEL).click();
		// activate central editor
		new DefaultEditor(CENTRAL_LABEL);
	}

	@Test
	public void createNewLauncherApplicationProjectWithDefaultSettings() {
		log.step("Import project start");
		
		NewLauncherProjectWizard wizard = new NewLauncherProjectWizard();
		NewLauncherProjectWizardPage wizardPage = new NewLauncherProjectWizardPage(wizard);
		
		wizard.openWizardFromShellMenu();
		wizardPage.setProjectName("project_1_default_settings");

		wizard.finish(TimePeriod.getCustom(2500));
		log.step("Import project finished");

		checkForErrors();
	}

	@Test
	public void createNewLauncherApplicationProjectWithCustomizedSettings() {
		log.step("Import project start");

		NewLauncherProjectWizard wizard = new NewLauncherProjectWizard();	
		NewLauncherProjectWizardPage wizardPage = new NewLauncherProjectWizardPage(wizard);
		
		wizard.openWizardFromShellMenu();

		wizardPage.setTargetMission("rest-http");
		wizardPage.setTargetRuntime("vert.x redhat");
		wizardPage.setProjectName("project_2_customized_settings");
		wizardPage.toggleUseDefaultLocationCheckBox(false);
		wizardPage.setCustomProjectLocation(System.getProperty("user.home") + File.separator + "customFolder");
		wizardPage.setArtifactId("my_customized_artifact_id");
		wizardPage.setGroupId("my.customized.group.id");
		wizardPage.setVersion("1.2.3-SNAPSHOT");

		wizard.finish(TimePeriod.getCustom(2500));
		log.step("Import project finished");

		checkForErrors();
	}

	@Test
	public void testFinishButtonDisabledInLauncherApplicationProjectWizard() {
		log.step("Import project start");

		NewLauncherProjectWizard wizard = new NewLauncherProjectWizard();	
		NewLauncherProjectWizardPage wizardPage = new NewLauncherProjectWizardPage(wizard);
		wizard.openWizardFromShellMenu();

		// PROJECT NAME not filled test
		assertFalse(wizard.isFinishEnabled());

		// LOCATION not filled test
		wizardPage.setProjectName("project_2_customized_settings");
		String defaultLocationBackup = "";
		wizardPage.toggleUseDefaultLocationCheckBox(false);
		defaultLocationBackup = wizardPage.getProjectLocation();
		wizardPage.setCustomProjectLocation("");
		assertFalse(wizard.isFinishEnabled());

		// ARTIFACT ID not filled test
		wizardPage.setCustomProjectLocation(defaultLocationBackup);
		wizardPage.toggleUseDefaultLocationCheckBox(true);
		String defaultArtifactIdBackup = "";
		defaultArtifactIdBackup = wizardPage.getArtifactId();
		wizardPage.setArtifactId("");
		assertFalse(wizard.isFinishEnabled());

		// GROUP ID not filled test
		wizardPage.setArtifactId(defaultArtifactIdBackup);
		String defaultGroupIdBackup = "";
		defaultGroupIdBackup = wizardPage.getGroupId();
		wizardPage.setGroupId("");
		assertFalse(wizard.isFinishEnabled());

		// VERSION not filled test
		wizardPage.setGroupId(defaultGroupIdBackup);
		String defaultVersionBackup = "";
		defaultVersionBackup = wizardPage.getVersion();
		wizardPage.setVersion("");
		assertFalse(wizard.isFinishEnabled());
		wizardPage.setVersion(defaultVersionBackup);

		// close the wizard
		new DefaultShell("New Launcher project").close();
		log.step("Window closed");

		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		checkForErrors();
	}

	/**
	 * Checks for errors in Problems View. Fails if there is some
	 */
	public void checkForErrors() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<Problem> errors = problemsView.getProblems(ProblemType.ERROR);
		boolean atLeastOneErrorFound = false;

		if (!errors.isEmpty()) {
			this.blacklistErrorsFileContents = CentralUtils.loadBlacklistErrorsFile(BLACKLIST_ERRORS_REGEXES_FILE);
			String failureMessage = "There are errors after importing project";
			for (Problem problem : errors) {

				if (!isThisErrorInBlacklist(problem.getDescription())) {
					atLeastOneErrorFound = true;
					failureMessage += problem.getDescription();
					failureMessage += System.getProperty("line.separator");
				}
			}

			if (atLeastOneErrorFound) {
				fail(failureMessage);
			};
		}
	}

	/**
	 * Checks if the given error description is in the regex blacklist
	 */
	private boolean isThisErrorInBlacklist(String errorDescription) {
		JSONArray regexArray = (JSONArray)(blacklistErrorsFileContents).get("LauncherApplicationTest");

		for (int i=0; i<regexArray.size(); i++) {
			if (Pattern.compile((String)regexArray.get(i)).matcher(errorDescription).find()) {
				return true;
			}
		}

		return false;
	}
}
