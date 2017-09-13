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
package org.jboss.tools.jsf.ui.test.project;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.jsf.ui.test.requirement.DoNotUseVPERequirement.DoNotUseVPE;
import org.jboss.tools.jsf.ui.test.utils.JSFTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

@RunWith(RedDeerSuite.class)
@DoNotUseVPE
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@JBossServer(state = ServerRequirementState.PRESENT)
public class CreateJSFProjectTest {

	protected static final String PROJECT_NAME_BASE = "JSFTestProject";

	private String jsfEnvironment;
	private String template;
	private String projectName;

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
	
	public CreateJSFProjectTest(String jsfEnvironment, String template) {
		this.jsfEnvironment = jsfEnvironment;
		this.template = template;
		this.projectName = (PROJECT_NAME_BASE + jsfEnvironment).replaceAll("\\s", "");
	}

	@Test
	public void createProjectTest() {

		JSFTestUtils.createJSFProject(projectName, jsfEnvironment, template);

		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		assertTrue(projectExplorer.containsProject(projectName));

		JSFTestUtils.checkProblemsView();

		JSFTestUtils.checkErrorLog();
	}
	
	@Before
	public void setup() {
		JSFTestUtils.deleteErrorLog();
	}

	@After
	public void teardown() {
		// delete test project
		ProjectExplorer projectExplorer = new ProjectExplorer();
		Project project = projectExplorer.getProject(projectName);
		project.delete(true);
	}

}
