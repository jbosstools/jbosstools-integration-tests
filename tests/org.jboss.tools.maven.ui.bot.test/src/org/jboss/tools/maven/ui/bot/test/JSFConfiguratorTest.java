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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.maven.ui.bot.test.dialog.maven.JBossMavenIntegrationDialog;
import org.jboss.tools.maven.ui.bot.test.dialog.maven.MavenRepositoriesDialog;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
/**
 * @author Rastislav Wagner
 * 
 */
public class JSFConfiguratorTest extends AbstractConfiguratorsTest{
	
	public static final String MAVEN_ACM_REPO = "http://maven.acm-sl.org/artifactory/libs-releases/";
	
	@BeforeClass
	public static void before(){
		setPerspective("Java EE");
		JBossMavenIntegrationDialog jm = new JBossMavenIntegrationDialog();
		jm.open();
		MavenRepositoriesDialog mr = jm.modifyRepositories();
		mr.addRepository("ACM Repo", MAVEN_ACM_REPO, true);
		mr.confirm();
		jm.apply();
		jm.ok();
		//enableSnapshosts("ACM Repo");
	}
	
	@After
	public void deleteProjects(){
		deleteProjects(true, true);
	}
	
	@AfterClass
	public static void cleanRepo(){
		JBossMavenIntegrationDialog jm = new JBossMavenIntegrationDialog();
		jm.open();
		MavenRepositoriesDialog mr = jm.modifyRepositories();
		boolean deleted = mr.removeAllRepos();
		if(deleted){
			mr.confirm();
		} else {
			mr.cancel();
		}
		jm.ok();
		
	}
	
	@Test
	public void testJSFConfiguratorMojjara() throws CoreException{
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addDependency(projectName, "com.sun.faces", "mojarra-jsf-api", "2.0.0-b04");
		updateConf(projectName);
		assertTrue("Project "+projectName+" with mojarra dependency doesn't have "+JSF_FACET+" nature",hasNature(projectName, null, JSF_FACET));
	}
		
	@Test
	public void testJSFConfiguratorFaces() throws CoreException, InterruptedException{
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addFacesConf(PROJECT_NAME_JSF+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_JSF+"_noRuntime"+" with faces config doesn't have "+JSF_FACET+" nature",hasNature(PROJECT_NAME_JSF+"_noRuntime", null, JSF_FACET));
	}
	@Test
	public void testJSFConfiguratorServlet() throws CoreException, ParserConfigurationException, SAXException, IOException, TransformerException{	
		//https://issues.jboss.org/browse/JBIDE-10831
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null, true);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addServlet(projectName,"Faces Servlet","javax.faces.webapp.FacesServlet","1");
		assertTrue("Project "+projectName+"with servlet in web.xml doesn't have "+JSF_FACET+" nature",hasNature(projectName, null, JSF_FACET));
		IProject facade = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		assertTrue("Project "+projectName+" doesn't have faces-config.xml file",facade.getProject().getFile("faces-config.xml") != null);
	}
	
	@Test
	public void testJSFConfigurator() throws CoreException{
		createWebProject(PROJECT_NAME_JSF, runtimeName, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", true);
		checkProjectWithRuntime(PROJECT_NAME_JSF);
		assertTrue("Project "+PROJECT_NAME_JSF+" doesn't have "+JSF_FACET+" nature",hasNature(PROJECT_NAME_JSF,null, JSF_FACET));
	}
	//https://issues.jboss.org/browse/JBIDE-13728
	@Test
	public void testJSFConfiguratorDeltaspikeApi() throws CoreException{
		createWebProject(PROJECT_NAME_JSF, null, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", false);
		checkProjectWithoutRuntime(PROJECT_NAME_JSF);
		addDependency(PROJECT_NAME_JSF, "org.apache.deltaspike.modules", "deltaspike-jsf-module-api", "0.4");
		updateConf(PROJECT_NAME_JSF);
		assertTrue("Project "+PROJECT_NAME_JSF+" doesn't have "+JSF_FACET+" nature",hasNature(PROJECT_NAME_JSF,null, JSF_FACET));
	}
	//https://issues.jboss.org/browse/JBIDE-13728
	@Test
	public void testJSFConfiguratorDeltaspikeImpl() throws CoreException{
		createWebProject(PROJECT_NAME_JSF, null, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", false);
		checkProjectWithoutRuntime(PROJECT_NAME_JSF);
		addDependency(PROJECT_NAME_JSF, "org.apache.deltaspike.modules", "deltaspike-jsf-module-impl", "0.4");
		updateConf(PROJECT_NAME_JSF);
		assertTrue("Project "+PROJECT_NAME_JSF+" doesn't have "+JSF_FACET+" nature",hasNature(PROJECT_NAME_JSF,null, JSF_FACET));
	}
	
	@Test
	public void testJSFConfiguratorSeam() throws CoreException{
		//https://issues.jboss.org/browse/JBIDE-8755
		String projectName = PROJECT_NAME_JSF+"_seam";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addDependency(projectName, "org.jboss.seam.faces", "seam-faces", "3.0.0.Final");
		updateConf(projectName);
		assertTrue("Project "+projectName+" with seam-faces3 dependency doesn't have "+JSF_FACET+" nature",hasNature(projectName,null, JSF_FACET));
	}
}