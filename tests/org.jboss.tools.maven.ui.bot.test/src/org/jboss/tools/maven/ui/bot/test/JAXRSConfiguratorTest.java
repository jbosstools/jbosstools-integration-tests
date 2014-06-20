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
public class JAXRSConfiguratorTest extends AbstractConfiguratorsTest{

	@Test
	public void testJAXRSConfiguratorJersey() {
		String projectName = PROJECT_NAME_JAXRS+"_noRuntime";
		
		createWebProject(projectName,null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);

		addDependency(projectName, "com.cedarsoft.rest", "jersey", "1.0.0");
		updateConf(projectName);
		new WaitUntil(new ProjectHasNature(projectName, JAXRS_FACET, null));
	}
	
	@Test
	public void testJAXRSConfiguratorResteasy() {
		String projectName = PROJECT_NAME_JAXRS+"_noRuntime";
		createWebProject(projectName,null,false);
		convertToMavenProject(projectName, "war", false);
		
		checkProjectWithoutRuntime(projectName);
		
		addDependency(projectName, "org.jboss.jbossas", "jboss-as-resteasy", "6.1.0.Final");
		updateConf(projectName);
		new WaitUntil(new ProjectHasNature(projectName, JAXRS_FACET, null));
	}
	
	@Test
	public void testJAXRSConfigurator() {
		createWebProject(PROJECT_NAME_JAXRS, runtimeName,false);
		convertToMavenProject(PROJECT_NAME_JAXRS, "war", true);
		updateConf(PROJECT_NAME_JAXRS);
		checkProjectWithRuntime(PROJECT_NAME_JAXRS);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_JAXRS, JAXRS_FACET, null));
	}
}