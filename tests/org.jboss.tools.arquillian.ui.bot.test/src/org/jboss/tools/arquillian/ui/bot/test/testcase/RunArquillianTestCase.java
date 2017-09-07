/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/

package org.jboss.tools.arquillian.ui.bot.test.testcase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.jdt.junit.ui.TestRunnerViewPart;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.JUnitConfigurationPage;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.JUnitTestTab;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.RunConfigurationsDialog;
import org.jboss.tools.arquillian.ui.bot.reddeer.junit.view.JUnitTestIsRunningCondition;
import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Runs Arquillian test case on remote Wildfly server and checks that
 * 
 * <ul>
 * <li>there are no failing tests in JUnit view
 * </ul>
 * 
 * @author Lucia Jelinkova
 *
 */
@OpenPerspective(JavaPerspective.class)
@JBossServer(state = ServerRequirementState.RUNNING)
public class RunArquillianTestCase extends AbstractArquillianTestCase {

	private static final String LAUNCH_CONFIGURATION = "arq-test";
	private static final Logger log = Logger.getLogger(RunArquillianTestCase.class);

	@Before
	public void before() {
		new WaitUntil(new JobIsRunning(), TimePeriod.LONG, false);
		prepareProject();
		addArquillianProfile();
		selectMavenProfile();
		forceMavenRepositoryUpdate();
		createTestCase();
		changeContent();
	}

	@Test
	public void testRunningTestCase() {
		runTestCase();
		checkJUnitView();
	}

	private void runTestCase() {
		RunConfigurationsDialog runDialog = new RunConfigurationsDialog();
		runDialog.open();
		runDialog.createConfiguration(new JUnitConfigurationPage(LAUNCH_CONFIGURATION));

		JUnitTestTab mainTab = new JUnitTestTab();
		mainTab.setProject(PROJECT_NAME);
		mainTab.setTestClass(PACKAGE + "." + TEST_CASE);

		// ArquillianTab arquillianTab = new ArquillianTab();
		// arquillianTab.selectMavenProfile(AddArquillianProfile.PROFILE_NAME);

		runDialog.run();
		/*
		 * Intermittent timing issue
		 * (https://issues.jboss.org/browse/JBIDE-22866) is being seen where the
		 * Run Configurations dialog is not closed - add this to make test more
		 * reliable
		 */
		log.info("Attempting to close the Run Configurations Dialog");

		try {
			new DefaultShell(new WithTextMatcher(new RegexMatcher(".*Run Configurations.*"))).close();
			log.info("Closed the Run Configurations Dialog");
		} catch (CoreLayerException swtle) {
			log.error("Unable to close the Run Configurations Dialog - " + swtle.getMessage());
			log.error(swtle);
		}
	}

	private void checkJUnitView() {
		TestRunnerViewPart view = new TestRunnerViewPart();
		view.open();

		new WaitWhile(new JUnitTestIsRunningCondition(), TimePeriod.LONG);

		assertThat(view.getNumberOfErrors(), is(0));
		assertThat(view.getNumberOfFailures(), is(0));
	}
}
