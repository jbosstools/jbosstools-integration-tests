/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringStartsWith;
import org.jboss.reddeer.eclipse.condition.ProblemsExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.helper.BuildPathHelper;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestFullLabels;
import org.jboss.tools.ws.reddeer.swt.condition.ProblemsCount;
import org.jboss.tools.ws.reddeer.ui.preferences.JAXRSValidatorPreferencePage;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;

public class RESTfulHelper {

	public static final String PATH_PARAM_VALID_ERROR = "@PathParam value";
	public static final String JAX_RS_PROBLEM = "JAX-RS Problem";

	private final TimePeriod WAIT_FOR_PROBLEMS_FALSE_POSItIVE_TIMEOUT = TimePeriod.getCustom(2);
	private final TimePeriod WAIT_FOR_PROBLEMS_FALSE_NEGATIVE_TIMEOUT = TimePeriod.getCustom(5);

	public List<TreeItem> getRESTValidationErrors(int expectedCount) {
		return getRESTValidationErrors(null, null, expectedCount);
	}

	public List<TreeItem> getRESTValidationErrors(String wsProjectName, int expectedCount) {
		return getRESTValidationErrors(wsProjectName, null, expectedCount);
	}

	public List<TreeItem> getRESTValidationErrors(String wsProjectName, String description, int expectedCount) {
		Matcher<String> descriptionMatcher = description != null ? StringContains.containsString(description) : null;
		Matcher<String> pathMatcher = wsProjectName != null ? StringStartsWith.startsWith("/" + wsProjectName) : null;

		/* wait for jax-rs validation */
		if(expectedCount == 0 && !new ProblemsExists().test()) {//prevent from false positive result when we do not expect errors and there is no error
			new WaitWhile(new ProblemsCount(ProblemsCount.ProblemType.ERROR, expectedCount, descriptionMatcher, null,
					pathMatcher, null, null), WAIT_FOR_PROBLEMS_FALSE_POSItIVE_TIMEOUT, false);
		} else {//prevent from false negative result
			new WaitUntil(new ProblemsCount(ProblemsCount.ProblemType.ERROR, expectedCount, descriptionMatcher, null,
					pathMatcher, null, null), WAIT_FOR_PROBLEMS_FALSE_NEGATIVE_TIMEOUT, false);
		}

		/* return jax-rs validation errors */
		return new ProblemsView().getErrors(descriptionMatcher, null,
				pathMatcher, null,Is.is(JAX_RS_PROBLEM));
	}

	public List<TreeItem> getRESTValidationWarnings(int expectedCount) {
		return getRESTValidationWarnings(null, null, expectedCount);
	}

	public List<TreeItem> getRESTValidationWarnings(String wsProjectName, int expectedCount) {
		return getRESTValidationWarnings(wsProjectName, null, expectedCount);
	}

	public List<TreeItem> getRESTValidationWarnings(String wsProjectName,
			String description, int expectedCount) {
		Matcher<String> descriptionMatcher = description != null ? StringContains.containsString(description) : null;
		Matcher<String> pathMatcher = wsProjectName != null ? StringStartsWith.startsWith("/" + wsProjectName) : null;

		/* wait for warnings */
		if(expectedCount == 0) {//prevent from false-positive
			new WaitWhile(new ProblemsCount(ProblemsCount.ProblemType.WARNING, expectedCount, descriptionMatcher, null, pathMatcher, null, Is.is(JAX_RS_PROBLEM)), WAIT_FOR_PROBLEMS_FALSE_POSItIVE_TIMEOUT, false);
		} else {//prevent from false-negative
			new WaitUntil(new ProblemsCount(ProblemsCount.ProblemType.WARNING, expectedCount, descriptionMatcher, null, pathMatcher, null, Is.is(JAX_RS_PROBLEM)), WAIT_FOR_PROBLEMS_FALSE_NEGATIVE_TIMEOUT, false);
		}

		/* return jax-rs validation warnings */
		return new ProblemsView().getWarnings(descriptionMatcher, null,
				pathMatcher, null, Is.is(JAX_RS_PROBLEM));
	}

	public List<TreeItem> getPathAnnotationValidationErrors(String wsProjectName, int expectedCount) {
		return getRESTValidationErrors(wsProjectName, PATH_PARAM_VALID_ERROR, expectedCount);
	}

	public void enableRESTValidation() {
		modifyRESTValidation(true);
	}

	public void disableRESTValidation() {
		modifyRESTValidation(false);
	}

	public void modifyRESTValidation(boolean enableRestSupport) {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		JAXRSValidatorPreferencePage page = new JAXRSValidatorPreferencePage();
		dialog.select(page);

		page.setEnableValidation(enableRestSupport);

		page.ok();

		if(new ShellWithTextIsActive("Validator Settings Changed").test()) {
			new PushButton("Yes").click();
		}

		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20), false);
	}

	public void addRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, true);
	}

	public void removeRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, false);
	}

	public boolean isRestSupportEnabled(String wsProjectName) {
		Project project = new ProjectExplorer().getProject(wsProjectName);
		return project.containsItem(RestFullLabels.REST_EXPLORER_LABEL.getLabel())
				|| project.containsItem(RestFullLabels.REST_EXPLORER_LABEL_BUILD.getLabel());
	}

	@SuppressWarnings("static-access")
	public List<String> addRestEasyLibs(String wsProjectName) {
		List<File> restLibsPaths = getPathForRestLibs();
		
		List<String> variables = new ArrayList<String>();
		BuildPathHelper buildPathHelper = new BuildPathHelper();
		
		for (File f : restLibsPaths) {
			variables.add(buildPathHelper.addExternalJar(f.getPath(), wsProjectName, true));
		}
		
		return variables;
	}

	private List<File> getPathForRestLibs() {
		assertTrue(TestConfigurator.currentConfig.getServer().type.equals("EAP"));

		String runtimeHome = TestConfigurator.currentConfig.getServer().runtimeHome;

		// index of last occurence of "/" in EAP runtime path: jboss-eap-5.1/jboss-as
		int indexOfAS = runtimeHome.lastIndexOf("/");

		// jboss-eap-5.1/jboss-as --> jboss-eap-5.1
		String eapDirHome = runtimeHome.substring(0, indexOfAS);

		String restEasyDirPath = eapDirHome + "/" + "resteasy";
		File restEasyDir = new File(restEasyDirPath);

		String[] restEasyLibs = {"jaxrs-api.jar"};

		return new ResourceHelper().searchAllFiles(restEasyDir, restEasyLibs);
	}

	private void configureRestSupport(String wsProjectName,
			boolean enableRestSupport) {
		new ProjectExplorer().getProject(wsProjectName).select();

		Menu menu = new ContextMenu(
				"Configure",
				enableRestSupport ? RestFullLabels.REST_SUPPORT_MENU_LABEL_ADD.getLabel()
						: RestFullLabels.REST_SUPPORT_MENU_LABEL_REMOVE.getLabel());
		menu.select();

		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
	}
}
