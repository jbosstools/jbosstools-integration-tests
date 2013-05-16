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


import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class CDIConfiguratorTest extends AbstractConfiguratorsTest{
	
	public static final String CDI_API_VERSION="1.1.EDR1.2";
	public static final String SEAM_FACES_VERSION="3.0.0.Alpha3";
	public static final String SEAM_INTERNATIONAL_VERSION="3.0.0.Alpha1";
	public static final String DELTASPIKE_CORE_API_VERSION="0.3-incubating";
	public static final String DELTASPIME_CORE_IMPL_VERSION="0.3-incubating";
	
	@BeforeClass
	public static void before(){
		setPerspective("Java EE");
	}
	
	@After
	public void clean(){
		deleteProjects(true, true);
	}
	
	@Test
	public void testCDIConfiguratorApi() throws CoreException{
		createWebProject(PROJECT_NAME_CDI, null,false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", false);
		addDependency(PROJECT_NAME_CDI, "javax.enterprise", "cdi-api",CDI_API_VERSION);
		updateConf(PROJECT_NAME_CDI);
		assertTrue("Project "+PROJECT_NAME_CDI+" with cdi dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI, CDI_NATURE,null));
		
	}
	@Test
	public void testCDIConfiguratorEjbApi() throws CoreException{
		createEJBProject(PROJECT_NAME_CDI_EJB, null);
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", false);
		addDependency(PROJECT_NAME_CDI_EJB, "javax.enterprise", "cdi-api",CDI_API_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+" with cdi dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB, CDI_NATURE,null));
	}
	@Test
	public void testCDIConfigurator() throws CoreException{
		createWebProject(PROJECT_NAME_CDI, runtimeName,false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", true);
		assertTrue("Project "+PROJECT_NAME_CDI+" has "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI, CDI_NATURE,null));
	}
	@Test
	public void testCDIConfiguratorEjb() throws CoreException{
		createEJBProject(PROJECT_NAME_CDI_EJB, runtimeName);
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", true);
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+" has "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB, CDI_NATURE,null));

	}
	@Test
	public void testCDIConfiguratorSeam() throws CoreException{
		//https://issues.jboss.org/browse/JBIDE-8755
		createWebProject(PROJECT_NAME_CDI, null,false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", false);
		addDependency(PROJECT_NAME_CDI, "org.jboss.seam.faces", "seam-faces", SEAM_FACES_VERSION);
		updateConf(PROJECT_NAME_CDI);
		assertTrue("Project "+PROJECT_NAME_CDI+" with seam-faces3 dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI, CDI_NATURE,null));

	}
	@Test
	public void testCDIConfiguratorEjbSeam() throws CoreException{
		createEJBProject(PROJECT_NAME_CDI_EJB, null);
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", false);
		addDependency(PROJECT_NAME_CDI_EJB, "org.jboss.seam.faces", "seam-faces", SEAM_FACES_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+" with seam-faces3 dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB, CDI_NATURE,null));

	}
	@Test
	public void testCDIConfiguratorSeamInternational() throws CoreException{
		createWebProject(PROJECT_NAME_CDI, null, false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", false);
		addDependency(PROJECT_NAME_CDI, "org.jboss.seam.international", "seam-international", SEAM_INTERNATIONAL_VERSION);
		updateConf(PROJECT_NAME_CDI);
		assertTrue("Project "+PROJECT_NAME_CDI+" with seam3 dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI, CDI_NATURE,null));
	}
	@Test
	public void testCDIConfiguratorEjbSeamInternational() throws CoreException{
		createEJBProject(PROJECT_NAME_CDI_EJB, null);
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", false);
		addDependency(PROJECT_NAME_CDI_EJB, "org.jboss.seam.international", "seam-international", SEAM_INTERNATIONAL_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+" with seam3 dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB, CDI_NATURE,null));

	}
	@Test
	public void testCDIConfiguratorEjbDeltaspike() throws CoreException{
		createEJBProject(PROJECT_NAME_CDI_EJB, null);
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", false);
		addDependency(PROJECT_NAME_CDI_EJB, "org.apache.deltaspike.core", "deltaspike-core-api", DELTASPIKE_CORE_API_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+" with deltaspike-api dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB, CDI_NATURE,null));

	}
	@Test
	public void testCDIConfiguratorDeltaspike() throws CoreException{
		createWebProject(PROJECT_NAME_CDI, null, false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", false);
		addDependency(PROJECT_NAME_CDI, "org.apache.deltaspike.core", "deltaspike-core-api", DELTASPIKE_CORE_API_VERSION);
		updateConf(PROJECT_NAME_CDI);
		assertTrue("Project "+PROJECT_NAME_CDI+" with deltaspike-api dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI, CDI_NATURE,null));

	}
	@Test
	public void testCDIConfiguratorEjbDeltaspimeImpl() throws CoreException{	
		createEJBProject(PROJECT_NAME_CDI_EJB, null);
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", false);
		addDependency(PROJECT_NAME_CDI_EJB, "org.apache.deltaspike.core", "deltaspike-core-impl", DELTASPIME_CORE_IMPL_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+" with deltaspike-impl dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB, CDI_NATURE,null));

	}
	@Test
	public void testCDIConfiguratorDeltaspikeImpl() throws CoreException{
		createWebProject(PROJECT_NAME_CDI, null, false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", false);
		addDependency(PROJECT_NAME_CDI, "org.apache.deltaspike.core", "deltaspike-core-impl", DELTASPIME_CORE_IMPL_VERSION);
		updateConf(PROJECT_NAME_CDI);
		assertTrue("Project "+PROJECT_NAME_CDI+" with deltaspike-impl dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI, CDI_NATURE,null));
	}

}