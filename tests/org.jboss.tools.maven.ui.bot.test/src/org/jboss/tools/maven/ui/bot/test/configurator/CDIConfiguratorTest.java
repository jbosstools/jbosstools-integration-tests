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
public class CDIConfiguratorTest extends AbstractConfiguratorsTest{
	
	public static final String CDI_API_VERSION_1_2="1.2";
	public static final String CDI_API_VERSION_2_0="2.0";
	public static final String SEAM_FACES_VERSION="3.0.0.Alpha3";
	public static final String SEAM_INTERNATIONAL_VERSION="3.0.0.Alpha1";
	public static final String DELTASPIKE_CORE_API_VERSION="1.9.3";
	public static final String DELTASPIME_CORE_IMPL_VERSION="1.9.3";
	
	@InjectRequirement
    private ServerRequirement sr;

	@Test
	public void testCDIConfiguratorApi1_2() {
		createWebProject(PROJECT_NAME_CDI, sr.getRuntimeName(),false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", false);
		addDependency(PROJECT_NAME_CDI, "javax.enterprise", "cdi-api",CDI_API_VERSION_1_2);
		updateConf(PROJECT_NAME_CDI);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI, CDI_FACET, CDI_API_VERSION_1_2));
	}
	
	@Test
	public void testCDIConfiguratorEjbApi1_2() {
		createEJBProject(PROJECT_NAME_CDI_EJB, sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", false);
		addDependency(PROJECT_NAME_CDI_EJB, "javax.enterprise", "cdi-api",CDI_API_VERSION_1_2);
		updateConf(PROJECT_NAME_CDI_EJB);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI_EJB, CDI_FACET, CDI_API_VERSION_1_2));
	}
	
	@Test
	public void testCDIConfiguratorApi2_0() {
		createWebProject(PROJECT_NAME_CDI, sr.getRuntimeName(),false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", false);
		addDependency(PROJECT_NAME_CDI, "javax.enterprise", "cdi-api",CDI_API_VERSION_2_0);
		updateConf(PROJECT_NAME_CDI);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorEjbApi2_0() {
		createEJBProject(PROJECT_NAME_CDI_EJB, sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", false);
		addDependency(PROJECT_NAME_CDI_EJB, "javax.enterprise", "cdi-api",CDI_API_VERSION_2_0);
		updateConf(PROJECT_NAME_CDI_EJB);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI_EJB, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfigurator() {
		createWebProject(PROJECT_NAME_CDI, sr.getRuntimeName(),false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", true);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorEjb() {
		createEJBProject(PROJECT_NAME_CDI_EJB, sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", true);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI_EJB, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorSeam() {
		//https://issues.jboss.org/browse/JBIDE-8755
		createWebProject(PROJECT_NAME_CDI, sr.getRuntimeName(), false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", true);
		addDependency(PROJECT_NAME_CDI, "org.jboss.seam.faces", "seam-faces", SEAM_FACES_VERSION);
		updateConf(PROJECT_NAME_CDI);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorEjbSeam() {
		createEJBProject(PROJECT_NAME_CDI_EJB, sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", true);
		addDependency(PROJECT_NAME_CDI_EJB, "org.jboss.seam.faces", "seam-faces", SEAM_FACES_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI_EJB, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorSeamInternational() {
		createWebProject(PROJECT_NAME_CDI, sr.getRuntimeName(), false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", true);
		addDependency(PROJECT_NAME_CDI, "org.jboss.seam.international", "seam-international", SEAM_INTERNATIONAL_VERSION);
		updateConf(PROJECT_NAME_CDI);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorEjbSeamInternational() {
		createEJBProject(PROJECT_NAME_CDI_EJB, sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", true);
		addDependency(PROJECT_NAME_CDI_EJB, "org.jboss.seam.international", "seam-international", SEAM_INTERNATIONAL_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI_EJB, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorEjbDeltaspike() {
		createEJBProject(PROJECT_NAME_CDI_EJB, sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", true);
		addDependency(PROJECT_NAME_CDI_EJB, "org.apache.deltaspike.core", "deltaspike-core-api", DELTASPIKE_CORE_API_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI_EJB, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorDeltaspike() {
		createWebProject(PROJECT_NAME_CDI, sr.getRuntimeName(), false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", true);
		addDependency(PROJECT_NAME_CDI, "org.apache.deltaspike.core", "deltaspike-core-api", DELTASPIKE_CORE_API_VERSION);
		updateConf(PROJECT_NAME_CDI);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorEjbDeltaspimeImpl() {	
		createEJBProject(PROJECT_NAME_CDI_EJB, sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME_CDI_EJB, "ejb", true);
		addDependency(PROJECT_NAME_CDI_EJB, "org.apache.deltaspike.core", "deltaspike-core-impl", DELTASPIME_CORE_IMPL_VERSION);
		updateConf(PROJECT_NAME_CDI_EJB);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI_EJB, CDI_FACET, CDI_API_VERSION_2_0));
	}
	
	@Test
	public void testCDIConfiguratorDeltaspikeImpl() {
		createWebProject(PROJECT_NAME_CDI, sr.getRuntimeName(), false);
		convertToMavenProject(PROJECT_NAME_CDI, "war", true);
		addDependency(PROJECT_NAME_CDI, "org.apache.deltaspike.core", "deltaspike-core-impl", DELTASPIME_CORE_IMPL_VERSION);
		updateConf(PROJECT_NAME_CDI);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_CDI, CDI_FACET, CDI_API_VERSION_2_0));
	}

}