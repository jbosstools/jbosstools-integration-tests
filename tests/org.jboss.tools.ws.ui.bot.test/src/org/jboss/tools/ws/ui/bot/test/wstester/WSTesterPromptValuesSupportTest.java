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

import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.core.Is;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ws.reddeer.ui.dialogs.WSTesterParametersDialog;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RESTFullExplorer;
import org.jboss.tools.ws.ui.bot.test.uiutils.RunOnServerDialog;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests WS Parameters dialog which is invoked when parameters values 
 * need to be set
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 */
@Require(server = @Server(state = ServerState.Running))
public class WSTesterPromptValuesSupportTest extends RESTfulTestBase {

	private String wsProjectName = "wsPromptTestProject";
	
	private WsTesterView testerView = new WsTesterView();
	
	private WSTesterParametersDialog dialog;
	
	private RESTFullExplorer restExplorer;
	
	@Override
	public void setup() {
		if (!projectExists(wsProjectName)) {
			importRestWSProject(wsProjectName);
			jbt.runProjectOnServer(wsProjectName);
			testerView.show();
		}
	}
	
	@Override
	public void cleanup() {
		if (dialog.isOpened()) {
			dialog.cancel();
		}
	}
	
	@AfterClass
	public static void cleanEnvironment() {
		projectExplorer.deleteAllProjects();
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
	 * Fails due to JBIDE-12027 and JBIDE-13546
	 * 
	 * (JBIDE-13111 OK button is always enabled - see comment)
	 * 
	 * Tests if the parameters were loaded with the specified type
	 * and default values
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-12027
	 * @see https://issues.jboss.org/browse/JBIDE-13546
	 * @see https://issues.jboss.org/browse/JBIDE-13111
	 */
	@Test
	public void testParameters() {
		invokeWSParametersDialog();
		checkWSParametersDialog();
	}
	
	@Ignore
	@Test
	public void testValueTypeChecking() {
		//TODO
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
		restExplorer = new RESTFullExplorer(getWsProjectName());
		
		RunOnServerDialog dialog = restExplorer.runOnServer(
				restExplorer.getAllRestServices().get(0));
		dialog.chooseExistingServer();
		dialog.finish();
		
		testerView.show();
		testerView.invoke();
		
		bot.waitUntil(new ShellIsActiveCondition(
						WSTesterParametersDialog.DIALOG_TITLE));
		
		this.dialog = new WSTesterParametersDialog();
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
		
		assertThat("JBIDE-12027: Default value of id is wrong", 
				dialog.getParameterValue(parameters.get(0)), Is.is("0"));
		assertThat("Default value of id is wrong", 
				dialog.getParameterValue(parameters.get(1)), Is.is("m1"));
		assertThat("Default value of id is wrong", 
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
