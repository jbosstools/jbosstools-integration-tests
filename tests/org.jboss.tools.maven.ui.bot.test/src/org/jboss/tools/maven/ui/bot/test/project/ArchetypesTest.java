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
package org.jboss.tools.maven.ui.bot.test.project;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.DefineMavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.PredefinedMavenRepository;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizardSecondPage;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizardThirdPage;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
@OpenPerspective(JavaPerspective.class)
@JBossServer(state=ServerRequirementState.RUNNING)
@DefineMavenRepository(predefinedRepositories = { @PredefinedMavenRepository(ID="jboss-public-repository",snapshots=true) })
public class ArchetypesTest extends AbstractMavenSWTBotTest{
	
	@Test
	public void createSimpleJarProjectArchetype(){
		String projectName= "ArchetypeQuickstart";
		createArchetype(projectName, "All Catalogs", "maven-archetype-quickstart");
		buildProject(projectName, "..Maven build...","clean package",true); //version is 1.0.0
	}
	
	
	
	private void createArchetype(String name, String catalog, String type){
		MavenProjectWizard md = new MavenProjectWizard();
		md.open();
		md.next();
		MavenProjectWizardSecondPage fp = new MavenProjectWizardSecondPage(md);
		fp.selectArchetype(catalog, type);
		md.next();
		MavenProjectWizardThirdPage tp = new MavenProjectWizardThirdPage(md);
		tp.setGAV(name, name, null);
		md.finish();
	}
}