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
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.util.FileUtil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsPathMatcher;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project.ThymPlatform;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

@CleanWorkspace
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class CreateHybridApplication extends AerogearBotTest {

	private ThymPlatform platform;
	private String engineVersion;
	private String cordovaVersion;

	@Parameters(name="{1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
			{ThymPlatform.ANDROID, "cordova-android@3.6.4"},
			{ThymPlatform.ANDROID, "cordova-android@3.7.2"},
			{ThymPlatform.ANDROID, "cordova-android@4.0.0"},
			{ThymPlatform.ANDROID, "cordova-android@4.1.1"},
			{ThymPlatform.ANDROID, "cordova-android@5.2.1"},
			{ThymPlatform.ANDROID, "cordova-android@5.2.2"}
		});
	}

	public CreateHybridApplication(ThymPlatform platform, String engineVersion) {
		this.platform = platform;
		this.engineVersion = engineVersion;
		this.cordovaVersion = engineVersion.substring(engineVersion.indexOf("@") + 1, engineVersion.length());
	}

	@Before
	public void setUp() {
		createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME, AerogearBotTest.CORDOVA_APP_NAME,
			"org.jboss.example.cordova", this.platform, this.engineVersion);

		assertTrue(new ProjectExplorer().containsProject(AerogearBotTest.CORDOVA_PROJECT_NAME));
	}

	@Test
	public void testEngineVersionMatchesInConfigXML() throws IOException {
		String xmlConfig = FileUtil.readFile(WS_PATH + "/" + CORDOVA_PROJECT_NAME + "/config.xml");
		boolean oldSpec = xmlConfig.toLowerCase()
				.contains("<engine name=\"" + this.platform.getText() + "\" version=\"" + this.cordovaVersion + "\" />".toLowerCase());
		boolean newSpec = xmlConfig.toLowerCase()
				.contains("<engine name=\"" + this.platform.getText().toLowerCase() + "\" spec=\"" + this.cordovaVersion + "\" />".toLowerCase());

		assertTrue("engine version doesn not match!", oldSpec || newSpec);
	}

	@Test
	public void testNoErrorsAndWarnings() {
		// Check there is no error/warning on Hybrid Mobile Project
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(CORDOVA_PROJECT_NAME);
		
		checkErrorsDuringPrepare();
		checkErrorsDuringPlatformAdd();
		
		
		ProblemsView pview = new ProblemsView();
		pview.open();

		List<Problem> errors = pview.getProblems(ProblemType.ERROR, new ProblemsPathMatcher(new CordovaPathMatcher()));
		List<Problem> warnings = pview.getProblems(ProblemType.WARNING, new ProblemsPathMatcher(new CordovaPathMatcher()));
		//errors in log
		assertTrue(
				"There were these errors for " + CORDOVA_PROJECT_NAME + " project " + Arrays.toString(errors.toArray()),
				errors == null || errors.size() == 0);

		assertTrue("There were these warnings for " + CORDOVA_PROJECT_NAME + " project "
				+ Arrays.toString(warnings.toArray()), warnings == null || warnings.size() == 0);

	}
	
	private void checkErrorsDuringPrepare(){
		ConsoleView cw = new ConsoleView();
		cw.switchConsole(new RegexMatcher(".*cordova prepare "));
		if(cordovaVersion.equals("5.2.2")){ //no text in case of 5.2.2 version
			new WaitUntil(new ConsoleHasText(""));
		} else {
			new WaitUntil(new ConsoleHasText("Creating Cordova project"));
		}
		String consoleText = cw.getConsoleText();
		assertFalse("ConsoleView has error: "+consoleText,consoleText.toLowerCase().contains("error"));
	}
	
	private void checkErrorsDuringPlatformAdd(){
		ConsoleView cw = new ConsoleView();
		cw.switchConsole(new RegexMatcher(".*cordova platform add"));
		//https://github.com/eclipse/thym/pull/73
		new WaitUntil(new ConsoleHasText("Error: Platform android already added"));
	}
	
	private class CordovaPathMatcher extends TypeSafeMatcher<String> {

		@Override
		public void describeTo(Description description) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected boolean matchesSafely(String item) {
			return item.matches("(/" + CORDOVA_PROJECT_NAME + "/)(.*)") && 
					!item.contains(CORDOVA_PROJECT_NAME+"/platforms") && 
					!item.contains(CORDOVA_PROJECT_NAME+"/plugins");
		}
		
	}
}
