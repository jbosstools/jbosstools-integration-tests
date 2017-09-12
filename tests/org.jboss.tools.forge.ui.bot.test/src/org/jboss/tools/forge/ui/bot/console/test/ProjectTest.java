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
package org.jboss.tools.forge.ui.bot.console.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.junit.Test;

/**
 * 
 * @author psrna
 *
 */
@CleanWorkspace
public class ProjectTest extends ForgeConsoleTestBase {

	@Test
	public void pomProject() {
		createProject(ProjectTypes.pom);
		String text = fView.getConsoleText();
		assertTrue(text.contains("***SUCCESS*** Created project [" + PROJECT_NAME + "]"));
		assertTrue(pExplorer.containsProject(PROJECT_NAME));
		Project project = pExplorer.getProject(PROJECT_NAME);
		assertTrue(project.containsResource("pom.xml"));
		
		try {
			String pomContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/pom.xml");
			assertTrue(pomContent.contains("<packaging>pom</packaging>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
	}
	
	
	@Test
	public void warProject() {
		createProject(ProjectTypes.war);
		String text = fView.getConsoleText();
		assertTrue(text.contains("***SUCCESS*** Created project [" + PROJECT_NAME + "]"));
		assertTrue(pExplorer.containsProject(PROJECT_NAME));
		Project project = pExplorer.getProject(PROJECT_NAME);
		assertTrue(project.containsResource("pom.xml"));
		
		try {
			String pomContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/pom.xml");
			assertTrue(pomContent.contains("<packaging>war</packaging>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
	}
	
}
