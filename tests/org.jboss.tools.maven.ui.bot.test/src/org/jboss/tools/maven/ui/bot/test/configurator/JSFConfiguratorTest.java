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

import static org.junit.Assert.assertTrue;
import static org.jboss.tools.maven.ui.bot.test.utils.MavenProjectHelper.addDependency;
import static org.jboss.tools.maven.ui.bot.test.utils.MavenProjectHelper.convertToMavenProject;
import static org.jboss.tools.maven.ui.bot.test.utils.MavenProjectHelper.updateConf;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.DefineMavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.MavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.PredefinedMavenRepository;
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
@DefineMavenRepository(newRepositories = {@MavenRepository(url="http://maven.acm-sl.org/artifactory/libs-releases/",ID="acm",snapshots=true)}, 
predefinedRepositories = { @PredefinedMavenRepository(ID="jboss-public-repository",snapshots=true) })
public class JSFConfiguratorTest extends AbstractConfiguratorsTest{

    @InjectRequirement
    private ServerRequirement sr;
	
	public static final String MAVEN_ACM_REPO = "http://maven.acm-sl.org/artifactory/libs-releases/";
	public static final String JBOSS_REPO = "jboss-public-repository";
	
	@Test
	public void testJSFConfiguratorMojjara(){
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addDependency(projectName, "com.sun.faces", "jsf-api", "2.2.20");
		updateConf(projectName);
		new WaitUntil(new ProjectHasNature(projectName, JSF_FACET, null));
	}
		
	@Test
	public void testJSFConfiguratorFaces() {
	    //https://issues.jboss.org/browse/JBIDE-10831
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addFacesConf(projectName);
		new WaitUntil(new ProjectHasNature(projectName, JSF_FACET, null));
	}
	@Test
	public void testJSFConfiguratorServlet() {	
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null, true);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addServlet(projectName,"Faces Servlet","javax.faces.webapp.FacesServlet","1");
		new WaitUntil(new ProjectHasNature(projectName, JSF_FACET, null));
		assertTrue("Project "+projectName+"with servlet in web.xml doesn't have "+JSF_FACET+" nature",hasNature(projectName, null, JSF_FACET));
	}
	
	@Test
	public void testJSFConfigurator() {
		createWebProject(PROJECT_NAME_JSF, sr.getRuntimeName(), false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", true);
		checkProjectWithRuntime(PROJECT_NAME_JSF);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_JSF, JSF_FACET, null));
	}
	//https://issues.jboss.org/browse/JBIDE-13728
	@Test
	public void testJSFConfiguratorDeltaspikeApi() {
		createWebProject(PROJECT_NAME_JSF, null, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", false);
		checkProjectWithoutRuntime(PROJECT_NAME_JSF);
		addDependency(PROJECT_NAME_JSF, "org.apache.deltaspike.modules", "deltaspike-jsf-module-api", "1.9.3");
		updateConf(PROJECT_NAME_JSF);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_JSF, JSF_FACET, null));
	}
	//https://issues.jboss.org/browse/JBIDE-13728
	@Test
	public void testJSFConfiguratorDeltaspikeImpl(){
		createWebProject(PROJECT_NAME_JSF, null, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", false);
		checkProjectWithoutRuntime(PROJECT_NAME_JSF);
		addDependency(PROJECT_NAME_JSF, "org.apache.deltaspike.modules", "deltaspike-jsf-module-impl", "1.9.3");
		updateConf(PROJECT_NAME_JSF);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_JSF, JSF_FACET, null));
	}
	
	@Test
	public void testJSFConfiguratorSeam() {
		//https://issues.jboss.org/browse/JBIDE-8755
		String projectName = PROJECT_NAME_JSF+"_seam";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addDependency(projectName, "org.jboss.seam.faces", "seam-faces", "3.0.0.Final");
		updateConf(projectName);
		new WaitUntil(new ProjectHasNature(projectName, JSF_FACET, null));
	}
}