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


package org.jboss.tools.examples.ui.bot.test.integration;

import java.util.Collection;

import org.eclipse.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.browser.InternalBrowserRequirement.UseInternalBrowser;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.DefineMavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.MavenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * This testsuite consist of one test. It imports all examples (location of
 * examples is defined by system property "examplesLocation") and checks for
 * errors and warnings. After it project is deployed on server and it is checked, if the project is running
 * 
 * @author rhopp, jkopriva
 *
 */

@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@JBossServer(state = ServerRequirementState.RUNNING)
@DefineMavenRepository(newRepositories = {@MavenRepository(url="https://maven.repository.redhat.com/ga/",ID="ga",snapshots=true)})
@UseInternalBrowser

public class WildFlyImportQuickstartsTest extends AbstractImportQuickstartsTest {
	public static final String SERVER_NAME = "WildFly";
	public static final String BLACKLIST_FILE = "resources/servers/wildfly-blacklist";
	public static final String BLACKLIST_ERRORS_REGEXES_FILE = "resources/servers/wildfly-blacklist-test-errors-regexes.json";

	@Parameters(name = "{0}")
	public static Collection<Quickstart> data() {
		return createQuickstartsList();
	}

	@Parameter
	public Quickstart qstart;

	/*
	 * Main tests. Imports quickstart as maven project, performs checks and
	 * deploy on server.
	 */
	@Test
	public void quickstartTest() {
		runQuickstarts(qstart, SERVER_NAME,BLACKLIST_FILE, BLACKLIST_ERRORS_REGEXES_FILE);
	}


}