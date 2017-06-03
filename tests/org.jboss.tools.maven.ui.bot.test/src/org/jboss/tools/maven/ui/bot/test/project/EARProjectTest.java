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

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jst.j2ee.ui.project.facet.EarProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.j2ee.ui.project.facet.EarProjectInstallPage;
import org.jboss.reddeer.eclipse.jst.j2ee.ui.project.facet.EarProjectWizard;
import org.jboss.reddeer.eclipse.jst.j2ee.wizard.NewJ2EEComponentSelectionPage;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;

/**
 * @author Rastislav Wagner
 * 
 */
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY10x)
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
		assertTrue(targetFiles.getChild(EAR_PROJECT_NAME+"EJB-0.0.1-SNAPSHOT.jar") != null);
		assertTrue(targetFiles.getChild(EAR_PROJECT_NAME+"Client-0.0.1-SNAPSHOT.jar") != null);
		assertTrue(targetFiles.getChild(EAR_PROJECT_NAME+"Connector-0.0.1-SNAPSHOT.rar") != null);
		assertTrue(targetFiles.getChild(EAR_PROJECT_NAME+"Web-0.0.1-SNAPSHOT.war") != null);
		
	}
	
	public ProjectItem getTargetFiles(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu("Refresh").select();
		return pe.getProject(projectName).getProjectItem("target",projectName+"-0.0.1-SNAPSHOT");
	}
	
	public void prepareProject(String projectName, String mavenBuild,String packaging){
		convertToMavenProject(projectName,packaging, false);
		buildProject(projectName, mavenBuild, "clean install",true);
		checkPackaging(projectName, packaging);
	}
	
	public void createEARProject(){
		EarProjectWizard earw = new EarProjectWizard();
		earw.open();
		EarProjectFirstPage ef = new EarProjectFirstPage();
		ef.setProjectName(EAR_PROJECT_NAME);
		ef.setTargetRuntime("<None>");
		earw.next();
		EarProjectInstallPage es = new EarProjectInstallPage();
		NewJ2EEComponentSelectionPage p = es.newModule();
		p.finish();
		earw.finish();
	}
	
}