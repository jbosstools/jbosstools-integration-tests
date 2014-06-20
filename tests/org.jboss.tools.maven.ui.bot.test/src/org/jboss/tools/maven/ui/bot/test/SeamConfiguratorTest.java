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

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasNature;
import org.jboss.tools.seam.reddeer.preferences.SeamPreferencePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
public class SeamConfiguratorTest extends AbstractConfiguratorsTest{
	
	private String projectNameNoRuntime = PROJECT_NAME_SEAM+"_noRuntime";
	
	@BeforeClass
	public static void setupRuntimes(){
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
	public void testSeamConfigurator21Seam(){
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam", "2.1.2.GA"); //dependency type EJB
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.1"));
	}
	
	@Test
	public void testSeamConfigurator22Seam(){
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam", "2.2.2.Final"); //dependency type EJB
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.2"));
	}
	
	@Test
	public void testSeamConfigurator22SeamDebug(){
		createWebProject(projectNameNoRuntime, null,false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-debug", "2.2.2.Final"); //dependency type EJB
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.2"));
	}
	
	@Test
	public void testSeamConfigurator23Seam(){
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam", "2.3.0.Beta2"); //dependency type EJB
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.3"));
	}
	@Test
	public void testSeamConfigurator23SeamUI() {	
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-ui", "2.3.0.Beta2");
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.3"));
	}
	@Test
	public void testSeamConfigurator23SeamPDF(){		
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-pdf", "2.3.0.Beta2");
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.3"));
	}
	@Test
	public void testSeamConfigurator23SeamRemoting(){		
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-remoting", "2.3.0.Beta2");
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.3"));	
	}
	@Test
	public void testSeamConfigurator23SeamIOC(){	
		createWebProject(projectNameNoRuntime,null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addDependency(projectNameNoRuntime, "org.jboss.seam", "jboss-seam-ioc", "2.3.0.Beta2");
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.3"));	
	}
	
	@Test
	public void testSeamRuntimeConfigurator23(){
		createWebProject(PROJECT_NAME_SEAM, null, false);
		convertToMavenProject(PROJECT_NAME_SEAM, "war", false);
		addDependency(PROJECT_NAME_SEAM, "org.jboss.seam", "jboss-seam", "2.3.0.Final"); //dependency type EJB
		updateConf(PROJECT_NAME_SEAM);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.3"));
		String seamRuntime = getSeamRuntime(PROJECT_NAME_SEAM);
		assertEquals("Project "+PROJECT_NAME_SEAM+" with jboss-seam dependency doesn't have proper seam runtime chosen", SeamProjectTest.SEAM23_NAME, seamRuntime);
		
	}
	
	@Test
	public void testSeamRuntimeConfigurator22(){
		createWebProject(PROJECT_NAME_SEAM, null, false);
		convertToMavenProject(PROJECT_NAME_SEAM, "war", false);
		addDependency(PROJECT_NAME_SEAM, "org.jboss.seam", "jboss-seam", "2.2.2.Final"); //dependency type EJB
		updateConf(PROJECT_NAME_SEAM);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.2"));
		String seamRuntime = getSeamRuntime(PROJECT_NAME_SEAM);
		assertEquals("Project "+PROJECT_NAME_SEAM+" with jboss-seam dependency doesn't have proper seam runtime chosen", SeamProjectTest.SEAM22_NAME, seamRuntime);
	}
	
	@Test
	public void testSeamRuntimeConfigurator21(){
		createWebProject(PROJECT_NAME_SEAM, null, false);
		convertToMavenProject(PROJECT_NAME_SEAM, "war", false);
		addDependency(PROJECT_NAME_SEAM, "org.jboss.seam", "jboss-seam", "2.1.2.GA"); //dependency type EJB
		updateConf(PROJECT_NAME_SEAM);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, SEAM_FACET, "2.1"));
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