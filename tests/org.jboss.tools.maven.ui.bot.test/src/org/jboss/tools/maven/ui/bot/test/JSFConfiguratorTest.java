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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
/**
 * @author Rastislav Wagner
 * 
 */
public class JSFConfiguratorTest extends AbstractConfiguratorsTest{
	
	@BeforeClass
	public static void before(){
		setPerspective("Java EE");
	}
	
	@After
	public void deleteProjects(){
		deleteProjects(true, false);
	}
	
	@Test
	public void testJSFConfiguratorMojjara() throws CoreException{
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addDependency(projectName, "com.sun.faces", "mojarra-jsf-api", "2.0.0-b04");
		updateConf(projectName);
		assertTrue("Project "+projectName+" with mojarra dependency doesn't have "+JSF_NATURE+" nature",hasNature(projectName, JSF_NATURE,null));
	}
		
	@Test
	public void testJSFConfiguratorFaces() throws CoreException, InterruptedException{
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addFacesConf(PROJECT_NAME_JSF+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_JSF+"_noRuntime"+" with faces config doesn't have "+JSF_NATURE+" nature",hasNature(PROJECT_NAME_JSF+"_noRuntime", JSF_NATURE,null));
	}
	@Test
	public void testJSFConfiguratorServlet() throws CoreException, ParserConfigurationException, SAXException, IOException, TransformerException{	
		//https://issues.jboss.org/browse/JBIDE-10831
		String projectName = PROJECT_NAME_JSF+"_noRuntime";
		createWebProject(projectName, null, true);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addServlet(projectName,"Faces Servlet","javax.faces.webapp.FacesServlet","1");
		assertTrue("Project "+projectName+"with servlet in web.xml doesn't have "+JSF_NATURE+" nature",hasNature(projectName, JSF_NATURE,null));
		IProject facade = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		assertTrue("Project "+projectName+" doesn't have faces-config.xml file",facade.getProject().getFile("faces-config.xml") != null);
	}
	
	@Test
	public void testJSFConfigurator() throws CoreException{
		createWebProject(PROJECT_NAME_JSF, runtimeName, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", true);
		checkProjectWithRuntime(PROJECT_NAME_JSF);
		assertTrue("Project "+PROJECT_NAME_JSF+" doesn't have "+JSF_NATURE+" nature",hasNature(PROJECT_NAME_JSF, JSF_NATURE,null));
	}
	//https://issues.jboss.org/browse/JBIDE-13728
	@Test
	public void testJSFConfiguratorDeltaspikeApi() throws CoreException{
		createWebProject(PROJECT_NAME_JSF, null, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", false);
		checkProjectWithRuntime(PROJECT_NAME_JSF);
		addDependency(PROJECT_NAME_JSF, "org.apache.deltaspike.modules", "deltaspike-jsf-module-api", "0.4-incubating-SNAPSHOT");
		assertTrue("Project "+PROJECT_NAME_JSF+" doesn't have "+JSF_NATURE+" nature",hasNature(PROJECT_NAME_JSF, JSF_NATURE,null));
	}
	//https://issues.jboss.org/browse/JBIDE-13728
	@Test
	public void testJSFConfiguratorDeltaspikeImpl() throws CoreException{
		createWebProject(PROJECT_NAME_JSF, null, false);
		convertToMavenProject(PROJECT_NAME_JSF, "war", false);
		checkProjectWithRuntime(PROJECT_NAME_JSF);
		addDependency(PROJECT_NAME_JSF, "org.apache.deltaspike.modules", "deltaspike-jsf-module-impl", "0.4-incubating-SNAPSHOT");
		assertTrue("Project "+PROJECT_NAME_JSF+" doesn't have "+JSF_NATURE+" nature",hasNature(PROJECT_NAME_JSF, JSF_NATURE,null));
	}
	
	@Test
	public void testJSFConfiguratorSeam() throws CoreException{
		//https://issues.jboss.org/browse/JBIDE-8755
		String projectName = PROJECT_NAME_JSF+"_seam";
		createWebProject(projectName, null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);
		addDependency(projectName, "org.jboss.seam.faces", "seam-faces", "3.0.0.Alpha3");
		updateConf(projectName);
		assertTrue("Project "+projectName+" with seam-faces3 dependency doesn't have "+JSF_NATURE+" nature",hasNature(projectName, JSF_NATURE,null));
	}
}