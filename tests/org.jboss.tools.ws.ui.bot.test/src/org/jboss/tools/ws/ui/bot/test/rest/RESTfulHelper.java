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
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsDescriptionMatcher;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsPathMatcher;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsTypeMatcher;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulLabel;
import org.jboss.tools.ws.reddeer.swt.condition.ProblemsCount;
import org.jboss.tools.ws.reddeer.ui.preferences.JAXRSValidatorPreferencePage;

public class RESTfulHelper {

	public static final String PATH_PARAM_VALID_ERROR = "@PathParam value";
	public static final String JAX_RS_PROBLEM = "JAX-RS Problem";

	private final TimePeriod WAIT_FOR_PROBLEMS_FALSE_POSItIVE_TIMEOUT = TimePeriod.getCustom(2);
	private final TimePeriod WAIT_FOR_PROBLEMS_FALSE_NEGATIVE_TIMEOUT = TimePeriod.getCustom(5);

	public List<Problem> getRESTValidationErrors(int expectedCount) {
		return getRESTValidationErrors(null, null, expectedCount);
	}

	public List<Problem> getRESTValidationErrors(String wsProjectName, int expectedCount) {
		return getRESTValidationErrors(wsProjectName, null, expectedCount);
	}

	public List<Problem> getRESTValidationErrors(String wsProjectName, String description, int expectedCount) {
		Matcher<String> descriptionMatcher = description != null ? StringContains.containsString(description) : null;
		Matcher<String> pathMatcher = wsProjectName != null ? StringStartsWith.startsWith("/" + wsProjectName) : null;

		/* wait for jax-rs validation */
		if(expectedCount == 0 && !new ProblemExists(ProblemType.ANY).test()) {//prevent from false positive result when we do not expect errors and there is no error
			new WaitWhile(new ProblemsCount(ProblemType.ERROR, expectedCount, descriptionMatcher, null,
					pathMatcher, null, null), WAIT_FOR_PROBLEMS_FALSE_POSItIVE_TIMEOUT, false);
		} else {//prevent from false negative result
			new WaitUntil(new ProblemsCount(ProblemType.ERROR, expectedCount, descriptionMatcher, null,
					pathMatcher, null, null), WAIT_FOR_PROBLEMS_FALSE_NEGATIVE_TIMEOUT, false);
		}

		/* return jax-rs validation errors */
		return new ProblemsView().getProblems(ProblemType.ERROR,
			new ProblemsDescriptionMatcher(descriptionMatcher),
			new ProblemsPathMatcher(pathMatcher),
			new ProblemsTypeMatcher(Is.is(JAX_RS_PROBLEM)));
	}

	public List<Problem> getRESTValidationWarnings(int expectedCount) {
		return getRESTValidationWarnings(null, null, expectedCount);
	}

	public List<Problem> getRESTValidationWarnings(String wsProjectName, int expectedCount) {
		return getRESTValidationWarnings(wsProjectName, null, expectedCount);
	}

	public List<Problem> getRESTValidationWarnings(String wsProjectName,
			String description, int expectedCount) {
		Matcher<String> descriptionMatcher = description != null ? StringContains.containsString(description) : null;
		Matcher<String> pathMatcher = wsProjectName != null ? StringStartsWith.startsWith("/" + wsProjectName) : null;

		/* wait for warnings */
		if(expectedCount == 0) {//prevent from false-positive
			new WaitWhile(new ProblemsCount(ProblemType.WARNING, expectedCount, descriptionMatcher, null, pathMatcher, null, Is.is(JAX_RS_PROBLEM)), WAIT_FOR_PROBLEMS_FALSE_POSItIVE_TIMEOUT, false);
		} else {//prevent from false-negative
			new WaitUntil(new ProblemsCount(ProblemType.WARNING, expectedCount, descriptionMatcher, null, pathMatcher, null, Is.is(JAX_RS_PROBLEM)), WAIT_FOR_PROBLEMS_FALSE_NEGATIVE_TIMEOUT, false);
		}

		/* return jax-rs validation warnings */
		return new ProblemsView().getProblems(ProblemType.WARNING,
				new ProblemsDescriptionMatcher(descriptionMatcher),
				new ProblemsPathMatcher(pathMatcher),
				new ProblemsTypeMatcher(Is.is(JAX_RS_PROBLEM)));
	}

	public List<Problem> getPathAnnotationValidationErrors(String wsProjectName, int expectedCount) {
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
		return project.containsItem(RESTfulLabel.REST_WS_NODE)
				|| project.containsItem(RESTfulLabel.REST_WS_BUILD_NODE);
	}

	private void configureRestSupport(String wsProjectName,
			boolean enableRestSupport) {
		new ProjectExplorer().getProject(wsProjectName).select();

		Menu menu = new ContextMenu(
				"Configure",
				enableRestSupport ? RESTfulLabel.ADD_REST_SUPPORT
						: RESTfulLabel.REMOVE_REST_SUPPORT);
		menu.select();

		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
	}
}
