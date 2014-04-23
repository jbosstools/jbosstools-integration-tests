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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.seam.reddeer.preferences.SeamPreferencePage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class SeamConfiguratorTest extends AbstractConfiguratorsTest{
	
	private String projectNameNoRuntime = PROJECT_NAME_SEAM+"_noRuntime";
	
	@BeforeClass
	public static void setupRuntimes(){
		setPerspective("Java EE");
		SeamPreferencePage sp = new SeamPreferencePage();
		sp.open();
		sp.addRuntime(SeamProjectTest.SEAM_2_1_NAME, SeamProjectTest.SEAM_2_1, "2.1");
		sp.addRuntime(SeamProjectTest.SEAM_2_2_NAME, SeamProjectTest.SEAM_2_2, "2.2");
		sp.addRuntime(SeamProjectTest.SEAM_2_3_NAME, SeamProjectTest.SEAM_2_3, "2.3");
		sp.ok();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		mr.chooseRepositoryFromList(MavenRepositories.JBOSS_REPO,true);
		mr.confirm();
		jm.apply();
		jm.ok();
		enableSnapshots(MavenRepositories.JBOSS_REPO);
	}
	
	@After
	public void clean(){
		deleteProjects(true, true);
	}
	
	@AfterClass
	public static void cleanRepo(){
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		boolean deleted = mr.removeAllRepos();
		if(deleted){
			mr.confirm();
		} else {
			mr.cancel();
		}
		jm.ok();
		
	}
	
	@Test
	public void testSeamConfigurator21Seam() throws CoreException{
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam", "2.1.2.GA"); //dependency type EJB
		updateConf(projectNameNoRuntime);
		assertTrue("Project "+projectNameNoRuntime+" with jboss-seam dependency doesn't have "+SEAM_FACET+" nature.",hasNature(projectNameNoRuntime, "2.1", SEAM_FACET));
	}
	
	@Test
	public void testSeamConfigurator22Seam() throws CoreException{
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam", "2.2.2.Final"); //dependency type EJB
		updateConf(projectNameNoRuntime);
		assertTrue("Project "+projectNameNoRuntime+" with jboss-seam dependency doesn't have "+SEAM_FACET+" nature.",hasNature(projectNameNoRuntime, "2.2", SEAM_FACET));
	}
	
	@Test
	public void testSeamConfigurator22SeamDebug() throws CoreException{
		createWebProject(projectNameNoRuntime, null,false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-debug", "2.2.2.Final"); //dependency type EJB
		updateConf(projectNameNoRuntime);
		assertTrue("Project "+projectNameNoRuntime+" with jboss-seam dependency doesn't have "+SEAM_FACET+" nature.",hasNature(projectNameNoRuntime, "2.2", SEAM_FACET));
	}
	
	@Test
	public void testSeamConfigurator23Seam() throws CoreException{
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam", "2.3.0.Beta2"); //dependency type EJB
		updateConf(projectNameNoRuntime);
		assertTrue("Project "+projectNameNoRuntime+" with jboss-seam dependency doesn't have "+SEAM_FACET+" nature.",hasNature(projectNameNoRuntime, "2.3", SEAM_FACET));
	}
	@Test
	public void testSeamConfigurator23SeamUI() throws CoreException{	
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-ui", "2.3.0.Beta2");
		updateConf(projectNameNoRuntime);
		assertTrue("Project "+projectNameNoRuntime+" with jboss-seam-ui dependency doesn't have "+SEAM_FACET+" nature.",hasNature(projectNameNoRuntime, "2.3", SEAM_FACET));
	}
	@Test
	public void testSeamConfigurator23SeamPDF() throws CoreException{		
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-pdf", "2.3.0.Beta2");
		updateConf(projectNameNoRuntime);
		assertTrue("Project "+projectNameNoRuntime+" with jboss-seam-pdf dependency doesn't have "+SEAM_FACET+" nature.",hasNature(projectNameNoRuntime, "2.3", SEAM_FACET ));
	}
	@Test
	public void testSeamConfigurator23SeamRemoting() throws CoreException{		
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-remoting", "2.3.0.Beta2");
		updateConf(projectNameNoRuntime);
		assertTrue("Project "+projectNameNoRuntime+" with jboss-seam-ioc dependency doesn't have "+SEAM_FACET+" nature.",hasNature(projectNameNoRuntime, "2.3", SEAM_FACET));
	}
	@Test
	public void testSeamConfigurator23SeamIOC() throws CoreException{	
		createWebProject(projectNameNoRuntime,null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-ioc", "2.3.0.Beta2");
		updateConf(projectNameNoRuntime);
		assertTrue("Project "+projectNameNoRuntime+" with jboss-seam-ioc dependency doesn't have "+SEAM_FACET+" nature.",hasNature(projectNameNoRuntime, "2.3", SEAM_FACET));
	}
	
	@Test
	public void testSeamRuntimeConfigurator23() throws CoreException{
		createWebProject(PROJECT_NAME_SEAM, null, false);
		convertToMavenProject(PROJECT_NAME_SEAM, "war", false);
		addDependency(PROJECT_NAME_SEAM, "org.jboss.seam", "jboss-seam", "2.3.0.Final"); //dependency type EJB
		updateConf(PROJECT_NAME_SEAM);
		
		String seamRuntime = getSeamRuntime(PROJECT_NAME_SEAM);
		assertEquals("Project "+PROJECT_NAME_SEAM+" with jboss-seam dependency doesn't have proper seam runtime chosen", SeamProjectTest.SEAM23_NAME, seamRuntime);
		
	}
	
	@Test
	public void testSeamRuntimeConfigurator22() throws CoreException{
		createWebProject(PROJECT_NAME_SEAM, null, false);
		convertToMavenProject(PROJECT_NAME_SEAM, "war", false);
		addDependency(PROJECT_NAME_SEAM, "org.jboss.seam", "jboss-seam", "2.2.2.Final"); //dependency type EJB
		updateConf(PROJECT_NAME_SEAM);
		String seamRuntime = getSeamRuntime(PROJECT_NAME_SEAM);
		assertEquals("Project "+PROJECT_NAME_SEAM+" with jboss-seam dependency doesn't have proper seam runtime chosen", SeamProjectTest.SEAM22_NAME, seamRuntime);
	}
	
	@Test
	public void testSeamRuntimeConfigurator21() throws CoreException{
		createWebProject(PROJECT_NAME_SEAM, null, false);
		convertToMavenProject(PROJECT_NAME_SEAM, "war", false);
		addDependency(PROJECT_NAME_SEAM, "org.jboss.seam", "jboss-seam", "2.1.2.GA"); //dependency type EJB
		updateConf(PROJECT_NAME_SEAM);
		
		String seamRuntime = getSeamRuntime(PROJECT_NAME_SEAM);
		assertEquals("Project "+PROJECT_NAME_SEAM+" with jboss-seam dependency doesn't have proper seam runtime chosen", SeamProjectTest.SEAM_2_1_NAME, seamRuntime);
	}
	
	private String getSeamRuntime(String project){
		PackageExplorer p = new PackageExplorer();
		p.open();
		p.getProject(project).select();
		new ContextMenu("Properties").select();
		new DefaultTreeItem("Seam Settings").select();
		String runtime =  new LabeledCombo("Seam Runtime:").getSelection();
		new PushButton("OK").click();
		return runtime;
	}
}