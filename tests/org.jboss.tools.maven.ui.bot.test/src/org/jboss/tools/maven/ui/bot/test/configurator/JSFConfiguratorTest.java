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
package org.jboss.tools.maven.ui.bot.test.configurator;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasNature;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class JSFConfiguratorTest extends AbstractConfiguratorsTest{

    @InjectRequirement
    private ServerRequirement sr;
	
	public static final String MAVEN_ACM_REPO = "http://maven.acm-sl.org/artifactory/libs-releases/";
	public static final String JBOSS_REPO = "jboss-public-repository";
	
	@BeforeClass
	public static void before(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		mr.addRepository("ACM Repo", MAVEN_ACM_REPO, true);
		mr.chooseRepositoryFromList(JBOSS_REPO, true);
		mr.confirm();
		jm.apply();
		preferenceDialog.ok();
		//enableSnapshosts("ACM Repo");
	}
	
	@AfterClass
	public static void cleanRepo(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		boolean deleted = mr.removeAllRepos();
		if(deleted){
			mr.confirm();
		} else {
			mr.cancel();
		}
		preferenceDialog.ok();
		
	}
	
	@Test
	public void testJSFConfiguratorMojjara(){
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addDependency(projectName, "com.sun.faces", "mojarra-jsf-api", "2.0.0-b04");
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
		createWebProject(PROJECT_NAME_JSF, sr.getRuntimeNameLabelText(sr.getConfig()), false);
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
		addDependency(PROJECT_NAME_JSF, "org.apache.deltaspike.modules", "deltaspike-jsf-module-api", "0.4");
		updateConf(PROJECT_NAME_JSF);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_JSF, JSF_FACET, null));
	}
	//https://issues.jboss.org/browse/JBIDE-13728
	@Test
	public void testJSFConfiguratorDeltaspikeImpl(){
		createWebProject(PROJECT_NAME_JSF, null, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", false);
		checkProjectWithoutRuntime(PROJECT_NAME_JSF);
		addDependency(PROJECT_NAME_JSF, "org.apache.deltaspike.modules", "deltaspike-jsf-module-impl", "0.4");
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