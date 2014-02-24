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
import org.jboss.tools.maven.reddeer.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizardSecondPage;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizardThirdPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class ArchetypesTest extends AbstractMavenSWTBotTest{


	@BeforeClass
	public static void setup(){
		setPerspective("Java");
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		mr.open();
		mr.removeAllRepos();
		mr.chooseRepositoryFromList(MavenRepositories.JBOSS_REPO, true);
		mr.confirm();
		jm.ok();
	}
	
	@AfterClass
	public static void clean(){
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		mr.removeAllRepos();
		mr.confirm();
		jm.ok();
	}
	
	
	@Test
	public void createSimpleJSFProjectArchetype() throws CoreException{
		String projectName= "JsfQuickstart";
		createArchetype(projectName, "Nexus Indexer", "maven-archetype-jsfwebapp");
		assertTrue(isMavenProject(projectName));
		buildProject(projectName, "..Maven build...","clean package",true); //version is 1.0.0
		checkWebTarget(projectName, projectName);
	}
	
	@Test
	public void createSimpleJarProjectArchetype() throws CoreException{
		String projectName= "ArchetypeQuickstart";
		createArchetype(projectName, "All Catalogs", "maven-archetype-quickstart");
		assertNoErrors(projectName);
		assertTrue(isMavenProject(projectName));
		buildProject(projectName, "..Maven build...","clean package",true); //version is 1.0.0
	}
	
	
	
	private void createArchetype(String name, String catalog, String type){
		MavenProjectWizard md = new MavenProjectWizard();
		md.open();
		md.selectPage(1);
		MavenProjectWizardSecondPage fp = (MavenProjectWizardSecondPage)md.getWizardPage();
		fp.selectArchetype(catalog, type);
		md.selectPage(2);
		MavenProjectWizardThirdPage tp = (MavenProjectWizardThirdPage)md.getWizardPage();
		tp.setGAV(name, name, null);
		md.finish();
	}
}