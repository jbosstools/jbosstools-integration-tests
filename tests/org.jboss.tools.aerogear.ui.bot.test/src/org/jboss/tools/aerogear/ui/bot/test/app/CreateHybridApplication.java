/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.app;

import static org.junit.Assert.*;

import java.util.List;
import java.io.IOException;
import java.util.Arrays;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.util.FileUtil;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsPathMatcher;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

@CleanWorkspace
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class CreateHybridApplication extends AerogearBotTest {

	private String engine;
	private String platform;
	private String cordovaVersion;

	@Parameters(name = "engine: {0}")
	public static String[] data() {
		return new String[] { "cordova-android@3.6.4", "cordova-android@3.7.2", "cordova-android@4.0.0",
				"cordova-android@4.1.1" };
	}

	public CreateHybridApplication(String engine) {
		this.engine = engine;
		this.platform = engine.substring("cordova-".length(), engine.indexOf("@"));
		this.cordovaVersion = engine.substring(engine.indexOf("@") + 1, engine.length());
	}

	@Before
	public void setUp() {
		createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME, AerogearBotTest.CORDOVA_APP_NAME,
				"org.jboss.example.cordova", this.engine);

		assertTrue(new ProjectExplorer().containsProject(AerogearBotTest.CORDOVA_PROJECT_NAME));
	}

	@Test
	public void testEngineVersionMatchesInConfigXML() throws IOException {
		String xmlConfig = FileUtil.readFile(WS_PATH + "/" + CORDOVA_PROJECT_NAME + "/config.xml");
		boolean oldSpec = xmlConfig
				.contains("<engine name=\"" + this.platform + "\" version=\"" + this.cordovaVersion + "\" />");
		boolean newSpec = xmlConfig
				.contains("<engine name=\"" + this.platform + "\" spec=\"" + this.cordovaVersion + "\" />");

		assertTrue("engine version doesn not match!", oldSpec || newSpec);
	}

	@Test
	public void canCreateHTMLHybridApplication() {
		assertTrue(new ProjectExplorer().containsProject(AerogearBotTest.CORDOVA_PROJECT_NAME));
	}

	@Test
	public void testNoErrorsAndWarnings() {
		// Check there is no error/warning on Hybrid Mobile Project
		new ProjectExplorer().selectProjects(CORDOVA_PROJECT_NAME);
		ProblemsView pview = new ProblemsView();
		pview.open();

		List<Problem> errors = pview.getProblems(ProblemType.ERROR,
				new ProblemsPathMatcher(new RegexMatcher("(/" + CORDOVA_PROJECT_NAME + "/)(.*)")));
		List<Problem> warnings = pview.getProblems(ProblemType.WARNING,
				new ProblemsPathMatcher(new RegexMatcher("(/" + CORDOVA_PROJECT_NAME + "/)(.*)")));

		assertTrue(
				"There were these errors for " + CORDOVA_PROJECT_NAME + " project " + Arrays.toString(errors.toArray()),
				errors == null || errors.size() == 0);

		assertTrue("There were these warnings for " + CORDOVA_PROJECT_NAME + " project "
				+ Arrays.toString(warnings.toArray()), warnings == null || warnings.size() == 0);

	}
}
