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

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.DefineMavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.PropertyDefinedMavenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;
/**
 * This testsuite consist of one test. It imports all examples (location of examples is defined by system property
 * "examplesLocation") and checks for errors and warnings.
 * 
 * @author rhopp, jkopriva
 *
 */

@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@DefineMavenRepository(propDefMavenRepo = @PropertyDefinedMavenRepository(ID = "test", snapshots = true) )
@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.EAP7x)
public class EAPImportQuickstartsTest extends AbstractImportQuickstartsTest {
	public static final String SERVER_NAME ="Enterprise Application Platform";

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
		runQuickstarts(qstart, SERVER_NAME);
	}


	
}