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

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.core.Is;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RESTFullExplorer;
import org.jboss.tools.ws.ui.bot.test.uiutils.RunOnServerDialog;
import org.jboss.tools.ws.ui.bot.test.uiutils.WSTesterParametersDialog;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView;
import org.junit.Test;

/**
 * Tests WS Parameters dialog which is invoked when parameters values 
 * need to be set
 * 
 * @author jjankovi
 *
 */
@Require(server = @Server(state = ServerState.Running))
public class WSTesterPromptValuesSupportTest extends RESTfulTestBase {

	private String wsProjectName = "wsPromptTestProject";
	
	private WsTesterView testerView = new WsTesterView();
	
	private WSTesterParametersDialog dialog;
	
	private RESTFullExplorer restExplorer;
	
	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			importRestWSProject(getWsProjectName());
			jbt.runProjectOnServer(getWsProjectName());
			testerView.show();
		}
	}
	
	@Override
	public void cleanup() {
		if (dialog.isOpened()) dialog.cancel();	
		projectExplorer.deleteAllProjects();
	}
	
	@Override
	protected String getWsProjectName() {
		return wsProjectName;
	}
	
	@Test
	public void testWSParametersDialog() {
		
		invokeWSParametersDialog();
		checkWSParametersDialog();
		checkWSResponse();
		
	}
	
	private void invokeWSParametersDialog() {
		restExplorer = new RESTFullExplorer(getWsProjectName());
		
		RunOnServerDialog dialog = restExplorer.runOnServer(
				restExplorer.getAllRestServices()[0]);
		dialog.chooseExistingServer();
		dialog.finish();
		
		testerView.show();
		testerView.invoke();
		
		bot.waitUntil(new ShellIsActiveCondition(
						WSTesterParametersDialog.DIALOG_TITLE));
	}
	
	private void checkWSParametersDialog() {
		
		dialog = new WSTesterParametersDialog();
		
		assertThat(dialog.isOkButtonEnabled(), Is.is(false));
		
		SWTBotTreeItem[] parameters = dialog.getAllParameters();
		assertThat(dialog.getAllParameters().length, Is.is(3));
		
		checkAllParametersWereLoaded(parameters);
		checkAllDefaultParametersValues(parameters);
		checkAllParametersTypes(parameters);
		setParametersValues(parameters);
		
		assertThat(dialog.isOkButtonEnabled(), Is.is(true));
		
		dialog.ok();
	}

	private void checkAllParametersWereLoaded(SWTBotTreeItem[] parameters) {
		
		assertThat("First parameter name is wrong", 
				dialog.getParameterName(parameters[0]), Is.is("id"));
		assertThat("Second parameter name is wrong", 
				dialog.getParameterName(parameters[1]), Is.is("m1"));
		assertThat("Third parameter name is wrong",
				dialog.getParameterName(parameters[2]), Is.is("q1"));
		
	}
	
	private void checkAllDefaultParametersValues(SWTBotTreeItem[] parameters) {
		
		assertThat("Default value of id is wrong", 
				dialog.getParameterValue(parameters[0]), Is.is("0"));
		assertThat("Default value of id is wrong", 
				dialog.getParameterValue(parameters[1]), Is.is("m1"));
		assertThat("Default value of id is wrong", 
				dialog.getParameterValue(parameters[2]), Is.is("q1"));
		
	}
	
	private void checkAllParametersTypes(SWTBotTreeItem[] parameters) {
		
		assertThat("Type of id is wrong", 
				dialog.getParameterType(parameters[0]), Is.is("java.lang.Integer"));
		assertThat("Type of m1 is wrong", 
				dialog.getParameterType(parameters[1]), Is.is("java.lang.String"));
		assertThat("Type of q1 is wrong", 
				dialog.getParameterType(parameters[2]), Is.is("java.lang.String"));
		
	}
	
	private void setParametersValues(SWTBotTreeItem[] parameters) {
		
		dialog.setParameterValue(parameters[0], "1");
		dialog.setParameterValue(parameters[1], "matrix");
		dialog.setParameterValue(parameters[2], "query");
		
	}
	
	private void checkWSResponse() {
		
		assertThat("JAX-RS Response on GET method is wrong", 
				testerView.getResponseBody(), Is.is("1 matrix query"));
		
	}
	
}
