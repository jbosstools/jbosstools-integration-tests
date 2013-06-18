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

import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class JAXRSConfiguratorTest extends AbstractConfiguratorsTest{

	
	@BeforeClass
	public static void before(){
		setPerspective("Java EE");
	}
	
	@After
	public void deleteProjects(){
		deleteProjects(true, true);
	}

	@Test
	public void testJAXRSConfiguratorJersey() throws CoreException {
		String projectName = PROJECT_NAME_JAXRS+"_noRuntime";
		
		createWebProject(projectName,null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);

		addDependency(projectName, "com.cedarsoft.rest", "jersey", "1.0.0");
		updateConf(projectName);
		assertTrue("Project "+projectName+" with jersey dependency doesn't have "+JAXRS_FACET+" nature.",hasNature(projectName, null, JAXRS_FACET));
	}
	@Test
	public void testJAXRSConfiguratorResteasy() throws CoreException {
		String projectName = PROJECT_NAME_JAXRS+"_noRuntime";
		createWebProject(projectName,null,false);
		convertToMavenProject(projectName, "war", false);
		
		checkProjectWithoutRuntime(projectName);
		
		addDependency(projectName, "org.jboss.jbossas", "jboss-as-resteasy", "6.1.0.Final");
		updateConf(projectName);
		assertTrue("Project "+projectName+" with resteasy dependency doesn't have "+JAXRS_FACET+" nature.",hasNature(projectName,null, JAXRS_FACET));
	}
	
	@Test
	public void testJAXRSConfigurator() throws CoreException {
		createWebProject(PROJECT_NAME_JAXRS, runtimeName,false);
		convertToMavenProject(PROJECT_NAME_JAXRS, "war", true);
		updateConf(PROJECT_NAME_JAXRS);
		checkProjectWithRuntime(PROJECT_NAME_JAXRS);
		assertTrue("Project "+PROJECT_NAME_JAXRS+" doesn't have "+JAXRS_FACET+" nature.",hasNature(PROJECT_NAME_JAXRS,null, JAXRS_FACET));
	}
}