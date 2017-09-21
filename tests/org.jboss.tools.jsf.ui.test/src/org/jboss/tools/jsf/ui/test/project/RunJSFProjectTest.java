/*******************************************************************************
O * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.test.project;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServerModule;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerPublishState;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerState;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.jsf.ui.test.requirement.DoNotUseVPERequirement.DoNotUseVPE;
import org.jboss.tools.jsf.ui.test.utils.JSFTestUtils;
import org.jboss.tools.jsf.ui.test.utils.ModuleIsInState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

@RunWith(RedDeerSuite.class)
@DoNotUseVPE
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@JBossServer(state = ServerRequirementState.RUNNING)
public class RunJSFProjectTest {

	protected static final String PROJECT_NAME_BASE = "JSFTestProject";

	private String jsfEnvironment;
	private String template;
	private String projectName;

	@InjectRequirement
	ServerRequirement serverReq;

	@Parameters(name = "{0} {1}")
	public static Collection<String[]> data() {
		return Arrays.asList(
				new String[][] {{"JSF 1.2", "JSFKickStartWithoutLibs"}, {"JSF 2.2", "JSFKickStartWithoutLibs"},
						{"JSF 1.2 with Facelets", "FaceletsKickStartWithoutLibs"}});
	}

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.EAP());
	}
	
	public RunJSFProjectTest(String jsfEnvironment, String template) {
		this.jsfEnvironment = jsfEnvironment;
		this.template = template;
		this.projectName = (PROJECT_NAME_BASE + jsfEnvironment).replaceAll("\\s", "");
	}

	@Before
	public void setup() {
		JSFTestUtils.createJSFProject(projectName, jsfEnvironment, template, false);
		JSFTestUtils.deleteErrorLog();
	}

	@After
	public void teardown() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		Project project = projectExplorer.getProject(projectName);
		project.delete(true);
		JSFTestUtils.checkErrorLog();
	}

	@Test
	public void deployProjectTest() {
		deployProject();

		checkDeployedProject();
	}

	private void checkDeployedProject() {
		Server server = getServer();
		ServerModule module = server.getModule(projectName);
		ModuleIsInState moduleIsInStateCondition = new ModuleIsInState(ServerState.STARTED,
				ServerPublishState.SYNCHRONIZED, module);
		try {
			new WaitUntil(moduleIsInStateCondition, TimePeriod.DEFAULT);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Module is not in state STARTED, SYNCHRONIZED");
		}
	}

	private void deployProject() {
		Server server = getServer();
		ModifyModulesDialog addAndRemoveModulesDialog = server.addAndRemoveModules();
		ModifyModulesPage modifyModulesPage = new ModifyModulesPage(addAndRemoveModulesDialog);
		modifyModulesPage.add(projectName);
		addAndRemoveModulesDialog.finish();
	}

	private Server getServer() {
		ServersView2 serversView = new ServersView2();
		return serversView.getServer(serverReq.getServerName());
	}
}
