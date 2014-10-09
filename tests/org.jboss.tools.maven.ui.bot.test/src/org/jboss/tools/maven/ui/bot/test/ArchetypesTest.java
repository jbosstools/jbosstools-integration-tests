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

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
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
@CleanWorkspace
@OpenPerspective(JavaPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class ArchetypesTest extends AbstractMavenSWTBotTest{


	@BeforeClass
	public static void setup(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		mr.removeAllRepos();
		mr.chooseRepositoryFromList(MavenRepositories.JBOSS_REPO, true);
		mr.confirm();
		preferenceDialog.ok();
	}
	
	@AfterClass
	public static void clean(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		mr.removeAllRepos();
		mr.confirm();
		preferenceDialog.ok();
	}
	
	@Test
	public void createSimpleJarProjectArchetype(){
		String projectName= "ArchetypeQuickstart";
		createArchetype(projectName, "All Catalogs", "maven-archetype-quickstart");
		buildProject(projectName, "..Maven build...","clean package",true); //version is 1.0.0
	}
	
	
	
	private void createArchetype(String name, String catalog, String type){
		MavenProjectWizard md = new MavenProjectWizard();
		md.open();
		MavenProjectWizardSecondPage fp = (MavenProjectWizardSecondPage)md.getWizardPage(1);
		fp.selectArchetype(catalog, type);
		MavenProjectWizardThirdPage tp = (MavenProjectWizardThirdPage)md.getWizardPage(2);
		tp.setGAV(name, name, null);
		md.finish();
	}
}