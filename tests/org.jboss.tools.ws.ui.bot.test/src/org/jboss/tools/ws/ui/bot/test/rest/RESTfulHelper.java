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

import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringStartsWith;
import org.jboss.reddeer.eclipse.condition.ProblemsExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestFullLabels;
import org.jboss.tools.ws.reddeer.swt.condition.ProblemsCount;
import org.jboss.tools.ws.reddeer.ui.preferences.JAXRSValidatorPreferencePage;

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
		dialog.open();
		JAXRSValidatorPreferencePage page = new JAXRSValidatorPreferencePage();
		dialog.select(page);

		page.setEnableValidation(enableRestSupport);
		
		page.apply();
		
		new WaitUntil(new ShellWithTextIsAvailable("Validator Settings Changed"), TimePeriod.SHORT, false);
		if(new ShellWithTextIsAvailable("Validator Settings Changed").test()) {
			new DefaultShell("Validator Settings Changed");
			new PushButton("Yes").click();
			new WaitWhile(new ShellWithTextIsAvailable("Validator Settings Changed"), TimePeriod.NORMAL);
		}

		dialog.ok();
		
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
