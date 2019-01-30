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

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.EarProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.EarProjectInstallPage;
import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.EarProjectWizard;
import org.eclipse.reddeer.eclipse.jst.j2ee.wizard.DefaultJ2EEComponentCreationWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Rastislav Wagner
 * 
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT)
public class EARProjectTest extends AbstractMavenSWTBotTest{
	
	public static final String WAR_PROJECT_NAME="earWeb";
	public static final String EJB_PROJECT_NAME="earEJB";
	public static final String EAR_PROJECT_NAME="ear";
	
	@Test
	public void testEARProject() throws InterruptedException{
		createEARProject();		
		prepareProject(EAR_PROJECT_NAME+"EJB", "..Maven build...","ejb");
		prepareProject(EAR_PROJECT_NAME+"Connector", "..Maven build...","rar");
		prepareProject(EAR_PROJECT_NAME+"Client", "..Maven build...","app-client");
		prepareProject(EAR_PROJECT_NAME+"Web","..Maven build...","war");
		convertToMavenProject(EAR_PROJECT_NAME, "ear",true);
		buildProject(EAR_PROJECT_NAME, "..Maven build...", "clean package",true);
		ProjectItem targetFiles = getTargetFiles(EAR_PROJECT_NAME);
		assertTrue(targetFiles.getResource(EAR_PROJECT_NAME+"EJB-"+EAR_PROJECT_NAME+"EJB-0.0.1-SNAPSHOT.jar") != null);
		assertTrue(targetFiles.getResource(EAR_PROJECT_NAME+"Client-"+EAR_PROJECT_NAME+"Client-0.0.1-SNAPSHOT.jar") != null);
		assertTrue(targetFiles.getResource(EAR_PROJECT_NAME+"Connector-"+EAR_PROJECT_NAME+"Connector-0.0.1-SNAPSHOT.rar") != null);
		assertTrue(targetFiles.getResource(EAR_PROJECT_NAME+"Web-"+EAR_PROJECT_NAME+"Web-0.0.1-SNAPSHOT.war") != null);
	}
	
	public ProjectItem getTargetFiles(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenuItem("Refresh").select();
		return pe.getProject(projectName).getProjectItem("target",projectName+"-0.0.1-SNAPSHOT");
	}
	
	public void generateWebXmlForProject(String projectName) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu().getItem("Java EE Tools","Generate Deployment Descriptor Stub").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	public void prepareProject(String projectName, String mavenBuild,String packaging){
		convertToMavenProject(projectName,packaging, false);
		if(projectName.toLowerCase().contains("web")) {
			generateWebXmlForProject(projectName);
		}
		buildProject(projectName, mavenBuild, "clean install",true);
		checkPackaging(projectName, packaging);
	}
	
	public void createEARProject(){
		EarProjectWizard earw = new EarProjectWizard();
		earw.open();
		EarProjectFirstPage ef = new EarProjectFirstPage(earw);
		ef.setProjectName(EAR_PROJECT_NAME);
		ef.setTargetRuntime("<None>");
		earw.next();
		EarProjectInstallPage es = new EarProjectInstallPage(earw);
		DefaultJ2EEComponentCreationWizard p = es.newModule();
		p.finish();
		earw.finish();
	}
	
}