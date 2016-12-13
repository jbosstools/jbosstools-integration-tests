/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.wstester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.core.Is;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebServicesNode;
import org.jboss.tools.ws.reddeer.ui.dialogs.WSTesterParametersDialog;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests WS Parameters dialog which is invoked when parameters values 
 * need to be set
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.RUNNING, cleanup=false)
public class WSTesterPromptValuesSupportTest extends RESTfulTestBase {

	private String wsProjectName = "wsPromptTestProject";

	private WsTesterView testerView;

	private WSTesterParametersDialog dialog;

	private RESTfulWebServicesNode restWebServicesNode;

	@Override
	public void setup() {
		if (!projectExists(wsProjectName)) {
			importWSTestProject(wsProjectName);
		}
		ServersViewHelper.runProjectOnServer(wsProjectName);
		ServersViewHelper.waitForDeployment(wsProjectName, getConfiguredServerName());
		testerView = new WsTesterView();
		testerView.open();
	}

	@Override
	public void cleanup() {
		if (dialog != null && dialog.isOpened()) {
			dialog.cancel();
		}
		if (testerView != null && testerView.isOpened()) {
			testerView.close();
		}
		ServersViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
	}

	@AfterClass
	public static void cleanEnvironment() {
		deleteAllProjects();
	}

	@Override
	protected String getWsProjectName() {
		return wsProjectName;
	}

	/**
	 * Tests if the parameter dialog can be invoked in WS Tester
	 */
	@Test
	public void testInvokeWsParameterDialog() {
		invokeWSParametersDialog();
	}

	/**
	 * Fails due to JBIDE-12027/JBIDE-22377
	 *  
	 * Tests if the parameters were loaded with the specified type
	 * and default values
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-12027
	 */
	@Test(expected=AssertionError.class)
	public void testParameters() {
		invokeWSParametersDialog();
		checkWSParametersDialog();
	}

	/**
	 * Tests the response
	 */
	@Test
	public void testResponse() {
		invokeWSParametersDialog();

		List<TreeItem> parameters = dialog.getAllParameters();
		setParametersValues(parameters);
		dialog.ok();

		checkWSResponse();
	}

	private void invokeWSParametersDialog() {
		restWebServicesNode = new RESTfulWebServicesNode(getWsProjectName());

		runRestServiceOnServer(restWebServicesNode.getWebServices().get(0));

		testerView.open();
		testerView.invoke();

		new WaitUntil(new ShellWithTextIsAvailable(WSTesterParametersDialog.DIALOG_TITLE));

		dialog = new WSTesterParametersDialog();
	}

	private void checkWSParametersDialog() {
		//button is always enabled until JBIDE-13111 is resolved
		//@see https://issues.jboss.org/browse/JBIDE-13111
//		assertThat(dialog.isOkButtonEnabled(), Is.is(false));

		List<TreeItem> parameters = dialog.getAllParameters();
		assertThat(dialog.getAllParameters().size(), Is.is(3));

		checkAllParametersWereLoaded(parameters);
		checkAllDefaultParametersValues(parameters);
		checkAllParametersTypes(parameters);
		checkThereIsMandatoryValueWarning();

		setParametersValues(parameters);

		checkThereIsNoWarning();

		assertThat(dialog.isOkButtonEnabled(), Is.is(true));

		dialog.ok();
	}

	private void checkThereIsNoWarning() {
		String warning = getWarningText();
		assertTrue("There is unexpected warning: " + warning, warning.length() == 0);
	}

	private void checkThereIsMandatoryValueWarning() {
		assertThat(getWarningText(), Is.is(" id's value is mandatory but missing."));
	}

	private String getWarningText() {
		return new DefaultText(0).getText();
	}

	private void checkAllParametersWereLoaded(List<TreeItem> parameters) {

		assertThat("First parameter name is wrong", 
				dialog.getParameterName(parameters.get(0)), Is.is("id* "));
		assertThat("Second parameter name is wrong", 
				dialog.getParameterName(parameters.get(1)), Is.is("m1"));
		assertThat("Third parameter name is wrong",
				dialog.getParameterName(parameters.get(2)), Is.is("q1"));
	}

	private void checkAllDefaultParametersValues(List<TreeItem> parameters) {
		assertThat("JBIDE-12027/JBIDE-22377: Default value of id is wrong", 
				dialog.getParameterValue(parameters.get(0)), Is.is("0"));
		assertThat("Default value of m1 is wrong", 
				dialog.getParameterValue(parameters.get(1)), Is.is("m1"));
		assertThat("Default value of q1 is wrong", 
				dialog.getParameterValue(parameters.get(2)), Is.is("q1"));
	}

	private void checkAllParametersTypes(List<TreeItem> parameters) {
		assertThat("Type of id is wrong", 
				dialog.getParameterType(parameters.get(0)), Is.is("Integer"));
		assertThat("Type of m1 is wrong", 
				dialog.getParameterType(parameters.get(1)), Is.is("String"));
		assertThat("Type of q1 is wrong", 
				dialog.getParameterType(parameters.get(2)), Is.is("String"));
	}

	private void setParametersValues(List<TreeItem> parameters) {
		dialog.setParameterValue(parameters.get(0), "1");
		dialog.setParameterValue(parameters.get(1), "matrix");
		dialog.setParameterValue(parameters.get(2), "query");
		
	}

	private void checkWSResponse() {
		assertThat("JAX-RS Response on GET method is wrong", 
				testerView.getResponseBody(), Is.is("1 matrix query"));
		
	}
}
