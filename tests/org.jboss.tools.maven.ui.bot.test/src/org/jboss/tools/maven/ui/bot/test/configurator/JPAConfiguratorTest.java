/*******************************************************************************
 * Copyright (c) 2011-2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.maven.ui.bot.test.configurator;

import static org.jboss.tools.maven.ui.bot.test.utils.MavenProjectHelper.convertToMavenProject;
import static org.jboss.tools.maven.ui.bot.test.utils.MavenProjectHelper.updateConf;

import java.io.FileNotFoundException;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasNature;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Rastislav Wagner
 * 
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT)
public class JPAConfiguratorTest extends AbstractConfiguratorsTest {
	
	private String projectNameNoRuntime = PROJECT_NAME_JPA + "_noRuntime";
	public static final String JPA_FACET_VERSION_2_1 = "2.1";
	public static final String JPA_FACET_VERSION_2_2 = "2.2";
	
	@InjectRequirement
    private ServerRequirement sr;
	
	@Test
	public void testJPAConfiguratorWithoutServerJPA21() throws FileNotFoundException {
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addPersistence(projectNameNoRuntime, JPA_FACET_VERSION_2_1);
		updateConf(projectNameNoRuntime, true);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, JPA_FACET, JPA_FACET_VERSION_2_1));
	}
	
	@Test
	public void testJPAConfiguratorWithServerJPA21() throws FileNotFoundException {
		createWebProject(projectNameNoRuntime, sr.getRuntimeName(), false);
		convertToMavenProject(projectNameNoRuntime, "war", true);
		addPersistence(projectNameNoRuntime, JPA_FACET_VERSION_2_1);
		updateConf(projectNameNoRuntime, true);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, JPA_FACET, JPA_FACET_VERSION_2_1));
	}
	
	@Test
	public void testJPAConfiguratorWithoutServerJPA22() throws FileNotFoundException {
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addPersistence(projectNameNoRuntime, JPA_FACET_VERSION_2_2);
		updateConf(projectNameNoRuntime, true);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, JPA_FACET, JPA_FACET_VERSION_2_2));
	}
	
	@Test
	public void testJPAConfiguratorWithServerJPA22() throws FileNotFoundException {
		createWebProject(projectNameNoRuntime, sr.getRuntimeName(), false);
		convertToMavenProject(projectNameNoRuntime, "war", true);
		addPersistence(projectNameNoRuntime, JPA_FACET_VERSION_2_2);
		updateConf(projectNameNoRuntime, true);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, JPA_FACET, JPA_FACET_VERSION_2_2));
	}
}