/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.maven.ui.bot.test;

import java.io.FileNotFoundException;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasNature;
import org.junit.Test;

/**
 * @author Rastislav Wagner
 * 
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
public class JPAConfiguratorTest extends AbstractConfiguratorsTest {
	
	private String projectNameNoRuntime = PROJECT_NAME_JPA + "_noRuntime";
	
	@Test
	public void testJPAConfigurator() throws FileNotFoundException {
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addPersistence(projectNameNoRuntime);
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, JPA_FACET, "2.0"));
	}
}